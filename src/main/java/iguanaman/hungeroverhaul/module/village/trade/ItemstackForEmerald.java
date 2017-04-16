package iguanaman.hungeroverhaul.module.village.trade;

import java.util.Random;

import iguanaman.hungeroverhaul.library.Util;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class ItemstackForEmerald implements ITradeList
{
    public ItemStack sellingItem;

    public PriceInfo priceInfo;

    public ItemstackForEmerald(Item par1Item, PriceInfo priceInfo)
    {
        this.sellingItem = new ItemStack(par1Item);
        this.priceInfo = priceInfo;
    }

    public ItemstackForEmerald(ItemStack stack, PriceInfo priceInfo)
    {
        this.sellingItem = stack;
        this.priceInfo = priceInfo;
    }

    @Override
    public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
    {
        int i = 1;

        if (this.priceInfo != null)
        {
            i = this.priceInfo.getPrice(random);
        }

        ItemStack itemstack;
        ItemStack itemstack1;

        if (i < 0)
        {
            itemstack = new ItemStack(Items.EMERALD);
            itemstack1 = Util.copyStackWithAmount(this.sellingItem, -i);
        }
        else
        {
            itemstack = new ItemStack(Items.EMERALD, i, 0);
            itemstack1 = Util.copyStackWithAmount(this.sellingItem, 1);
        }

        recipeList.add(new MerchantRecipe(itemstack, itemstack1));
    }
}
