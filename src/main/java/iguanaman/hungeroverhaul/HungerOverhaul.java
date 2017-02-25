package iguanaman.hungeroverhaul;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.library.Util;
import iguanaman.hungeroverhaul.module.biomesoplenty.BiomesOPlentyModule;
import iguanaman.hungeroverhaul.module.bonemeal.BonemealModule;
import iguanaman.hungeroverhaul.module.event.IMCHandler;
import iguanaman.hungeroverhaul.module.event.IguanaEventHook;
import iguanaman.hungeroverhaul.module.food.FoodEventHandler;
import iguanaman.hungeroverhaul.module.food.FoodModifier;
import iguanaman.hungeroverhaul.module.growth.PlantGrowthModule;
import iguanaman.hungeroverhaul.module.harvestcraft.HarvestCraftModule;
import iguanaman.hungeroverhaul.module.harvestcraft.helper.PamsModsHelper;
import iguanaman.hungeroverhaul.module.hunger.RespawnHungerModule;
import iguanaman.hungeroverhaul.module.json.JsonModule;
import iguanaman.hungeroverhaul.module.loot.LootModule;
import iguanaman.hungeroverhaul.module.natura.NaturaModule;
import iguanaman.hungeroverhaul.module.seed.GrassSeedsModule;
import iguanaman.hungeroverhaul.module.tinkersconstruct.TinkersConstructModule;
import iguanaman.hungeroverhaul.module.tweak.TweaksModule;
import iguanaman.hungeroverhaul.module.vanilla.VanillaModule;
import iguanaman.hungeroverhaul.module.vanilla.potion.PotionWellFed;
import iguanaman.hungeroverhaul.module.village.VillageModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
import slimeknights.mantle.util.RecipeRemover;

@Mod(modid = HungerOverhaul.modID, name = HungerOverhaul.modName, version = HungerOverhaul.modVersion, dependencies = "required-after:Forge@[12.18.0.1993,);required-after:AppleCore;after:TConstruct;after:harvestcraft;after:temperateplants;after:randomplants;after:weeeflowers;after:Natura;after:IC2;after:*")
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

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Config.load(event);

        JsonModule.preinit(event.getModConfigurationDirectory());

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

        if (Loader.isModLoaded("TConstruct"))
            TinkersConstructModule.init();

        if (Loader.isModLoaded("Natura"))
            NaturaModule.init();

        if (Loader.isModLoaded("BiomesOPlenty"))
            BiomesOPlentyModule.init();

        JsonModule.init();

        VillageModule.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (Config.removeTallGrassSeeds || Config.allSeedsEqual)
            GrassSeedsModule.postInit();

        TweaksModule.postInit();
        VillageModule.postInit();
        LootModule.postInit();

        MinecraftForge.EVENT_BUS.register(new IguanaEventHook());
        MinecraftForge.EVENT_BUS.register(new RespawnHungerModule());
        MinecraftForge.EVENT_BUS.register(new LootModule());

        if (Config.removeHoeRecipes)
        {
            RecipeRemover.removeAnyRecipe(new ItemStack(Items.WOODEN_HOE));
            RecipeRemover.removeAnyRecipe(new ItemStack(Items.STONE_HOE));
        }
    }

    @EventHandler
    public void handleIMCMessages(IMCEvent event)
    {
        IMCHandler.processMessages(event.getMessages());
    }
}
