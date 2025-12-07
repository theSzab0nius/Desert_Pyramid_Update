package net.mossclock.desert_pyramid_update.item;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.mossclock.desert_pyramid_update.enchantment.ModEnchantments;
import net.mossclock.desert_pyramid_update.enchantment.custom.InvisibleEnchantment;

import java.util.Collection;
import java.util.Map;

public class IronAlloyKhopeshItem extends SwordItem {

    private static final float ARMOR_DAMAGE_BONUS_PER_POINT = 0.1f;
    private static final float BASE_SWEEP_DAMAGE = 2.0f;
    private static final double BASE_SWEEP_RANGE = 3.0d;
    public IronAlloyKhopeshItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Bonus vs ARMOR on the primary hit
        float armor = target.getArmor(); // armour points
        if (armor > 0.0F) {
            float bonus = armor * ARMOR_DAMAGE_BONUS_PER_POINT;

            target.damage(attacker.getDamageSources().generic(), bonus);

        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // keep vanilla durability / callbacks from SwordItem
        super.postDamageEntity(stack, target, attacker);

        // server-side only
        World world = attacker.getWorld();
        if (world.isClient) return;

        // compute sweep range; we will optionally scale by Sweeping Edge enchantment level if available
        double range = BASE_SWEEP_RANGE;

        int sweepingLevel = 0;
        try {
            RegistryEntry<Enchantment> sweepingEntry = null;


            var registry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);

            var maybe = registry.getEntry(Enchantments.SWEEPING_EDGE);
            if (maybe.isPresent()) {
                sweepingEntry = maybe.get();
            }

            if (sweepingEntry != null) {
                sweepingLevel = EnchantmentHelper.getLevel(sweepingEntry, stack);
            }
        } catch (Exception e) {

            sweepingLevel = 0;
        }

        range += sweepingLevel;

        double rangeSq = range * range;

        Box box = attacker.getBoundingBox().expand(range, 0.5D, range);

        // apply sweeping damage to other nearby LivingEntity instances (exclude the primary target + attacker)
        for (var entity : world.getOtherEntities(attacker, box, e -> e instanceof LivingEntity && e != target && e != attacker)) {
            if (!(entity instanceof LivingEntity swept)) continue;

            // distance check (optional)
            Vec3d center = attacker.getPos();
            double dx = swept.getX() - center.x;
            double dz = swept.getZ() - center.z;
            if (dx * dx + dz * dz > rangeSq) continue;

            // damage amount: base + small increase per sweeping level (you can change this)
            float damage = BASE_SWEEP_DAMAGE + (0.5f * sweepingLevel);

            // apply damage using a generic damage source (works for both player and mob attackers)
            swept.damage(attacker.getDamageSources().generic(), damage);
        }
    }



    @Override
    public boolean hasGlint(ItemStack stack) {

        ItemEnchantmentsComponent enchants = EnchantmentHelper.getEnchantments(stack);

        if (!EnchantmentHelper.hasEnchantments(stack)) {
            return false; // no enchantments, no glint
        }

        int enchantmentCount = enchants.getEnchantmentEntries().size();

        // If there is exactly one enchantment, and it is your invisible one, no glint
        if (enchantmentCount == 1) {
            Object2IntMap.Entry<RegistryEntry<Enchantment>> entry = enchants.getEnchantmentEntries().iterator().next();
            RegistryKey<Enchantment> key = entry.getKey().getKey().get();
            if (key.equals(ModEnchantments.INVISIBLE_ENCHANTMENT)) {
                return false;
            }
        }

        // All other cases -> glint
        return true;
    }
}

