package net.mossclock.desert_pyramid_update;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record BlessingsData(Set<String> activeBlessings) {
    // Empty set as default
    public BlessingsData() {
        this(new HashSet<>());
    }

    public static final Codec<BlessingsData> CODEC = Codec.STRING.listOf()
            .xmap(list -> {
                BlessingsData data = new BlessingsData();
                list.forEach(data.activeBlessings::add);
                return data;
            }, data -> List.copyOf(data.activeBlessings));

    public BlessingsData add(String id) {
        Set<String> newSet = new HashSet<>(activeBlessings);
        newSet.add(id);
        return new BlessingsData(newSet);
    }

    public BlessingsData remove(String id) {
        Set<String> newSet = new HashSet<>(activeBlessings);
        newSet.remove(id);
        return new BlessingsData(newSet);
    }

    public boolean has(String id) {
        return activeBlessings.contains(id);
    }
}