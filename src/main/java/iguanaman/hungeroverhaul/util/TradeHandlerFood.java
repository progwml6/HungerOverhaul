package iguanaman.hungeroverhaul.util;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class TradeHandlerFood implements IVillageTradeHandler {

	final Item item;
	final int tradeValue;


	public TradeHandlerFood(Item par1, int par2) {
		item = par1;
		tradeValue = par2;
	}


	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.addToListWithCheck(new MerchantRecipe(new ItemStack(Items.emerald, 1), new ItemStack(item, tradeValue, 0)));

	}

}