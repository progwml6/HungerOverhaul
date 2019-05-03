package iguanaman.hungeroverhaul.module.loot;

import com.google.common.collect.ImmutableSet;
import iguanaman.hungeroverhaul.common.config.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodValues;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.world.storage.loot.LootTableList.CHESTS_ABANDONED_MINESHAFT;
import static net.minecraft.world.storage.loot.LootTableList.CHESTS_DESERT_PYRAMID;
import static net.minecraft.world.storage.loot.LootTableList.CHESTS_JUNGLE_TEMPLE;
import static net.minecraft.world.storage.loot.LootTableList.CHESTS_SIMPLE_DUNGEON;

public class LootModule
{
    private static final Set<String> LOOT_LOCATIONS = ImmutableSet.<String>builder()
            .add(CHESTS_SIMPLE_DUNGEON.toString())
            .add(CHESTS_ABANDONED_MINESHAFT.toString())
            .add(CHESTS_DESERT_PYRAMID.toString())
            .add(CHESTS_JUNGLE_TEMPLE.toString())
            .build();

    private final static Set<LootPool> lootPools = new HashSet<LootPool>();

    public static void postInit()
    {
        boolean worthLooping = Config.modifyFoodStackSize || Config.addTradesButcher || Config.addHarvestCraftChestLoot || Config.addTradesFarmer || Config.addSaplingTradesFarmer;

        if (worthLooping)
        {
            for (Object obj : Item.REGISTRY)
            {
                Item item = (Item) obj;

                if (item instanceof ItemFood)
                {
                    ItemStack stack = new ItemStack(item);
                    FoodValues values = FoodValues.get(stack);

                    if (values.hunger > 9)
                    {
                        if (Config.addHarvestCraftChestLoot)
                        {
                            addChestLoot(item, stack, values);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
        if (LOOT_LOCATIONS.contains(event.getName().toString()))
        {
            for (LootPool pool : lootPools)
            {
                event.getTable().addPool(pool);
            }
        }
    }

    public static void addChestLoot(Item item, ItemStack stack, FoodValues values)
    {
        int maxStackSize = Math.min(Config.chestLootMaxStackSize, item.getItemStackLimit(stack));

        LootCondition[] lootConditions = new LootCondition[0];
        LootFunction[] setMeta = new LootFunction[] { new SetMetadata(lootConditions, new RandomValueRange(stack.getMetadata())) };
        LootEntry entry = new LootEntryItem(stack.getItem(), 1, maxStackSize, setMeta, lootConditions, stack.getUnlocalizedName());
        LootCondition chance = new RandomChance(Config.chestLootChance);

        lootPools.add(new LootPool(new LootEntry[] { entry }, new LootCondition[] { chance }, new RandomValueRange(1), new RandomValueRange(0), stack.getUnlocalizedName()));
    }
}
