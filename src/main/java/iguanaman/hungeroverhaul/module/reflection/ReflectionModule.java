package iguanaman.hungeroverhaul.module.reflection;

import java.lang.reflect.Field;

import com.pam.harvestcraft.blocks.growables.BlockPamCrop;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

//TODO: REMOVE REFLECTION HELPER IN 1.13
public class ReflectionModule
{
    //@formatter:off
    public static PropertyInteger pamCropAge = null;

    public static boolean pamCropAgeFound = false;
    //@formatter:on

    public static void postInit()
    {
        if (Loader.isModLoaded("harvestcraft"))
        {
            Field pamCropAgeField = ReflectionHelper.findField(BlockPamCrop.class, "AGE");
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
        }
    }
}
