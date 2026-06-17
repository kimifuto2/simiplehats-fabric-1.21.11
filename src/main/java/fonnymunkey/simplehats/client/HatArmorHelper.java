package fonnymunkey.simplehats.client;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.world.entity.LivingEntity;

public class HatArmorHelper {
    public static final RenderStateDataKey<LivingEntity> ENTITY_KEY =
        RenderStateDataKey.create(() -> "simplehats:entity");
}
