package net.mossclock.desert_pyramid_update.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.mossclock.desert_pyramid_update.enchantment.custom.CobraStrike;
import net.mossclock.desert_pyramid_update.enchantment.custom.SandsOfTime;
import net.mossclock.desert_pyramid_update.enchantment.custom.SunForged;

public class ScrollItem extends EnchantedBookItem {

    public ScrollItem(Settings settings) {
        super(settings
                .component(DataComponentTypes.STORED_ENCHANTMENTS, new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT)
                        .build()
                )
                //.component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false)

        );
    }


}

