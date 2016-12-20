package iguanaman.hungeroverhaul.json;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;
import squeek.applecore.api.food.FoodValues;

/**
 * Class for serializing Foods to/from json based on format from Jaded's Blood mod by Minalien
 * https://github.com/Minalien/JadedsBlood
 */
public class Food
{
    //Registry Name
    public ResourceLocation name;

    public String oreName = "";

    public int meta = 0;

    public int count = 1;

    public float saturationModifier = 0.0F;

    public int hunger = 0;

    public boolean hasOredictEntry()
    {
        return oreName.isEmpty();
    }

    //TODO handle blocks as well!!
    @SuppressWarnings("deprecation")
    public ItemStack toItemStack()
    {
        Item i = GameData.getItemRegistry().getObject(name);
        if (i == null)
        {
            Block b = GameData.getBlockRegistry().getObject(name);
            if (b != null)
                i = Item.getItemFromBlock(b);
        }
        return i == null ? null : new ItemStack(GameData.getItemRegistry().getObject(name), count, meta);
    }

    public FoodValues toFoodValues()
    {
        return new FoodValues(hunger, saturationModifier);
    }

    public static Food fromItemStack(ItemStack is, FoodValues fv)
    {
        return fromItemStack(is, fv.saturationModifier, fv.hunger);
    }

    @SuppressWarnings("deprecation")
    public static Food fromItemStack(ItemStack is, Float saturationModifier, int hunger)
    {
        Food fd = new Food();
        fd.name = GameData.getItemRegistry().getNameForObject(is.getItem());
        if (fd.name == null || fd.name.getResourcePath().isEmpty())
            fd.name = GameData.getBlockRegistry().getNameForObject(Block.getBlockFromItem(is.getItem()));
        if (fd.name == null || fd.name.getResourcePath().isEmpty())
            return null;
        fd.meta = is.getItemDamage();
        fd.count = is.stackSize;
        int[] oreIds = OreDictionary.getOreIDs(is);
        if (oreIds.length > 0)
            fd.oreName = OreDictionary.getOreName(oreIds[0]);
        fd.saturationModifier = saturationModifier;
        fd.hunger = hunger;
        return fd;
    }
}
