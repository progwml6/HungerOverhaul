package iguanaman.hungeroverhaul.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.fml.common.registry.GameData;

public class ItemAndBlockList
{
    private static LinkedHashSet<Class<?>> classes = new LinkedHashSet<Class<?>>();
    private static HashSet<Item> items = new HashSet<Item>();
    private static HashSet<Block> blocks = new HashSet<Block>();
    private static List<ItemStack> itemStacks = new ArrayList<ItemStack>();

    public void add(Class<?> clazz)
    {
        classes.add(clazz);
    }

    public void add(Item item)
    {
        items.add(item);
        add(new ItemStack(item));
    }

    public void add(Block block)
    {
        blocks.add(block);
        add(new ItemStack(block));
    }

    public void add(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() != null)
            itemStacks.add(itemStack);
    }

    public void add(String objectOrClassName) throws ClassNotFoundException
    {
        if (objectOrClassName.contains(":"))
        {
            Item item = GameData.getItemRegistry().getObject(objectOrClassName);
            Block block = GameData.getBlockRegistry().getObject(objectOrClassName);

            if (item != null)
                add(item);
            if (block != null)
                add(block);
        }
        else
        {
            Class<?> clazz = Class.forName(objectOrClassName);
            add(clazz);
        }
    }

    public boolean contains(Class<?> clazz)
    {
        if (classes.contains(clazz))
            return true;

        Iterator<Class<?>> itr = classes.iterator();
        while (itr.hasNext())
        {
            Class<?> testClass = itr.next();
            if (testClass.isAssignableFrom(clazz))
                return true;
        }

        return false;
    }

    public boolean contains(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() != null)
        {
            for (ItemStack curItemStack : itemStacks)
            {
                if (OreDictionary.itemMatches(curItemStack, itemStack, false))
                    return true;
            }
        }
        return false;
    }

    public boolean contains(Item item)
    {
        if (items.contains(item))
            return true;

        if (contains(new ItemStack(item)))
            return true;

        if (contains(item.getClass()))
            return true;

        return false;
    }

    public boolean contains(Block block)
    {
        if (blocks.contains(block))
            return true;

        if (contains(new ItemStack(block)))
            return true;

        if (contains(block.getClass()))
            return true;

        return false;
    }
}
