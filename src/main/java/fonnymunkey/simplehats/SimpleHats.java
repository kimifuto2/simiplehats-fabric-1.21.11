package fonnymunkey.simplehats;

import fonnymunkey.simplehats.common.init.HatJson;
import fonnymunkey.simplehats.common.init.ModConfig;
import fonnymunkey.simplehats.common.init.ModRegistry;
import fonnymunkey.simplehats.common.item.HatItem;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleHats implements ModInitializer {
    public static final String modId = "simplehats";
    public static Logger logger = LogManager.getLogger();
    public static ModConfig config;

    public static final CreativeModeTab HAT_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModRegistry.HATICON))
            .title(Component.translatable("itemGroup.simplehats.hat_group"))
            .displayItems((context, entries) -> {
                entries.accept(ModRegistry.HATBAG_COMMON);
                entries.accept(ModRegistry.HATBAG_UNCOMMON);
                entries.accept(ModRegistry.HATBAG_RARE);
                entries.accept(ModRegistry.HATBAG_EPIC);
                entries.accept(ModRegistry.HATBAG_EASTER);
                entries.accept(ModRegistry.HATBAG_SUMMER);
                entries.accept(ModRegistry.HATBAG_HALLOWEEN);
                entries.accept(ModRegistry.HATBAG_FESTIVE);
                entries.accept(ModRegistry.HATSCRAPS_COMMON);
                entries.accept(ModRegistry.HATSCRAPS_UNCOMMON);
                entries.accept(ModRegistry.HATSCRAPS_RARE);
                entries.accept(ModRegistry.HATSCRAPS_EASTER);
                entries.accept(ModRegistry.HATSCRAPS_SUMMER);
                entries.accept(ModRegistry.HATSCRAPS_HALLOWEEN);
                entries.accept(ModRegistry.HATSCRAPS_FESTIVE);
                entries.accept(ModRegistry.HATICON);
                for(HatItem hat : ModRegistry.hatList) entries.accept(hat);
            }).build();

    @Override
    public void onInitialize() {
        config = AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(Toml4jConfigSerializer::new)).getConfig();
        HatJson.registerHatJson();
        ModRegistry.registerHats();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, 
            net.minecraft.resources.Identifier.fromNamespaceAndPath(modId, "hat_group"), HAT_TAB);

        LootTableEvents.MODIFY.register((id, tableBuilder, source, registryLookup) -> {
            if(SimpleHats.config.common.enableChestLoot && ModRegistry.LOOT_HATINJECT_CHEST.contains(id)) {
                LootPool.Builder pool = LootPool.lootPool()
                        .add(LootItem.lootTableItem(ModRegistry.HATBAG_COMMON).setWeight(SimpleHats.config.common.chestCommonWeight))
                        .add(LootItem.lootTableItem(ModRegistry.HATBAG_UNCOMMON).setWeight(SimpleHats.config.common.chestUncommonWeight))
                        .add(LootItem.lootTableItem(ModRegistry.HATBAG_RARE).setWeight(SimpleHats.config.common.chestRareWeight))
                        .add(LootItem.lootTableItem(ModRegistry.HATBAG_EPIC).setWeight(SimpleHats.config.common.chestEpicWeight))
                        .add(LootItem.lootTableItem(ItemStack.EMPTY.getItem()).setWeight(SimpleHats.config.common.chestNoneWeight))
                        .setRolls(UniformGenerator.between(1.0F, 2.0F));
                tableBuilder.pool(pool.build());
            } else if(SimpleHats.config.common.enableMobLoot && ModRegistry.LOOT_HATINJECT_ENTITY.contains(id)) {
                LootPool.Builder pool = LootPool.lootPool()
                        .add(LootItem.lootTableItem(ModRegistry.HATBAG_COMMON).setWeight(SimpleHats.config.common.entityCommonWeight))
                        .add(LootItem.lootTableItem(ModRegistry.HATBAG_UNCOMMON).setWeight(SimpleHats.config.common.entityUncommonWeight))
                        .add(LootItem.lootTableItem(ModRegistry.HATBAG_RARE).setWeight(SimpleHats.config.common.entityRareWeight))
                        .add(LootItem.lootTableItem(ModRegistry.HATBAG_EPIC).setWeight(SimpleHats.config.common.entityEpicWeight))
                        .add(LootItem.lootTableItem(ItemStack.EMPTY.getItem()).setWeight(SimpleHats.config.common.entityNoneWeight))
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer());
                tableBuilder.pool(pool.build());
            }
        });
    }
}
