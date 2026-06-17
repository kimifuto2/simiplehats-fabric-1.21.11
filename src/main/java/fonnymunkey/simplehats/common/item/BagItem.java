package fonnymunkey.simplehats.common.item;

import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.common.init.ModRegistry;
import fonnymunkey.simplehats.util.HatEntry;
import fonnymunkey.simplehats.util.HatEntry.HatSeason;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;

public class BagItem extends Item {
    private HatSeason hatSeason = HatSeason.NONE;
    private boolean seasonal = false;
    private Rarity rarity = Rarity.COMMON;
    private List<HatItem> availableHatList = new ArrayList<>();

    public BagItem(Rarity rarity) {
        super(new Item.Properties().rarity(rarity));
        this.rarity = rarity;
    }

    public BagItem(Item.Properties props, Rarity rarity) {
        super(props.rarity(rarity));
        this.rarity = rarity;
    }

    public BagItem(HatSeason hatSeason) {
        super(new Item.Properties().rarity(Rarity.EPIC));
        this.hatSeason = hatSeason;
        this.seasonal = true;
        this.rarity = Rarity.EPIC;
    }

    public BagItem(Item.Properties props, HatSeason hatSeason) {
        super(props.rarity(Rarity.EPIC));
        this.hatSeason = hatSeason;
        this.seasonal = true;
        this.rarity = Rarity.EPIC;
    }

    public static SoundEvent getUnwrapFinishSound() { return SoundEvents.ARMOR_EQUIP_GENERIC.value(); }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.playSound(getUnwrapFinishSound(), 1.0F, 1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.4F);
        stack.shrink(1);
        if(!level.isClientSide()) {
            if(!this.seasonal && HatSeason.getSeason() != HatSeason.NONE) {
                if(level.getRandom().nextFloat()*100.0F < SimpleHats.config.common.seasonalBagChance)
                    player.drop(new ItemStack(getSeasonalBag()), false, true);
            }
            player.drop(new ItemStack(getBagResult(level)), false, true);
        }
        return InteractionResult.SUCCESS;
    }

    private static Item getSeasonalBag() {
        return switch(HatSeason.getSeason()) {
            case EASTER -> ModRegistry.HATBAG_EASTER;
            case SUMMER -> ModRegistry.HATBAG_SUMMER;
            case HALLOWEEN -> ModRegistry.HATBAG_HALLOWEEN;
            case FESTIVE -> ModRegistry.HATBAG_FESTIVE;
            default -> { SimpleHats.logger.log(org.apache.logging.log4j.Level.ERROR, "Failed bag type."); yield Items.AIR; }
        };
    }

    private Item getBagResult(Level level) {
        if(availableHatList.isEmpty()) {
            for(HatItem hat : ModRegistry.hatList) {
                if((hat.getHatEntry().getHatRarity() == this.rarity || this.seasonal) &&
                    hat.getHatEntry().getHatWeight() != 0 &&
                    hat.getHatEntry().getHatSeason() == this.hatSeason)
                    availableHatList.add(hat);
            }
            if(availableHatList.isEmpty()) return Items.AIR;
        }
        return availableHatList.get(level.random.nextInt(availableHatList.size()));
    }
}
