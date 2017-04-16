package iguanaman.hungeroverhaul.library;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Util
{
    public static final String MODID = "hungeroverhaul";

    public static final String RESOURCE = MODID.toLowerCase(Locale.US);

    public static Logger getLogger(String type)
    {
        String log = MODID;

        return LogManager.getLogger(log + "-" + type);
    }

    /**
     * Returns the given Resource prefixed with tinkers resource location. Use this function instead of hardcoding resource locations.
     */
    public static String resource(String res)
    {
        return String.format("%s:%s", RESOURCE, res);
    }

    public static ResourceLocation getResource(String res)
    {
        return new ResourceLocation(RESOURCE, res);
    }

    /**
     * Prefixes the given unlocalized name with the Hunger Overhaul prefix. Use this when passing unlocalized names for a uniform namespace.
     */
    public static String prefix(String name)
    {
        return String.format("%s.%s", RESOURCE, name.toLowerCase(Locale.US));
    }

    public static ItemStack copyStackWithAmount(ItemStack stack, int amount)
    {
        if (stack.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        ItemStack s2 = stack.copy();

        s2.setCount(amount);

        return s2;
    }
}
