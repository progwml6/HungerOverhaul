package iguanaman.hungeroverhaul.module.tweak;

import iguanaman.hungeroverhaul.common.config.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import squeek.applecore.api.food.FoodValues;

public class TweaksModule
{
    public static void postInit()
    {
        boolean worthLooping = Config.modifyFoodStackSize || Config.addTradesButcher || Config.addHarvestCraftChestLoot || Config.addTradesFarmer || Config.addSaplingTradesFarmer;

        if (worthLooping)
        {
            for (Object obj : Item.REGISTRY)
            {
                Item item = (Item) obj;
                if (item instanceof ItemFood)
                {
                    ItemStack stack = new ItemStack(item);
                    FoodValues values = FoodValues.get(stack);

                    if (Config.modifyFoodStackSize)
                    {
                        modifyStackSize(item, stack, values);
                    }
                }
            }
        }
    }

    public static void modifyStackSize(Item item, ItemStack stack, FoodValues values)
    {
        int curStackSize = item.getItemStackLimit(stack);
        int newStackSize = curStackSize;

        if (values.hunger <= 2)
        {
            newStackSize = 16 * Config.foodStackSizeMultiplier;
        }
        else if (values.hunger <= 5)
        {
            newStackSize = 8 * Config.foodStackSizeMultiplier;
        }
        else if (values.hunger <= 8)
        {
            newStackSize = 4 * Config.foodStackSizeMultiplier;
        }
        else if (values.hunger <= 11)
        {
            newStackSize = 2 * Config.foodStackSizeMultiplier;
        }
        else
        {
            newStackSize = Config.foodStackSizeMultiplier;
        }

        if (curStackSize > newStackSize)
        {
            item.setMaxStackSize(newStackSize);
        }
    }
}
