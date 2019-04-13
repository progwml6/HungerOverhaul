package iguanaman.hungeroverhaul.module.hunger;

import iguanaman.hungeroverhaul.common.config.Config;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import squeek.applecore.api.AppleCoreAPI;

public class RespawnHungerModule
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if (!Config.enableRespawnHunger)
            return;

        int respawnHunger = Config.respawnHungerValue;

        if (Config.difficultyScalingRespawnHunger && event.player.world.getDifficulty().getDifficultyId() > EnumDifficulty.EASY.getDifficultyId())
        {
            respawnHunger -= (event.player.world.getDifficulty().getDifficultyId() - 1) * Config.respawnHungerDifficultyModifier;
        }

        AppleCoreAPI.mutator.setHunger(event.player, Math.min(Math.max(respawnHunger, 1), 20));
        if (event.player.getFoodStats().getSaturationLevel() > event.player.getFoodStats().getFoodLevel())
        {
            AppleCoreAPI.mutator.setSaturation(event.player, event.player.getFoodStats().getFoodLevel());
        }
    }
}
