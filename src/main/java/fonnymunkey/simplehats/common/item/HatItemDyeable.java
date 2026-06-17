package fonnymunkey.simplehats.common.item;

import fonnymunkey.simplehats.util.HatEntry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class HatItemDyeable extends HatItem {
    public HatItemDyeable(HatEntry entry) { super(entry); }
    public HatItemDyeable(Item.Properties props, HatEntry entry) { super(props, entry); }

    public int getColor(ItemStack stack) {
        return 0xFF000000 | (stack.has(DataComponents.DYED_COLOR) ? stack.get(DataComponents.DYED_COLOR).rgb() : getHatEntry().getHatDyeSettings().getColorCode());
    }

    private static final String[] colorList = {"\u00A7c","\u00A7e","\u00A7a","\u00A7b","\u00A79","\u00A7d","\u00A75"};

    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        MutableComponent c = Component.empty();
        char[] chars = Component.translatable("tooltip.simplehats.dyeable").getString().toCharArray();
        for(int i = 0; i < chars.length; i++) c.append(colorList[i%colorList.length] + chars[i]);
        tooltip.add(c);
    }
}
