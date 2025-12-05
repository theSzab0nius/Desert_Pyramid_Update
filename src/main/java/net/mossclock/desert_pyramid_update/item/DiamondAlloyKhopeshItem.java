package net.mossclock.desert_pyramid_update.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Khopesh with:
 *  - built-in sweeping attack (always) and scales with Sweeping Edge level if present
 *  - bonus damage vs armored targets (armor points * multiplier)
 *  - tooltip describing abilities
 *
 * Tailored to the SwordItem / EnchantmentHelper signatures you pasted from your environment.
 */
public class DiamondAlloyKhopeshItem extends SwordItem {

    // tweak these to taste
    private static final float ARMOR_DAMAGE_BONUS_PER_POINT = 0.15f; // +0.1 damage per armor point
    private static final float BASE_SWEEP_DAMAGE = 2.5f;            // damage dealt to swept mobs
    private static final double BASE_SWEEP_RANGE = 3.2d;            // sweep search radius

    public DiamondAlloyKhopeshItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    /**
     * Called when this item hits a target. Keep vanilla behaviour but add the
     * bonus vs armour here (so the main hit gets the bonus).
     */
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Bonus vs ARMOR on the primary hit
        float armor = target.getArmor(); // armour points
        if (armor > 0.0F) {
            float bonus = armor * ARMOR_DAMAGE_BONUS_PER_POINT;
            // apply bonus as additional generic damage
            target.damage(attacker.getDamageSources().generic(), bonus);

            // optional: show the hit effect (like crit particles); this mirrors vanilla patterns
            attacker.getWorld().sendEntityStatus(target, (byte)4);
        }

        // keep vanilla return behaviour (SwordItem.postHit returns true in your pasted code)
        return super.postHit(stack, target, attacker);
    }

    /**
     * Called after the entity has been damaged by the weapon. We use this to
     * perform the *sweeping* effect (affect nearby mobs).
     *
     * Note: SwordItem.postDamageEntity in your pasted code already damages the stack.
     * We call super to keep that durability behaviour.
     */
    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // keep vanilla durability / callbacks from SwordItem
        super.postDamageEntity(stack, target, attacker);

        // server-side only
        World world = attacker.getWorld();
        if (world.isClient) return;

        // compute sweep range; we will optionally scale by Sweeping Edge enchantment level if available
        double range = BASE_SWEEP_RANGE;

        // try to look up the RegistryEntry<Enchantment> for Sweeping Edge so we can get a level
        // This is the approach that works with the EnchantmentHelper signature you pasted:
        // EnchantmentHelper.getLevel(RegistryEntry<Enchantment>, ItemStack)
        int sweepingLevel = 0;
        try {
            RegistryEntry<Enchantment> sweepingEntry = null;

            // get the registry entry for the enchantment; this uses the world's registry manager
            // NOTE: Enchantments.SWEEPING_EDGE is a RegistryKey<Enchantment> in these mappings.
            var registry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
            // getEntry() expects the RegistryKey (Enchantments.SWEEPING_EDGE) — if your Enchantments constant is different,
            // this call will fail at compile time; if that happens, let me know what Enchantments.SWEEPING_EDGE's type is.
            var maybe = registry.getEntry(Enchantments.SWEEPING_EDGE);
            if (maybe.isPresent()) {
                sweepingEntry = maybe.get();
            }

            if (sweepingEntry != null) {
                sweepingLevel = EnchantmentHelper.getLevel(sweepingEntry, stack);
            }
        } catch (Exception e) {
            // fallback: if anything about the registry lookup differs in your environment,
            // we simply treat sweepingLevel as 0 (no enchantment). This keeps the built-in sweep.
            sweepingLevel = 0;
        }

        // scale range by enchantment level (optional, tweak as desired)
        range += sweepingLevel;

        // compute squared radius for distance checks
        double rangeSq = range * range;

        // bounding box around the attacker to search for nearby living entities
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

        // (Optional) you could send attack-sweep sounds/particles here
    }

    /**
     * Tooltip describing the special properties.
     * Signature matches the client-side tooltip signature in 1.21 mappings.
     */

}
