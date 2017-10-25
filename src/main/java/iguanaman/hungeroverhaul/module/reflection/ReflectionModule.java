package iguanaman.hungeroverhaul.module.reflection;

import java.lang.reflect.Field;

import com.pam.harvestcraft.blocks.growables.BlockPamCrop;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamFruitLog;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

//TODO: REMOVE REFLECTION HELPER IN 1.13
public class ReflectionModule
{
    //@formatter:off
    public static PropertyInteger pamCropAge = null;
    public static PropertyInteger pamFruitAge = null;
    public static PropertyInteger pamFruitLogAge = null;

    public static boolean pamCropAgeFound = false;
    public static boolean pamFruitAgeFound = false;
    public static boolean pamFruitLogAgeFound = false;
    //@formatter:on

    public static void postInit()
    {
        if (Loader.isModLoaded("harvestcraft"))
        {
            Field pamCropAgeField = ReflectionHelper.findField(BlockPamCrop.class, "AGE");
            pamCropAgeField.setAccessible(true);

            Field pamFruitAgeField = ReflectionHelper.findField(BlockPamFruit.class, "AGE");
            pamCropAgeField.setAccessible(true);

            Field pamFruitLogAgeField = ReflectionHelper.findField(BlockPamFruitLog.class, "AGE");
            pamCropAgeField.setAccessible(true);

            try
            {
                pamCropAge = (PropertyInteger) pamCropAgeField.get(null);
                pamCropAgeFound = pamCropAge != null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                pamFruitAge = (PropertyInteger) pamFruitAgeField.get(null);
                pamFruitAgeFound = pamFruitAge != null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                pamFruitLogAge = (PropertyInteger) pamFruitLogAgeField.get(null);
                pamFruitLogAgeFound = pamFruitLogAge != null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
