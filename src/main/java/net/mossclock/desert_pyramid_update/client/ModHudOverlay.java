package net.mossclock.desert_pyramid_update.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModHudOverlay implements HudRenderCallback {

    public static final TagKey<net.minecraft.entity.effect.StatusEffect> BLESSINGS =
            TagKey.of(RegistryKeys.STATUS_EFFECT, Identifier.of("desert_pyramid_update", "blessings"));

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {


    }
}
