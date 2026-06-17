package fonnymunkey.simplehats.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import fonnymunkey.simplehats.SimpleHatsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HatItemRenderer implements TrinketRenderer {

    @Override
    public void render(ItemStack stack, SlotReference slotRef, EntityModel<? extends LivingEntityRenderState> model,
                       PoseStack matrices, SubmitNodeCollector collector,
                       int light, LivingEntityRenderState state, float limbAngle, float limbDistance) {
        if (!(model instanceof HeadedModel headModel)) return;

        model.root().translateAndRotate(matrices);
        headModel.getHead().translateAndRotate(matrices);

        matrices.translate(0.0F, -0.25F, 0.0F);
        matrices.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrices.scale(0.625F, -0.625F, -0.625F);

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStackRenderState renderState = new ItemStackRenderState();
        SimpleHatsClient.getItemModelResolver().updateForLiving(renderState, stack, ItemDisplayContext.HEAD, mc.player);
        renderState.submit(matrices, collector, light, OverlayTexture.NO_OVERLAY, 0);
    }
}
