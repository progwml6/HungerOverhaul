package iguanaman.hungeroverhaul.module.village;

import com.pam.harvestcraft.blocks.growables.BlockPamSapling;
import com.pam.harvestcraft.item.items.ItemPamSeedFood;

import iguanaman.hungeroverhaul.HungerOverhaul;
import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.module.village.generation.VillageCustomField;
import iguanaman.hungeroverhaul.module.village.trade.EmeraldForItemstack;
import iguanaman.hungeroverhaul.module.village.trade.ItemstackForEmerald;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import squeek.applecore.api.food.FoodValues;

public class VillageModule
{
    public static void init()
    {
        if (Config.addCustomVillageField && Config.fieldNormalWeight + Config.fieldReedWeight + Config.fieldStemWeight > 0)
        {
            VillagerRegistry.instance().registerVillageCreationHandler(new VillageCustomField.VillageManager());
            MapGenStructureIO.registerStructureComponent(VillageCustomField.class, HungerOverhaul.modID + ":CustomField");
        }
    }

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
                        if (Config.addTradesButcher)
                        {
                            addButcherTrade(item, stack, values);
                        }
                    }
                    if (Loader.isModLoaded("harvestcraft") && Config.addTradesFarmer && item instanceof ItemPamSeedFood)
                    {
                        addCropTrade(item);
                    }
                }
                else if (Loader.isModLoaded("harvestcraft") && Config.addSaplingTradesFarmer && item instanceof ItemBlock)
                {
                    Block block = Block.getBlockFromItem(item);

                    if (block instanceof BlockPamSapling)
                    {
                        addSaplingTrade(block);
                    }
                }
            }
        }
    }

    public static void addButcherTrade(Item item, ItemStack stack, FoodValues values)
    {
        VillagerProfession butcher = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:butcher"));
        VillagerCareer butcher_career = butcher.getCareer(0);

        butcher_career.addTrade(1, new ItemstackForEmerald(new ItemStack(item, item.getItemStackLimit(stack), 0), null));
    }

    public static void addSaplingTrade(Block block)
    {
        VillagerProfession farmer = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:farmer"));
        VillagerCareer farmer_career = farmer.getCareer(0);

        farmer_career.addTrade(1, new ItemstackForEmerald(new ItemStack(block, 1, 0), null));
    }

    public static void addCropTrade(Item item)
    {
        VillagerProfession farmer = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:farmer"));
        VillagerCareer farmer_career = farmer.getCareer(0);

        farmer_career.addTrade(1, new EmeraldForItemstack(new ItemStack(item, 1, 0), new PriceInfo(16, 16)));
    }
}
