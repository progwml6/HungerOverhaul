package iguanaman.hungeroverhaul.module.json;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import iguanaman.hungeroverhaul.HungerOverhaul;
import iguanaman.hungeroverhaul.library.ItemAndBlockList;
import iguanaman.hungeroverhaul.module.event.HungerOverhaulEventHook;
import iguanaman.hungeroverhaul.module.food.FoodModifier;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import squeek.applecore.api.food.FoodValues;

public class JsonModule
{
    private static Gson GSON;

    private static List<File> hojsons = Lists.newArrayList();

    private static List<HOJsonData> hoData = Lists.newArrayList();

    public static void preinit(File configFolder)
    {
        GsonBuilder builder = new GsonBuilder();

        builder.enableComplexMapKeySerialization();
        builder.setPrettyPrinting();

        GSON = builder.create();

        File hoFolder = new File(configFolder, "HungerOverhaul");

        if (!hoFolder.exists())
        {
            hoFolder.mkdirs();
        }

        for (File potentialConfigFile : hoFolder.listFiles())
        {
            if (!FilenameUtils.getExtension(potentialConfigFile.getName()).equalsIgnoreCase("json"))
            {
                continue;
            }

            hojsons.add(potentialConfigFile);
        }
    }

    public static void init()
    {
        HungerOverhaul.log.info("Loading JSON Files");

        HOJsonData hod;

        for (File j : hojsons)
        {
            try
            {
                FileReader reader = new FileReader(j);
                hod = GSON.fromJson(reader, HOJsonData.class);
                reader.close();

                hoData.add(hod);
            }
            catch (Exception e)
            {
                HungerOverhaul.log.warn("Error Loading json files: ", e);
            }
        }

        HungerOverhaul.log.info("Loading data from json");

        for (HOJsonData h : hoData)
        {
            if (h == null)
            {
                continue;
            }

            if (h.foods != null)
            {
                for (Food f : h.foods)
                {
                    ItemStack itemStack = f.toItemStack();
                    FoodValues foodValues = f.toFoodValues();
                    if (itemStack != null && itemStack.getItem() != null && foodValues != null)
                    {
                        FoodModifier.setModifiedFoodValues(itemStack, foodValues);
                    }
                }
            }

            if (h.foodsBlacklist != null)
            {
                for (GameObject gameObj : h.foodsBlacklist)
                {
                    addGameObjectToList(FoodModifier.blacklist, gameObj);
                }
            }

            if (h.dropsBlacklist != null)
            {
                for (GameObject gameObj : h.dropsBlacklist)
                {
                    addGameObjectToList(HungerOverhaulEventHook.harvestDropsBlacklist, gameObj);
                }
            }

            if (h.harvestBlacklist != null)
            {
                for (GameObject gameObj : h.harvestBlacklist)
                {
                    addGameObjectToList(HungerOverhaulEventHook.rightClickHarvestBlacklist, gameObj);
                }
            }
        }

        HungerOverhaul.log.info("Loaded all data from JSON");
    }

    public static void addGameObjectToList(ItemAndBlockList list, GameObject gameObj)
    {
        if (gameObj.name == null && gameObj.name.toString().isEmpty())
        {
            return;
        }

        if (gameObj.meta == OreDictionary.WILDCARD_VALUE)
        {
            if (gameObj.toBlock() != null)
            {
                list.add(gameObj.toBlock());
            }
            if (gameObj.toItem() != Items.AIR)
            {
                list.add(gameObj.toItem());
            }
        }
        else
        {
            ItemStack itemStack = gameObj.toItemStack();

            if (!itemStack.isEmpty())
            {
                list.add(itemStack);
            }
        }
    }
}
