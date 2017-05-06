package iguanaman.hungeroverhaul.module.commands;

import com.google.common.base.Joiner;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public final class HOCommand extends CommandBase
{
    private final MinecraftServer server;

    public HOCommand(final MinecraftServer server)
    {
        this.server = server;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "hungeroverhaul";
    }

    @Override
    public String getUsage(final ICommandSender icommandsender)
    {
        return "commands.hungeroverhaul.usage";
    }

    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException
    {
        if (args.length == 0)
        {
            throw new WrongUsageException("commands.hungeroverhaul.usage");
        }
        else if ("help".equals(args[0]))
        {
            try
            {
                if (args.length > 1)
                {
                    final Commands c = Commands.valueOf(args[1]);

                    throw new WrongUsageException(c.command.getHelp(this.server));
                }
            }
            catch (final WrongUsageException wrong)
            {
                throw wrong;
            }
            catch (final Throwable er)
            {
                throw new WrongUsageException("commands.hungeroverhaul.usage");
            }
        }
        else if ("list".equals(args[0]))
        {
            throw new WrongUsageException(Joiner.on(", ").join(Commands.values()));
        }
        else
        {
            try
            {
                final Commands c = Commands.valueOf(args[0]);

                if (sender.canUseCommand(c.level, this.getName()))
                {
                    c.command.call(this.server, args, sender);
                }
                else
                {
                    throw new WrongUsageException("commands.hungeroverhaul.permissions");
                }
            }
            catch (final WrongUsageException wrong)
            {
                throw wrong;
            }
            catch (final Throwable er)
            {
                throw new WrongUsageException("commands.hungeroverhaul.usage");
            }
        }
    }
}
