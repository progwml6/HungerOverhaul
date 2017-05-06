package iguanaman.hungeroverhaul.module.commands.sub;

import iguanaman.hungeroverhaul.HungerOverhaul;
import iguanaman.hungeroverhaul.module.json.JsonModule;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class ReloadJson implements ISubCommand
{
    @Override
    public String getHelp(MinecraftServer srv)
    {
        return "commands.hungeroverhaul.ReloadJson";
    }

    @Override
    public void call(MinecraftServer srv, String[] args, ICommandSender sender)
    {
        JsonModule.preinit(HungerOverhaul.configPath);

        JsonModule.init();

        sender.addChatMessage(new TextComponentTranslation("commands.hungeroverhaul.JsonReloaded"));
    }
}
