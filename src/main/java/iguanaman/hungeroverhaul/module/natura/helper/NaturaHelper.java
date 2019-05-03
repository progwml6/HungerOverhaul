package iguanaman.hungeroverhaul.module.natura.helper;

import com.progwml6.natura.overworld.NaturaOverworld;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;

public class NaturaHelper
{
    public static HashMap<Block, Item> cropToSeedMap = new HashMap<Block, Item>();

    public static void loadNatura()
    {
        if (Loader.isModLoaded("natura"))
        {
            cropToSeedMap.put(NaturaOverworld.barleyCrop, NaturaOverworld.overworldSeeds);
            cropToSeedMap.put(NaturaOverworld.cottonCrop, NaturaOverworld.overworldSeeds);
        }
    }
}
