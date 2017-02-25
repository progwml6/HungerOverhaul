package iguanaman.hungeroverhaul.module.seed;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import iguanaman.hungeroverhaul.HungerOverhaul;
import iguanaman.hungeroverhaul.common.config.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

public class GrassSeedsModule
{
    public static List<WeightedRandom.Item> hoeSeedList = new ArrayList<WeightedRandom.Item>();

    public static Class<?> SeedEntry = null;

    public static Constructor<?> SeedEntryConstructor = null;

    public static Field weightField = null;

    public static Field seedListField = null;

    public static Field seedField = null;

    public static ItemStack getSeedFromTillingGrass(Random rand)
    {
        if (Config.removeTallGrassSeeds)
        {
            try
            {
                WeightedRandom.Item entry = WeightedRandom.getRandomItem(rand, hoeSeedList);
                if (entry != null && seedField.get(entry) != null)
                    return ((ItemStack) seedField.get(entry)).copy();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        else
        {
            return ForgeHooks.getGrassSeed(rand, 0);
        }
    }

    @SuppressWarnings("unchecked")
    public static void postInit()
    {
        initReflection();

        List<WeightedRandom.Item> seedList = null;
        try
        {
            seedList = (List<WeightedRandom.Item>) seedListField.get(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        if (Config.allSeedsEqual)
        {
            try
            {
                for (WeightedRandom.Item entry : seedList)
                {
                    weightField.set(entry, 1);
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        if (Config.removeTallGrassSeeds)
        {
            HungerOverhaul.log.info("Removing tall grass seeds");
            for (WeightedRandom.Item seedEntry : seedList)
            {
                hoeSeedList.add(seedEntry);
            }
            seedList.clear();
            MinecraftForge.addGrassSeed(null, 10);
        }
    }

    private static void initReflection()
    {
        try
        {
            for (Class<?> declaredClass : ForgeHooks.class.getDeclaredClasses())
            {
                if (declaredClass.getSimpleName().equals("SeedEntry"))
                {
                    SeedEntry = declaredClass;
                    break;
                }
            }
            SeedEntryConstructor = SeedEntry.getConstructor(ItemStack.class, int.class);
            seedField = SeedEntry.getDeclaredField("seed");
            seedField.setAccessible(true);
            seedListField = ForgeHooks.class.getDeclaredField("seedList");
            seedListField.setAccessible(true);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        try
        {
            weightField = SeedEntry.getSuperclass().getDeclaredField("field_76292_a");
            weightField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            try
            {
                weightField = SeedEntry.getSuperclass().getDeclaredField("itemWeight");
                weightField.setAccessible(true);
            }
            catch (NoSuchFieldException e2)
            {
                throw new RuntimeException(e2);
            }
        }
    }
}
