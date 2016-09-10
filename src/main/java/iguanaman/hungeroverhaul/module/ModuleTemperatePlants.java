package iguanaman.hungeroverhaul.module;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import com.pam.temperateplants.temperateplants;

import net.minecraftforge.fml.common.Loader;

public class ModuleTemperatePlants {
	public static void init()
	{
		// Add Thaumcraft aspects
		if (Loader.isModLoaded("Thaumcraft"))
			if (!ThaumcraftApi.exists(Item.getItemFromBlock(temperateplants.pamtemperatePlant), -1))
			{
				AspectList flowerAspects = new AspectList().add(Aspect.PLANT, 1);
				ThaumcraftApi.registerObjectTag(new ItemStack(temperateplants.pamtemperatePlant), new int[] {-1}, flowerAspects);
			}
	}
}
