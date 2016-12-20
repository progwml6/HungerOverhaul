package iguanaman.hungeroverhaul.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;

public class RecipeRemover
{
    public static void removeAnyRecipe(ItemStack resultItem)
    {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for (int i = 0; i < recipes.size(); i++)
        {
            IRecipe tmpRecipe = recipes.get(i);
            ItemStack recipeResult = tmpRecipe.getRecipeOutput();
            if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
                recipes.remove(i--);
        }
    }

    public static void removeFurnaceRecipe(ItemStack furnaceInput)
    {
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
        Iterator<Map.Entry<ItemStack, ItemStack>> recipeIterator = recipes.entrySet().iterator();
        while (recipeIterator.hasNext())
        {
            Map.Entry<ItemStack, ItemStack> entry = recipeIterator.next();
            if (ItemStack.areItemStacksEqual(entry.getKey(), furnaceInput))
            {
                recipeIterator.remove();
            }
        }
    }

}
