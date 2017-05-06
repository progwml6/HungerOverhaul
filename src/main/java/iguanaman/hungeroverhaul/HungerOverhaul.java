package iguanaman.hungeroverhaul;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.library.Util;
import iguanaman.hungeroverhaul.module.biomesoplenty.BiomesOPlentyModule;
import iguanaman.hungeroverhaul.module.bonemeal.BonemealModule;
import iguanaman.hungeroverhaul.module.commands.HOCommand;
import iguanaman.hungeroverhaul.module.event.HungerOverhaulEventHook;
import iguanaman.hungeroverhaul.module.event.IMCHandler;
import iguanaman.hungeroverhaul.module.food.FoodEventHandler;
import iguanaman.hungeroverhaul.module.food.FoodModifier;
import iguanaman.hungeroverhaul.module.growth.PlantGrowthModule;
import iguanaman.hungeroverhaul.module.harvestcraft.HarvestCraftModule;
import iguanaman.hungeroverhaul.module.harvestcraft.helper.PamsModsHelper;
import iguanaman.hungeroverhaul.module.hunger.RespawnHungerModule;
import iguanaman.hungeroverhaul.module.json.JsonModule;
import iguanaman.hungeroverhaul.module.loot.LootModule;
import iguanaman.hungeroverhaul.module.natura.NaturaModule;
import iguanaman.hungeroverhaul.module.natura.helper.NaturaHelper;
import iguanaman.hungeroverhaul.module.reflection.ReflectionModule;
import iguanaman.hungeroverhaul.module.tinkersconstruct.TinkersConstructModule;
import iguanaman.hungeroverhaul.module.tweak.TweaksModule;
import iguanaman.hungeroverhaul.module.vanilla.VanillaModule;
import iguanaman.hungeroverhaul.module.vanilla.potion.PotionWellFed;
import iguanaman.hungeroverhaul.module.village.VillageModule;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = HungerOverhaul.modID, name = HungerOverhaul.modName, version = HungerOverhaul.modVersion, dependencies = "required-after:Forge@[12.18.0.1993,);required-after:AppleCore;after:tconstruct;after:harvestcraft;after:natura@[${natura_version},);after:ic2;after:*", acceptedMinecraftVersions = "[1.10.2, 1.11)")
public class HungerOverhaul
{
    public static final String modID = Util.MODID;

    public static final String modVersion = "${version}";

    public static final String modName = "Hunger Overhaul";

    public static final Logger log = LogManager.getLogger(modID);

    /* Instance of this mod, used for grabbing prototype fields */
    @Instance(modID)
    public static HungerOverhaul instance;

    public static Potion potionWellFed;

    public static File configPath;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Config.load(event);

        configPath = event.getModConfigurationDirectory();

        JsonModule.preinit(configPath);

        potionWellFed = new PotionWellFed();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new FoodEventHandler());
        MinecraftForge.EVENT_BUS.register(new FoodModifier());
        MinecraftForge.EVENT_BUS.register(new PlantGrowthModule());
        MinecraftForge.EVENT_BUS.register(new BonemealModule());

        VanillaModule.init();

        if (Loader.isModLoaded("harvestcraft"))
        {
            PamsModsHelper.loadHC();
            HarvestCraftModule.init();
        }

        if (Loader.isModLoaded("tconstruct"))
        {
            TinkersConstructModule.init();
        }

        if (Loader.isModLoaded("natura"))
        {
            NaturaHelper.loadNatura();
            NaturaModule.init();
        }

        if (Loader.isModLoaded("biomesoplenty"))
        {
            BiomesOPlentyModule.init();
        }

        JsonModule.init();

        VillageModule.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        //if (Config.removeTallGrassSeeds || Config.allSeedsEqual)
        //{
        //    GrassSeedsModule.postInit();
        //}

        VanillaModule.postInit();
        ReflectionModule.postInit();
        TweaksModule.postInit();
        VillageModule.postInit();
        LootModule.postInit();

        MinecraftForge.EVENT_BUS.register(new HungerOverhaulEventHook());
        MinecraftForge.EVENT_BUS.register(new RespawnHungerModule());
        MinecraftForge.EVENT_BUS.register(new LootModule());
    }

    @EventHandler
    private void serverStarting(final FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new HOCommand(evt.getServer()));
    }

    @EventHandler
    public void handleIMCMessages(IMCEvent event)
    {
        IMCHandler.processMessages(event.getMessages());
    }
}
