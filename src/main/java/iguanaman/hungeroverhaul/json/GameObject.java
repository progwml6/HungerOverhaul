package iguanaman.hungeroverhaul.json;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;

public class GameObject
{
    public String name = "";

    public int meta = OreDictionary.WILDCARD_VALUE;

    public Item toItem()
    {
        return GameData.getItemRegistry().getObject(new ResourceLocation(name));
    }

    public Block toBlock()
    {
        return GameData.getBlockRegistry().getObject(new ResourceLocation(name));
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
