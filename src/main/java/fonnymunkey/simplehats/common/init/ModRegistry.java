package fonnymunkey.simplehats.common.init;

import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.common.item.BagItem;
import fonnymunkey.simplehats.common.item.HatItem;
import fonnymunkey.simplehats.common.item.HatItemDyeable;
import fonnymunkey.simplehats.util.HatEntry;
import fonnymunkey.simplehats.util.HatEntry.HatSeason;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModRegistry {
    public static List<HatItem> hatList = new ArrayList<>();

    public static final BagItem HATBAG_COMMON = item("hatbag_common", props -> new BagItem(props, Rarity.COMMON));
    public static final BagItem HATBAG_UNCOMMON = item("hatbag_uncommon", props -> new BagItem(props, Rarity.UNCOMMON));
    public static final BagItem HATBAG_RARE = item("hatbag_rare", props -> new BagItem(props, Rarity.RARE));
    public static final BagItem HATBAG_EPIC = item("hatbag_epic", props -> new BagItem(props, Rarity.EPIC));
    public static final BagItem HATBAG_EASTER = item("hatbag_easter", props -> new BagItem(props, HatSeason.EASTER));
    public static final BagItem HATBAG_SUMMER = item("hatbag_summer", props -> new BagItem(props, HatSeason.SUMMER));
    public static final BagItem HATBAG_HALLOWEEN = item("hatbag_halloween", props -> new BagItem(props, HatSeason.HALLOWEEN));
    public static final BagItem HATBAG_FESTIVE = item("hatbag_festive", props -> new BagItem(props, HatSeason.FESTIVE));
    public static final Item HATSCRAPS_COMMON = rItem("hatscraps_common");
    public static final Item HATSCRAPS_UNCOMMON = rItem("hatscraps_uncommon");
    public static final Item HATSCRAPS_RARE = rItem("hatscraps_rare");
    public static final Item HATSCRAPS_EASTER = rItem("hatscraps_easter");
    public static final Item HATSCRAPS_SUMMER = rItem("hatscraps_summer");
    public static final Item HATSCRAPS_HALLOWEEN = rItem("hatscraps_halloween");
    public static final Item HATSCRAPS_FESTIVE = rItem("hatscraps_festive");
    public static final Item HATICON = rItem("haticon");
    public static final HatItem HATSPECIAL = item("special", props -> new HatItem(props, new HatEntry("special", Rarity.EPIC, 0)));

    @SuppressWarnings("unchecked")
    private static <T extends Item> T item(String name, java.util.function.Function<Item.Properties, T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SimpleHats.modId, name));
        return (T)Items.registerItem(key, (java.util.function.Function<Item.Properties, Item>) factory);
    }

    private static Item rItem(String name) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SimpleHats.modId, name));
        return Items.registerItem(key, Item::new);
    }

    public static void registerHats() {
        hatList.add(HATSPECIAL);
        for(HatEntry entry : HatJson.getHatList()) {
            final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SimpleHats.modId, entry.getHatName()));
            HatItem hat = (HatItem)Items.registerItem(key, (java.util.function.Function<Item.Properties, Item>)(props -> entry.getHatDyeSettings().getUseDye() ? new HatItemDyeable(props, entry) : new HatItem(props, entry)));
            hatList.add(hat);
        }
        SimpleHats.logger.log(Level.INFO, "Generated " + hatList.size() + " hat items from hat entries.");
    }

    public static final List<ResourceKey<LootTable>> LOOT_HATINJECT_CHEST = Arrays.<ResourceKey<LootTable>>asList(
            BuiltInLootTables.ABANDONED_MINESHAFT, BuiltInLootTables.NETHER_BRIDGE, BuiltInLootTables.STRONGHOLD_LIBRARY,
            BuiltInLootTables.DESERT_PYRAMID, BuiltInLootTables.JUNGLE_TEMPLE, BuiltInLootTables.WOODLAND_MANSION,
            BuiltInLootTables.BURIED_TREASURE, BuiltInLootTables.SHIPWRECK_TREASURE, BuiltInLootTables.PILLAGER_OUTPOST,
            BuiltInLootTables.SPAWN_BONUS_CHEST, BuiltInLootTables.END_CITY_TREASURE, BuiltInLootTables.SIMPLE_DUNGEON,
            BuiltInLootTables.VILLAGE_ARMORER, BuiltInLootTables.VILLAGE_TEMPLE, BuiltInLootTables.BASTION_TREASURE
    );
    public static final List<ResourceKey<LootTable>> LOOT_HATINJECT_ENTITY;

    static {
        List<ResourceKey<LootTable>> list = new ArrayList<>();
        list.add(EntityType.BLAZE.getDefaultLootTable().orElseThrow());
        list.add(EntityType.CAVE_SPIDER.getDefaultLootTable().orElseThrow());
        // Simplified list - main entity types
        list.add(EntityType.CREEPER.getDefaultLootTable().orElseThrow());
        list.add(EntityType.DROWNED.getDefaultLootTable().orElseThrow());
        list.add(EntityType.ENDERMAN.getDefaultLootTable().orElseThrow());
        list.add(EntityType.EVOKER.getDefaultLootTable().orElseThrow());
        list.add(EntityType.GHAST.getDefaultLootTable().orElseThrow());
        list.add(EntityType.GUARDIAN.getDefaultLootTable().orElseThrow());
        list.add(EntityType.HUSK.getDefaultLootTable().orElseThrow());
        list.add(EntityType.PHANTOM.getDefaultLootTable().orElseThrow());
        list.add(EntityType.PILLAGER.getDefaultLootTable().orElseThrow());
        list.add(EntityType.SKELETON.getDefaultLootTable().orElseThrow());
        list.add(EntityType.SPIDER.getDefaultLootTable().orElseThrow());
        list.add(EntityType.STRAY.getDefaultLootTable().orElseThrow());
        list.add(EntityType.VINDICATOR.getDefaultLootTable().orElseThrow());
        list.add(EntityType.WITCH.getDefaultLootTable().orElseThrow());
        list.add(EntityType.WITHER_SKELETON.getDefaultLootTable().orElseThrow());
        list.add(EntityType.ZOGLIN.getDefaultLootTable().orElseThrow());
        list.add(EntityType.ZOMBIE.getDefaultLootTable().orElseThrow());
        list.add(EntityType.HOGLIN.getDefaultLootTable().orElseThrow());
        list.add(EntityType.ZOMBIE_VILLAGER.getDefaultLootTable().orElseThrow());
        LOOT_HATINJECT_ENTITY = List.copyOf(list);
    }
}
