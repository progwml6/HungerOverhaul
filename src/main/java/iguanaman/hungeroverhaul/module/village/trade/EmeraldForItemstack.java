package iguanaman.hungeroverhaul.module.village.trade;

import java.util.Random;

import javax.annotation.Nonnull;

import iguanaman.hungeroverhaul.library.Util;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class EmeraldForItemstack implements EntityVillager.ITradeList
{
    public ItemStack buyingItem;

    public EntityVillager.PriceInfo buyAmounts;

    public EmeraldForItemstack(@Nonnull ItemStack item, @Nonnull EntityVillager.PriceInfo buyAmounts)
    {
        this.buyingItem = item;
        this.buyAmounts = buyAmounts;
    }

    @Override
    public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
    {
        recipeList.add(new MerchantRecipe(Util.copyStackWithAmount(this.buyingItem, this.buyAmounts.getPrice(random)), Items.EMERALD));
    }
}
