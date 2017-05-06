package iguanaman.hungeroverhaul.module.commands;

import iguanaman.hungeroverhaul.module.commands.sub.ISubCommand;
import iguanaman.hungeroverhaul.module.commands.sub.ReloadJson;

public enum Commands
{
    ReloadJson(4, new ReloadJson());

    public final int level;

    public final ISubCommand command;

    Commands(final int level, final ISubCommand w)
    {
        this.level = level;
        this.command = w;
    }

    @Override
    public String toString()
    {
        return this.name();
    }
}
