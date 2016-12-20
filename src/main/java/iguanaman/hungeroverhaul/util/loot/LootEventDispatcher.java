package iguanaman.hungeroverhaul.util.loot;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootEventDispatcher
{
    public LootEventDispatcher()
    {
        System.out.println("hi testing");
    }

    private static Map<ResourceLocation, LootTable> lootTableMap = new HashMap<ResourceLocation, LootTable>();

    public static LootTable getLootTable(ResourceLocation resourceLocation)
    {
        LootTable lootTable = lootTableMap.get(resourceLocation);
        if (lootTable == null)
        {
            throw new NullPointerException("No loot table loaded for resource location: " + resourceLocation.toString());
        }
        return lootTable;
    }

    public static void addItem(ResourceLocation location, WeightedRandomChestContent loot)
    {
        ResourceLocation hungerOverhaulLocation = new ResourceLocation("hungeroverhaul", location.getResourcePath());
        String hungerOverhaulLocationString = hungerOverhaulLocation.toString();

        LootTable lootTable = LootEventDispatcher.getLootTable(location);

        LootPool lootPool = lootTable.getPool(hungerOverhaulLocationString);
        if (lootPool == null)
        {
            lootPool = emptyPool(hungerOverhaulLocationString);
            lootTable.addPool(lootPool);
        }

        LootEntryItem lootEntryItem = new LootEntryItem(
                loot.getStack().getItem(),
                loot.getLootChance(),
                1,
                new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(loot.getMinStack(), loot.getMaxStack())) },
                new LootCondition[0],
                "hungeroverhaul");
        lootPool.addEntry(lootEntryItem);
    }

    private static LootPool emptyPool(String name)
    {
        return new LootPool(
                new LootEntry[0],
                new LootCondition[0],
                new RandomValueRange(1),
                new RandomValueRange(1),
                name);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTableLoad(LootTableLoadEvent event)
    {
        System.out.println(event);
        ResourceLocation resourceLocation = event.getName();
        LootTable lootTable = event.getTable();
        lootTableMap.put(resourceLocation, lootTable);
    }
}
