package iguanaman.hungeroverhaul.common.config;

import net.minecraftforge.common.config.Configuration;

public class ConfigCategory
{
    String name;

    String comment;

    public ConfigCategory(String name, String comment)
    {
        this.name = name;
        this.comment = comment;
    }

    public net.minecraftforge.common.config.ConfigCategory get(Configuration config)
    {
        return config.getCategory(this.name);
    }

    public void create(Configuration config)
    {
        net.minecraftforge.common.config.ConfigCategory category = this.get(config);
        category.setComment(this.comment);
    }

    public void remove(Configuration config)
    {
        if (this.get(config) != null)
        {
            config.removeCategory(this.get(config));
        }
    }
}
