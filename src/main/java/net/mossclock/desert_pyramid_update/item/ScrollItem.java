package net.mossclock.desert_pyramid_update.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.EnchantedBookItem;

public class ScrollItem extends EnchantedBookItem {

    public ScrollItem(Settings settings) {
        super(settings
                .component(DataComponentTypes.STORED_ENCHANTMENTS, new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT)
         // ← THIS IS THE KEY LINE
                        .build()
                )
                .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)

        );
    }
}
