package iguanaman.hungeroverhaul.module.json;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
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
    public String name;

    public String oreName;

    public int meta = 0;

    public int count = 1;

    public float saturationModifier = 0.0F;

    public int hunger = 0;

    public boolean hasOredictEntry()
    {
        return this.oreName.isEmpty();
    }

    //TODO handle blocks as well!!
    @SuppressWarnings("deprecation")
    public ItemStack toItemStack()
    {
        ResourceLocation loc = new ResourceLocation(this.name);

        Item item = GameData.getItemRegistry().getObject(loc);

        if (item == Items.AIR)
        {
            Block block = GameData.getBlockRegistry().getObject(loc);

            if (block != null)
            {
                item = Item.getItemFromBlock(block);
            }
        }

        return item == null ? null : new ItemStack(GameData.getItemRegistry().getObject(loc), this.count, this.meta);
    }

    public FoodValues toFoodValues()
    {
        return new FoodValues(this.hunger, this.saturationModifier);
    }

    public static Food fromItemStack(ItemStack is, FoodValues fv)
    {
        return fromItemStack(is, fv.saturationModifier, fv.hunger);
    }

    @SuppressWarnings("deprecation")
    public static Food fromItemStack(ItemStack is, Float saturationModifier, int hunger)
    {
        Food fd = new Food();

        fd.name = GameData.getItemRegistry().getNameForObject(is.getItem()).toString();

        if (fd.name == null || fd.name.toString().isEmpty())
        {
            fd.name = GameData.getBlockRegistry().getNameForObject(Block.getBlockFromItem(is.getItem())).toString();
        }

        if (fd.name == null || fd.name.toString().isEmpty())
        {
            return null;
        }

        fd.meta = is.getItemDamage();
        fd.count = is.getCount();

        int[] oreIds = OreDictionary.getOreIDs(is);

        if (oreIds.length > 0)
        {
            fd.oreName = OreDictionary.getOreName(oreIds[0]);
        }

        fd.saturationModifier = saturationModifier;
        fd.hunger = hunger;

        return fd;
    }
}
