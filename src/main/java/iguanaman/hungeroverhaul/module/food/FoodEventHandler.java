package iguanaman.hungeroverhaul.module.food;

import iguanaman.hungeroverhaul.HungerOverhaul;
import iguanaman.hungeroverhaul.common.config.Config;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.HealthRegenEvent;
import squeek.applecore.api.hunger.StarvationEvent;

public class FoodEventHandler
{
    @SubscribeEvent
    public void onFoodEaten(FoodEvent.FoodEaten event)
    {
        if (!event.player.world.isRemote && event.player.world.getGameRules().getBoolean("naturalRegeneration") && Config.healthRegenRatePercentage > 0)
        {
            if (Config.addWellFedEffect)
            {
                int duration = (int) Math.pow(event.foodValues.hunger * 100, 1.2);

                if (duration >= 30)
                {
                    PotionEffect currentEffect = event.player.getActivePotionEffect(HungerOverhaul.potionWellFed);

                    if (currentEffect != null)
                    {
                        duration += currentEffect.getDuration();
                    }

                    event.player.addPotionEffect(new PotionEffect(HungerOverhaul.potionWellFed, duration, 0, true, true));
                }
            }
        }

        if (Config.foodRegensHealth)
        {
            float toHeal = Math.round(event.foodValues.hunger / (float) Config.foodHealDivider);
            float canHeal = event.player.getMaxHealth() - event.player.getHealth();

            if (toHeal > canHeal)
            {
                toHeal = canHeal;
            }

            if (toHeal > 0f)
            {
                event.player.heal(toHeal);
            }
        }
    }

    @SubscribeEvent
    public void allowExhaustion(ExhaustionEvent.AllowExhaustion event)
    {
        if (Config.hungerLossRatePercentage == 0)
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
        EnumDifficulty difficulty = event.player.world.getDifficulty();
        float hungerLossRate = event.maxExhaustionLevel / (Config.hungerLossRatePercentage / 100F);

        if (Config.difficultyScalingHunger)
        {
            if (difficulty == EnumDifficulty.PEACEFUL)
            {
                hungerLossRate *= 5F / 3F;
            }
            else if (difficulty == EnumDifficulty.EASY)
            {
                hungerLossRate *= 4F / 3F;
            }
        }

        event.maxExhaustionLevel = hungerLossRate;
    }

    @SubscribeEvent
    public void onExhausted(ExhaustionEvent.Exhausted event)
    {
        // decrease hunger in peaceful
        if (event.player.getFoodStats().getSaturationLevel() <= 0)
        {
            event.deltaHunger = -1;
        }
    }

    @SubscribeEvent
    public void allowHealthRegen(HealthRegenEvent.AllowRegen event)
    {
        if (event.player.getFoodStats().getFoodLevel() >= Config.minHungerToHeal && Config.healthRegenRatePercentage > 0 && event.player.world.getGameRules().getBoolean("naturalRegeneration") && event.player.shouldHeal())
        {
            event.setResult(Result.ALLOW);
        }
        else
        {
            event.setResult(Result.DENY);
        }
    }

    @SubscribeEvent
    public void onHealthRegenTick(HealthRegenEvent.GetRegenTickPeriod event)
    {
        float wellfedModifier = 1.0F;

        if (event.player.isPotionActive(HungerOverhaul.potionWellFed))
        {
            wellfedModifier = 0.75F;
        }

        EnumDifficulty difficulty = event.player.world.getDifficulty();
        float difficultyModifierHealing = 1.0F;

        if (Config.difficultyScalingHealing)
        {
            if (difficulty.getDifficultyId() <= EnumDifficulty.EASY.getDifficultyId())
            {
                difficultyModifierHealing = 0.75F;
            }
            else if (difficulty == EnumDifficulty.HARD)
            {
                difficultyModifierHealing = 1.5F;
            }
        }

        float lowHealthModifier = 1.0F;

        if (Config.modifyRegenRateOnLowHealth)
        {
            lowHealthModifier = event.player.getMaxHealth() - event.player.getHealth();
            lowHealthModifier *= Config.lowHealthRegenRateModifier / 100F;
            lowHealthModifier *= difficultyModifierHealing;
            lowHealthModifier = (float) Math.pow(lowHealthModifier + 1F, 1.5F);
        }

        event.regenTickPeriod = Math.round(80.0F * difficultyModifierHealing * wellfedModifier * lowHealthModifier / (Config.healthRegenRatePercentage / 100F));
    }

    @SubscribeEvent
    public void onHealthRegen(HealthRegenEvent.Regen event)
    {
        if (Config.disableHealingHungerDrain)
        {
            event.deltaExhaustion = 0;
        }
    }

    @SubscribeEvent
    public void onStarve(StarvationEvent.Starve event)
    {
        event.starveDamage = Config.damageOnStarve;
    }

    @SubscribeEvent
    public void onFoodStatsAddition(FoodEvent.FoodStatsAddition event)
    {
        if (Config.hungerLossRatePercentage == 0)
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onFoodStartEating(LivingEntityUseItemEvent.Start event)
    {
        if (Config.modifyFoodEatingSpeed && AppleCoreAPI.accessor.isFood(event.getItem()))
        {
            int hunger = FoodValues.get(event.getItem()).hunger;

            if (hunger > 0)
            {
                event.setDuration(hunger * 8 + 8);
            }
        }
    }
}
