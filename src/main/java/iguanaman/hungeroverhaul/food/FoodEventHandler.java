package iguanaman.hungeroverhaul.food;

import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import iguanaman.hungeroverhaul.HungerOverhaul;
import iguanaman.hungeroverhaul.config.IguanaConfig;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.HealthRegenEvent;
import squeek.applecore.api.hunger.StarvationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FoodEventHandler
{
    @SubscribeEvent
    public void onFoodEaten(FoodEvent.FoodEaten event)
    {
        if (!event.player.worldObj.isRemote && event.player.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && IguanaConfig.healthRegenRatePercentage > 0)
        {
            if (IguanaConfig.addWellFedEffect)
            {
                int duration = (int) Math.pow(event.foodValues.hunger * 100, 1.2);

                if (duration >= 30)
                {
                    PotionEffect currentEffect = event.player.getActivePotionEffect(HungerOverhaul.potionWellFed);
                    if (currentEffect != null)
                        duration += currentEffect.getDuration();
                    event.player.addPotionEffect(new PotionEffect(HungerOverhaul.potionWellFed.id, duration, 0, true));
                }
            }
        }

        if (IguanaConfig.foodRegensHealth)
        {
            float toHeal = Math.round((float) event.foodValues.hunger / (float) IguanaConfig.foodHealDivider);
            float canHeal = event.player.getMaxHealth() - event.player.getHealth();
            if (toHeal > canHeal)
                toHeal = canHeal;
            if (toHeal > 0f)
                event.player.heal(toHeal);
        }
    }

    @SubscribeEvent
    public void allowExhaustion(ExhaustionEvent.AllowExhaustion event)
    {
        if (IguanaConfig.hungerLossRatePercentage == 0)
        {
            AppleCoreAPI.mutator.setHunger(event.player, 19);
            AppleCoreAPI.mutator.setSaturation(event.player, 0f);
            AppleCoreAPI.mutator.setExhaustion(event.player, 0f);
            event.setResult(Result.DENY);
        }
    }
    
    @SubscribeEvent
    public void getMaxExhaustion(ExhaustionEvent.GetMaxExhaustion event)
    {
        EnumDifficulty difficulty = event.player.worldObj.difficultySetting;
        float hungerLossRate = event.maxExhaustionLevel / (IguanaConfig.hungerLossRatePercentage / 100F);
        if (IguanaConfig.difficultyScalingHunger)
        {
            if (difficulty == EnumDifficulty.PEACEFUL)
                hungerLossRate *= 5F/3F;
            else if (difficulty == EnumDifficulty.EASY)
                hungerLossRate *= 4F/3F;
        }
        event.maxExhaustionLevel = hungerLossRate;
    }

    @SubscribeEvent
    public void onExhausted(ExhaustionEvent.Exhausted event)
    {
        // decrease hunger in peaceful
        if (event.player.getFoodStats().getSaturationLevel() <= 0)
            event.deltaHunger = -1;
    }

    @SubscribeEvent
    public void allowHealthRegen(HealthRegenEvent.AllowRegen event)
    {
        if (event.player.getFoodStats().getFoodLevel() >= IguanaConfig.minHungerToHeal
                && IguanaConfig.healthRegenRatePercentage > 0
                && event.player.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration")
                && event.player.shouldHeal())
        {
            event.setResult(Result.ALLOW);
        }
        else
            event.setResult(Result.DENY);
    }

    @SubscribeEvent
    public void onHealthRegenTick(HealthRegenEvent.GetRegenTickPeriod event)
    {
        float wellfedModifier = 1.0F;
        if (event.player.isPotionActive(HungerOverhaul.potionWellFed))
            wellfedModifier = 0.75F;

        EnumDifficulty difficulty = event.player.worldObj.difficultySetting;
        float difficultyModifierHealing = 1.0F;
        if (IguanaConfig.difficultyScalingHealing)
        {
            if (difficulty.getDifficultyId() <= EnumDifficulty.EASY.getDifficultyId())
                difficultyModifierHealing = 0.75F;
            else if (difficulty == EnumDifficulty.HARD)
                difficultyModifierHealing = 1.5F;
        }

        float lowHealthModifier = 1.0F;
        if (IguanaConfig.modifyRegenRateOnLowHealth)
        {
            lowHealthModifier = event.player.getMaxHealth() - event.player.getHealth();
            lowHealthModifier *= IguanaConfig.lowHealthRegenRateModifier / 100F;
            lowHealthModifier *= difficultyModifierHealing;
            lowHealthModifier = (float) Math.pow(lowHealthModifier + 1F, 1.5F);
        }

        event.regenTickPeriod = Math.round(80.0F * difficultyModifierHealing * wellfedModifier * lowHealthModifier
                / (IguanaConfig.healthRegenRatePercentage / 100F));
    }

    @SubscribeEvent
    public void onHealthRegen(HealthRegenEvent.Regen event)
    {
        if (IguanaConfig.disableHealingHungerDrain)
            event.deltaExhaustion = 0;
    }

    @SubscribeEvent
    public void onStarve(StarvationEvent.Starve event)
    {
        event.starveDamage = IguanaConfig.damageOnStarve;
    }

    @SubscribeEvent
    public void onFoodStatsAddition(FoodEvent.FoodStatsAddition event)
    {
        if (IguanaConfig.hungerLossRatePercentage == 0)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onFoodStartEating(PlayerUseItemEvent.Start event)
    {
        if (IguanaConfig.modifyFoodEatingSpeed && AppleCoreAPI.accessor.isFood(event.item))
        {
            int hunger = FoodValues.get(event.item).hunger;
            if (hunger > 0)
                event.duration = hunger * 8 + 8;
        }
    }
}
