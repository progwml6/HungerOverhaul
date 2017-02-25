package iguanaman.hungeroverhaul.module.json;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;

public class GameObject
{
    public ResourceLocation name;

    public int meta = OreDictionary.WILDCARD_VALUE;

    @SuppressWarnings("deprecation")
    public Item toItem()
    {
        return GameData.getItemRegistry().getObject(this.name);
    }

    @SuppressWarnings("deprecation")
    public Block toBlock()
    {
        return GameData.getBlockRegistry().getObject(this.name);
    }

    public ItemStack toItemStack()
    {
        Item item = this.toItem();

        if (item != null)
        {
            return new ItemStack(item, 1, this.meta);
        }

        Block block = this.toBlock();

        if (block != null)
        {
            return new ItemStack(block, 1, this.meta);
        }

        return null;
    }
}
