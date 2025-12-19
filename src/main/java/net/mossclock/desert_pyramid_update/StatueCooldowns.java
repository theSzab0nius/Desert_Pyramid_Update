package net.mossclock.desert_pyramid_update;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StatueCooldowns {
    // Stores last interaction time in milliseconds
    private static final Map<UUID, Long> lastUse = new ConcurrentHashMap<>();
    private static final long COOLDOWN_MS = 10_000; // 10 seconds

    public static boolean canUse(ServerPlayerEntity player) {
        long now = System.currentTimeMillis();
        return lastUse.getOrDefault(player.getUuid(), 0L) + COOLDOWN_MS <= now;
    }

    public static void markUsed(ServerPlayerEntity player) {
        lastUse.put(player.getUuid(), System.currentTimeMillis());
    }
}
