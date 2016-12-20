package iguanaman.hungeroverhaul.json;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class GameObject
{
    public String name = "";

    public int meta = OreDictionary.WILDCARD_VALUE;

    public Item toItem()
    {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
    }

    public Block toBlock()
    {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
    }

    public ItemStack toItemStack()
    {
        Item item = toItem();

        if (item != null)
            return new ItemStack(item, 1, meta);

        Block block = toBlock();

        if (block != null)
            return new ItemStack(block, 1, meta);

        return null;
    }
}
