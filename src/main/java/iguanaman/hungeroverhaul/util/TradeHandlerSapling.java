package iguanaman.hungeroverhaul.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class TradeHandlerSapling implements IVillageTradeHandler {

	final Block block;


	protected TradeHandlerSapling(Block par1) {
		block = par1;
	}


	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.addToListWithCheck(new MerchantRecipe(new ItemStack(Items.emerald, 1), new ItemStack(block, 1, 0)));
	}

}