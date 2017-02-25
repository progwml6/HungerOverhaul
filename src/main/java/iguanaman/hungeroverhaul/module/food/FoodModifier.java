package iguanaman.hungeroverhaul.module.food;

import java.util.HashMap;
import java.util.Map;

import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.library.ItemAndBlockList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;

public class FoodModifier
{
    private static HashMap<ItemStack, FoodValues> modifiedFoodValues = new HashMap<ItemStack, FoodValues>();

    public static ItemAndBlockList blacklist = new ItemAndBlockList();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void getModifiedFoodValues(FoodEvent.GetFoodValues event)
    {
        if (!Config.modifyFoodValues)
            return;

        if (blacklist.contains(event.food))
            return;

        FoodValues modifiedFoodValues = lookupModifiedFoodValues(event.food);
        if (modifiedFoodValues != null)
            event.foodValues = modifiedFoodValues;
        else
        {
            int hunger = Math.max(Math.round(event.foodValues.hunger / Config.foodHungerDivider), 1);
            float saturation = event.foodValues.saturationModifier;
            if (Config.foodHungerToSaturationDivider != 0)
            {
                saturation = hunger / Config.foodHungerToSaturationDivider;
            }
            saturation /= Config.foodSaturationDivider;
            event.foodValues = new FoodValues(hunger, saturation);
        }
    }

    public static void setModifiedFoodValues(Item item, FoodValues values)
    {
        setModifiedFoodValues(new ItemStack(item), values);
    }

    public static void setModifiedFoodValues(ItemStack stack, FoodValues values)
    {
        ItemStack foundMatchingFood = findMatchingFood(stack);
        if (foundMatchingFood != null)
        {
            modifiedFoodValues.remove(foundMatchingFood);
        }
        modifiedFoodValues.put(stack, values);
    }

    private static FoodValues lookupModifiedFoodValues(ItemStack stack)
    {
        ItemStack foundMatchingFood = findMatchingFood(stack);
        return foundMatchingFood != null ? modifiedFoodValues.get(foundMatchingFood) : null;
    }

    private static ItemStack findMatchingFood(ItemStack stack)
    {
        for (Map.Entry<ItemStack, FoodValues> entry : modifiedFoodValues.entrySet())
        {
            if (stack.isItemEqual(entry.getKey()))
            {
                return entry.getKey();
            }
        }
        return null;
    }
}
