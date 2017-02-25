package iguanaman.hungeroverhaul.module.vanilla.potion;

import iguanaman.hungeroverhaul.library.Util;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionWellFed extends Potion
{
    public PotionWellFed()
    {
        super(false, 0);

        ResourceLocation resource = Util.getResource("wellfed");

        this.setIconIndex(7, 0);
        this.setPotionName("potion." + resource.getResourcePath());

        REGISTRY.register(-1, resource, this);
    }
}
