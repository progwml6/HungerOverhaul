package iguanaman.hungeroverhaul.module.village.trade;

import iguanaman.hungeroverhaul.library.Util;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import javax.annotation.Nonnull;
import java.util.Random;

public class EmeraldForItemstack implements ITradeList
{
    public ItemStack buyingItem;

    public PriceInfo buyAmounts;

    public EmeraldForItemstack(@Nonnull ItemStack item, @Nonnull PriceInfo buyAmounts)
    {
        this.buyingItem = item;
        this.buyAmounts = buyAmounts;
    }

    @Override
    public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
    {
        recipeList.add(new MerchantRecipe(Util.copyStackWithAmount(this.buyingItem, this.buyAmounts.getPrice(random)), Items.EMERALD));
    }
}
