package iguanaman.hungeroverhaul.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigOption<T>
{
    public String category;

    public String name;

    public T defaultValue;

    public T blankSlate;

    public String comment;

    public T minValue;

    public T maxValue;

    public ConfigOption(String category, String name, T defaultValue, T blankSlate, String comment)
    {
        this(category, name, defaultValue, null, null, blankSlate, comment);
    }

    public ConfigOption(String category, String name, T defaultValue, T minValue, T maxValue, T blankSlate, String comment)
    {
        this.category = category;
        this.name = name;
        this.defaultValue = defaultValue;
        this.blankSlate = blankSlate;
        this.comment = comment;
        this.minValue = minValue != null ? minValue : this.getDefaultMinValue();
        this.maxValue = maxValue != null ? maxValue : this.getDefaultMaxValue();
    }

    public String getComment()
    {
        String commentSuffix = "vanilla: " + this.blankSlate;
        return this.comment + " [" + commentSuffix + "]";
    }

    @SuppressWarnings("unchecked")
    private T getDefaultMinValue()
    {
        if (this.defaultValue instanceof Integer)
        {
            return (T) Integer.valueOf(Integer.MIN_VALUE);
        }
        else if (this.defaultValue instanceof Float)
        {
            return (T) Float.valueOf(-Float.MAX_VALUE);
        }
        else if (this.defaultValue instanceof Double)
        {
            return (T) Double.valueOf(-Double.MAX_VALUE);
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private T getDefaultMaxValue()
    {
        if (this.defaultValue instanceof Integer)
        {
            return (T) Integer.valueOf(Integer.MAX_VALUE);
        }
        else if (this.defaultValue instanceof Float)
        {
            return (T) Float.valueOf(Float.MAX_VALUE);
        }
        else if (this.defaultValue instanceof Double)
        {
            return (T) Double.valueOf(Double.MAX_VALUE);
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public T get(Configuration config)
    {
        if (this.defaultValue instanceof Boolean)
        {
            return (T) Boolean.valueOf(config.getBoolean(this.name, this.category, (Boolean) this.defaultValue, this.getComment()));
        }
        else if (this.defaultValue instanceof Integer)
        {
            return (T) Integer.valueOf(config.getInt(this.name, this.category, (Integer) this.defaultValue, (Integer) this.minValue, (Integer) this.maxValue, this.getComment()));
        }
        else if (this.defaultValue instanceof Float)
        {
            return (T) Float.valueOf(config.getFloat(this.name, this.category, (Float) this.defaultValue, (Float) this.minValue, (Float) this.maxValue, this.getComment()));
        }
        else if (this.defaultValue instanceof Double)
        {
            return (T) Double.valueOf(Math.min((Double) this.maxValue, Math.max((Double) this.minValue, this.getProperty(config).getDouble())));
        }
        else if (this.defaultValue instanceof String)
        {
            return (T) config.getString(this.name, this.category, (String) this.defaultValue, this.getComment());
        }
        else
        {
            throw new RuntimeException("Unknown ConfigOption type for '" + this.category + ":" + this.name + "': " + this.defaultValue.getClass().getName());
        }
    }

    public T getBackwardsCompatible(Configuration config, ConfigOption<T> legacyConfigOption)
    {
        if (!this.exists(config) && legacyConfigOption.exists(config))
        {
            T oldConfigOptionValue = legacyConfigOption.get(config);
            if (oldConfigOptionValue != null)
            {
                this.set(config, oldConfigOptionValue);
            }
        }
        return this.get(config);
    }

    public Property getProperty(Configuration config)
    {
        Property property = config.getCategory(this.category).get(this.name);
        if (property == null)
        {
            property = config.get(this.category, this.name, this.defaultValue.toString());
        }
        return property;
    }

    public boolean exists(Configuration config)
    {
        return config.hasCategory(this.category) && config.getCategory(this.category).containsKey(this.name);
    }

    public void set(Configuration config, T value)
    {
        Property property = this.getProperty(config);
        if (value instanceof Boolean)
        {
            property.set((Boolean) value);
        }
        else if (value instanceof Integer)
        {
            property.set((Integer) value);
        }
        else if (value instanceof Float)
        {
            property.set((Float) value);
        }
        else if (value instanceof Double)
        {
            property.set((Double) value);
        }
        else if (value instanceof String)
        {
            property.set((String) value);
        }
        else
        {
            throw new RuntimeException("Unknown ConfigOption type for '" + this.category + ":" + this.name + "': " + this.defaultValue.getClass().getName());
        }
    }

    public void setToBlankSlate(Configuration config)
    {
        this.get(config);
        this.set(config, this.blankSlate);
    }

    public void setToDefault(Configuration config)
    {
        this.get(config);
        this.set(config, this.defaultValue);
    }

    public void remove(Configuration config)
    {
        config.getCategory(this.category).remove(this.name);
    }
}
