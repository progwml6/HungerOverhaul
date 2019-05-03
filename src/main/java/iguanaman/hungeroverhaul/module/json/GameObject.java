package iguanaman.hungeroverhaul.module.json;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class GameObject
{
    public String name;

    public int meta = OreDictionary.WILDCARD_VALUE;

    public Item toItem()
    {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation((this.name)));
    }

    public Block toBlock()
    {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.name));
    }

    public ItemStack toItemStack()
    {
        Item item = this.toItem();

        if (item != Items.AIR)
        {
            return new ItemStack(item, 1, this.meta);
        }

        Block block = this.toBlock();

        if (block != null)
        {
            return new ItemStack(block, 1, this.meta);
        }

        return ItemStack.EMPTY;
    }
}
