package iguanaman.hungeroverhaul.library;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RecipeRemover
{
    public static void removeShapedRecipes(List<ItemStack> removelist)
    {
        for (ItemStack stack : removelist)
        {
            removeShapedRecipe(stack);
        }
    }

    public static void removeAnyRecipe(ItemStack resultItem)
    {
        Set<Entry<ResourceLocation, IRecipe>> recipes = ForgeRegistries.RECIPES.getEntries();
        Set<ResourceLocation> removeRecipes = new HashSet<>();

        for (Entry<ResourceLocation, IRecipe> recipe : recipes)
        {
            ItemStack recipeResult = recipe.getValue().getRecipeOutput();

            if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
            {
                removeRecipes.add(recipe.getKey());
            }
        }

        for(ResourceLocation recipe : removeRecipes)
        {
            RegistryManager.ACTIVE.getRegistry(GameData.RECIPES).remove(recipe);
        }
    }

    public static void removeShapedRecipe(ItemStack resultItem)
    {
        Set<Entry<ResourceLocation, IRecipe>> recipes = ForgeRegistries.RECIPES.getEntries();
        Set<ResourceLocation> removeRecipes = new HashSet<>();

        for (Entry<ResourceLocation, IRecipe> recipe : recipes)
        {
            IRecipe tmpRecipe = recipe.getValue();

            if (tmpRecipe instanceof ShapedRecipes)
            {
                ShapedRecipes shaped_recipe = (ShapedRecipes) tmpRecipe;
                ItemStack recipeResult = shaped_recipe.getRecipeOutput();

                if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
                {
                    removeRecipes.add(recipe.getKey());
                }
            }
        }

        for(ResourceLocation recipe : removeRecipes)
        {
            RegistryManager.ACTIVE.getRegistry(GameData.RECIPES).remove(recipe);
        }
    }

    public static void removeShapelessRecipe(ItemStack resultItem)
    {
        Set<Entry<ResourceLocation, IRecipe>> recipes = ForgeRegistries.RECIPES.getEntries();
        Set<ResourceLocation> removeRecipes = new HashSet<>();

        for (Entry<ResourceLocation, IRecipe> recipe : recipes)
        {
            IRecipe tmpRecipe = recipe.getValue();

            if (tmpRecipe instanceof ShapelessRecipes)
            {
                ShapelessRecipes shapeless_recipe = (ShapelessRecipes) tmpRecipe;
                ItemStack recipeResult = shapeless_recipe.getRecipeOutput();

                if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
                {
                    removeRecipes.add(recipe.getKey());
                }
            }
        }

        for(ResourceLocation recipe : removeRecipes)
        {
            RegistryManager.ACTIVE.getRegistry(GameData.RECIPES).remove(recipe);
        }
    }

    public static void removeFurnaceRecipe(ItemStack resultItem)
    {
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();

        recipes.remove(resultItem);
    }

    public static void removeFurnaceRecipe(Item item, int metadata)
    {
        removeFurnaceRecipe(new ItemStack(item, 1, metadata));
    }

    public static void removeFurnaceRecipe(Item item)
    {
        removeFurnaceRecipe(new ItemStack(item, 1, 32767));
    }
}
