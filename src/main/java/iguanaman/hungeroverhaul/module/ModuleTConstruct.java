package iguanaman.hungeroverhaul.module;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerCommons;

public class ModuleTConstruct
{
    public static void init()
    {
        //TODO: FIX!
        /*if (!PHConstruct.enableHealthRegen)
        {
            PHConstruct.enableHealthRegen = true;
            IguanaConfig.healthRegenRatePercentage = 0;
        }
        if (PHConstruct.alphaRegen)
        {
            PHConstruct.alphaRegen = false;
            IguanaConfig.foodRegensHealth = true;
        }*/
        if (IguanaConfig.dryingRackTimeMultiplier != 1)
        {
            //TinkerRegistry.dryingRegistry.clear(); //TODO: Talk to boni

            int dryingTime = 20 * 60 * 20; //in minutes

            TinkerRegistry.registerDryingRecipe(Items.BEEF, TinkerCommons.jerkyBeef, dryingTime);
            TinkerRegistry.registerDryingRecipe(Items.CHICKEN, TinkerCommons.jerkyChicken, dryingTime);
            TinkerRegistry.registerDryingRecipe(Items.PORKCHOP, TinkerCommons.jerkyPork, dryingTime);
            //TODO there seems to be new jerky in 1.10

            TinkerRegistry.registerDryingRecipe(new ItemStack(Items.FISH, 1, 0), TinkerCommons.jerkyFish, dryingTime);
            TinkerRegistry.registerDryingRecipe(new ItemStack(Items.FISH, 1, 1), TinkerCommons.jerkySalmon, dryingTime);
            TinkerRegistry.registerDryingRecipe(new ItemStack(Items.FISH, 1, 2), TinkerCommons.jerkyClownfish, dryingTime);
            TinkerRegistry.registerDryingRecipe(new ItemStack(Items.FISH, 1, 3), TinkerCommons.jerkyPufferfish, dryingTime);

            TinkerRegistry.registerDryingRecipe(Items.ROTTEN_FLESH, TinkerCommons.jerkyMonster, dryingTime);
        }
    }
}
