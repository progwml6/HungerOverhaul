package iguanaman.hungeroverhaul.util;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class TradeHandlerCrop implements IVillageTradeHandler {

	final Item item;


	protected TradeHandlerCrop(Item par1) {
		item = par1;
	}


	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.addToListWithCheck(new MerchantRecipe(new ItemStack(item, 16, 0), new ItemStack(Items.emerald, 1)));
	}

}