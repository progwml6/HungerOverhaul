package iguanaman.hungeroverhaul.module;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import squeek.applecore.api.AppleCoreAPI;

public class ModuleRespawnHunger
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        int respawnHunger = IguanaConfig.respawnHungerValue;

        if (IguanaConfig.difficultyScalingRespawnHunger && event.player.world.getDifficulty().getDifficultyId() > EnumDifficulty.EASY.getDifficultyId())
            respawnHunger -= (event.player.world.getDifficulty().getDifficultyId() - 1) * IguanaConfig.respawnHungerDifficultyModifier;

        AppleCoreAPI.mutator.setHunger(event.player, Math.min(Math.max(respawnHunger, 1), 20));
        if (event.player.getFoodStats().getSaturationLevel() > event.player.getFoodStats().getFoodLevel())
            AppleCoreAPI.mutator.setSaturation(event.player, event.player.getFoodStats().getFoodLevel());
    }
}
