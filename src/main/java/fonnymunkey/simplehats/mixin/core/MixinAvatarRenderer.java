package fonnymunkey.simplehats.mixin.core;

import dev.emi.trinkets.api.TrinketsApi;
import fonnymunkey.simplehats.client.HatArmorHelper;
import fonnymunkey.simplehats.common.item.HatItem;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class MixinAvatarRenderer {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V", at = @At("RETURN"))
    public void simplehats_storeEntity(Entity entity, EntityRenderState state, float tickDelta, CallbackInfo ci) {
        if(entity instanceof LivingEntity living && state instanceof FabricRenderState fabricState) {
            fabricState.setData(HatArmorHelper.ENTITY_KEY, living);

            if (state instanceof HumanoidRenderState humanoidState) {
                boolean hasHat = TrinketsApi.getTrinketComponent(living)
                    .map(component -> {
                        for (var slotRef : component.getAllEquipped()) {
                            if (!slotRef.getB().isEmpty() && slotRef.getB().getItem() instanceof HatItem) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .orElse(false);

                if (hasHat) {
                    humanoidState.headEquipment = ItemStack.EMPTY;
                }
            }
        }
    }
}
