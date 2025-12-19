package net.mossclock.desert_pyramid_update.util;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementUtils {

    public static void grantIfFirstTime(ServerPlayerEntity player, String advancementId) {
        Identifier advId = Identifier.of("desert_pyramid_update", advancementId);
        AdvancementEntry advancement = player.getServer().getAdvancementLoader().get(advId);

        if (advancement != null && !player.getAdvancementTracker().getProgress(advancement).isDone()) {
            player.getAdvancementTracker().grantCriterion(advancement, "impossible");  // ← Fixed!
        }
    }
}