package fonnymunkey.simplehats.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.TrinketItem;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.util.HatEntry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class HatItem extends TrinketItem {
    private HatEntry hatEntry;

    public HatItem(HatEntry entry) {
        super(new Item.Properties().stacksTo(1).rarity(entry.getHatRarity()).fireResistant());
        this.hatEntry = entry;
    }

    public HatItem(Item.Properties props, HatEntry entry) {
        super(props.stacksTo(1).rarity(entry.getHatRarity()).fireResistant());
        this.hatEntry = entry;
    }

    public HatEntry getHatEntry() { return this.hatEntry; }

    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.level().isClientSide()) return;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        if(getHatEntry().getHatVariantRange() > 0) tooltip.add(Component.translatable("tooltip.simplehats.variant"));
        if(getHatEntry().getHatName().equalsIgnoreCase("special")) {
            if(stack.has(DataComponents.CUSTOM_MODEL_DATA))
                tooltip.add(Component.translatable("tooltip.simplehats.special_true"));
            else
                tooltip.add(Component.translatable("tooltip.simplehats.special_false"));
        }
    }

    public TrinketEnums.DropRule getDropRule(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity instanceof Player && SimpleHats.config.common.keepHatOnDeath) return TrinketEnums.DropRule.KEEP;
        return TrinketEnums.DropRule.DEFAULT;
    }
}
