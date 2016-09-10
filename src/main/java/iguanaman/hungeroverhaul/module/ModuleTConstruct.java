package iguanaman.hungeroverhaul.module;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.util.config.PHConstruct;

public class ModuleTConstruct {
    public static void init () {
        if (!PHConstruct.enableHealthRegen) {
            PHConstruct.enableHealthRegen = true;
            IguanaConfig.healthRegenRatePercentage = 0;
        }
        if (PHConstruct.alphaRegen) {
            PHConstruct.alphaRegen = false;
            IguanaConfig.foodRegensHealth = true;
        }
        if (IguanaConfig.dryingRackTimeMultiplier != 1) {
            DryingRackRecipes.recipes.clear();

            int dryingTime = 20 * 60 * 20; //in minutes

            TinkerRegistry.registerDryingRecipe(Items.BEEF, TinkerCommons.jerkyBeef, dryingTime);
            TinkerRegistry.registerDryingRecipe(Items.CHICKEN, TinkerCommons.jerkyChicken, dryingTime);
            TinkerRegistry.registerDryingRecipe(Items.PORKCHOP, TinkerCommons.jerkyPork, dryingTime);
            //TODO there seems to be new jerky in 1.10
            TinkerRegistry.registerDryingRecipe(Items.FISH, dryingTime, new ItemStack(TinkerArmor.jerky, 1, 4));
            TinkerRegistry.registerDryingRecipe(Items.ROTTEN_FLESH, TinkerCommons.jerkyMonster, dryingTime);
        }
    }
}
