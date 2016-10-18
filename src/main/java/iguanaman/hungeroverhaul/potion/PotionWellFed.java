package iguanaman.hungeroverhaul.potion;

import net.minecraft.potion.Potion;

public class PotionWellFed extends Potion
{
    public PotionWellFed()
    {
        super(false, 0);
        setIconIndex(7, 0);
        setPotionName("potion.wellfedPotion");
    }
}
