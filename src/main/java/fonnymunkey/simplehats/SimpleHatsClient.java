package fonnymunkey.simplehats;

import dev.emi.trinkets.api.TrinketsApi;
import fonnymunkey.simplehats.client.HatModel;
import fonnymunkey.simplehats.common.init.ModRegistry;
import fonnymunkey.simplehats.common.item.HatItem;
import fonnymunkey.simplehats.util.HatEntry;
import fonnymunkey.simplehats.client.HatItemRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class SimpleHatsClient implements ClientModInitializer {
    private static ItemModelResolver itemModelResolver;

    public static ItemModelResolver getItemModelResolver() {
        if (itemModelResolver == null) {
            itemModelResolver = new ItemModelResolver(Minecraft.getInstance().getModelManager());
        }
        return itemModelResolver;
    }

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(HatModel.LAYER_LOCATION, HatModel::createLayer);
        
        HatItemRenderer hatRenderer = new HatItemRenderer();
        for(Item hat : ModRegistry.hatList) {
            TrinketRendererRegistry.registerRenderer(hat, hatRenderer);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level == null || client.isPaused()) return;
            for (Player player : client.level.players()) {
                TrinketsApi.getTrinketComponent(player).ifPresent(component -> {
                    component.forEach((slotRef, stack) -> {
                        if (stack.isEmpty() || !(stack.getItem() instanceof HatItem hatItem)) return;
                        HatEntry.HatParticleSettings ps = hatItem.getHatEntry().getHatParticleSettings();
                        if (!ps.getUseParticles()) return;
                        if (player.getRandom().nextFloat() >= (player.isInvisible() ? ps.getParticleFrequency() / 2 : ps.getParticleFrequency())) return;

                        double d0 = player.getRandom().nextGaussian() * 0.02;
                        double d1 = player.getRandom().nextGaussian() * 0.02;
                        double d2 = player.getRandom().nextGaussian() * 0.02;
                        double y = switch (ps.getParticleMovement()) {
                            case TRAILING_HEAD -> player.getY() + 1.75;
                            case TRAILING_FEET -> player.getY() + 0.25;
                            case TRAILING_FULL -> player.getRandomY();
                        };
                        double px = player.getX() + player.getRandom().nextFloat() - 0.5;
                        double pz = player.getZ() + player.getRandom().nextFloat() - 0.5;

                        ParticleType<?> particleType = ps.getParticleType();
                        if (particleType instanceof SimpleParticleType spt) {
                            client.level.addParticle(spt, px, y, pz, d0, d1, d2);
                        }
                    });
                });
            }
        });
    }
}
