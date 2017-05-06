package iguanaman.hungeroverhaul.module.commands.sub;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface ISubCommand
{
    String getHelp(MinecraftServer srv);

    void call(MinecraftServer srv, String[] args, ICommandSender sender);
}
