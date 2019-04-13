package iguanaman.hungeroverhaul.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import iguanaman.hungeroverhaul.HungerOverhaul;
import iguanaman.hungeroverhaul.library.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config
{
    public static Config instance = new Config();

    public static Logger log = Util.getLogger("Config");

    private Config()
    {
    }

    public static void load(FMLPreInitializationEvent event)
    {
        File configurationDir = new File(event.getModConfigurationDirectory(), HungerOverhaul.modID);

        if (!configurationDir.exists())
        {
            try
            {
                if (!configurationDir.mkdir())
                {
                    log.error("Could not create config directory {}", configurationDir);
                    return;
                }
            }
            catch (SecurityException e)
            {
                log.error("Could not create config directory {}", configurationDir, e);
                return;
            }
        }

        final File configFile = new File(configurationDir, "HungerOverhaul.cfg");
        final File defaultConfigFile = new File(configurationDir, "default.cfg");
        final File blankSlateConfigFile = new File(configurationDir, "blankslate.cfg");

        {
            final File oldConfigFile = event.getSuggestedConfigurationFile();
            if (oldConfigFile.exists())
            {
                try
                {
                    if (!oldConfigFile.renameTo(configFile))
                    {
                        log.error("Could not move old config file {}", oldConfigFile);
                    }
                }
                catch (SecurityException e)
                {
                    log.error("Could not move old config file {}", oldConfigFile, e);
                }
            }
        }

        config = new Configuration(configFile, "0.0.0", false);
        defaultConfig = new Configuration(defaultConfigFile, "0.0.0", false);
        blankSlateConfig = new Configuration(blankSlateConfigFile, "0.0.0", false);

        MinecraftForge.EVENT_BUS.register(instance);

        syncMainConfig();
        syncDefaultConfig();
        syncBlankStateConfig();
    }

    @SubscribeEvent
    public void update(OnConfigChangedEvent event)
    {
        if (event.getModID().equals(HungerOverhaul.modID))
        {
            syncMainConfig();
        }
    }

    public static boolean syncMainConfig()
    {
        if (config == null)
        {
            return false;
        }

        // categories
        for (ConfigCategory configCategory : configCategories)
        {
            configCategory.create(config);
        }

        // info category from default/blankslate configs
        if (config.hasCategory(" "))
        {
            config.removeCategory(config.getCategory(" "));
        }

        // get all config option values
        allSeedsEqual = allSeedsEqualOption.get(config);
        removeHoeRecipes = removeHoeRecipesOption.get(config);
        modifyHoeUse = modifyHoeUseOption.get(config);
        removeTallGrassSeeds = removeTallGrassSeedsOption.get(config);
        seedChance = seedChanceOption.get(config);
        hoeToolDamageMultiplier = hoeToolDamageMultiplierOption.get(config);
        addSeedsCraftingRecipe = addSeedsCraftingRecipeOption.get(config);
        noSunlightRegrowthMultiplier = noSunlightRegrowthMultiplierOption.get(config);
        wrongBiomeRegrowthMultiplier = wrongBiomeRegrowthMultiplierOption.get(config);
        wrongBiomeRegrowthMultiplierSugarcane = wrongBiomeRegrowthMultiplierSugarcaneOption.get(config);
        flowerRegrowthMultiplier = flowerRegrowthMultiplierOption.get(config);
        cropRegrowthMultiplier = cropRegrowthMultiplierOption.get(config);
        sugarcaneRegrowthMultiplier = sugarcaneRegrowthMultiplierOption.get(config);
        cactusRegrowthMultiplier = cactusRegrowthMultiplierOption.get(config);
        cocoaRegrowthMultiplier = cocoaRegrowthMultiplierOption.get(config);
        treeCropRegrowthMultiplier = treeCropRegrowthMultiplierOption.get(config);
        saplingRegrowthMultiplier = saplingRegrowthMultiplierOption.get(config);
        netherWartRegrowthMultiplier = netherWartRegrowthMultiplierOption.get(config);
        dryingRackTimeMultiplier = dryingRackTimeMultiplierOption.get(config);
        eggTimeoutMultiplier = eggTimeoutMultiplierOption.get(config);
        breedingTimeoutMultiplier = breedingTimeoutMultiplierOption.get(config);
        childDurationMultiplier = childDurationMultiplierOption.get(config);
        milkedTimeout = milkedTimeoutOption.get(config);
        enableRightClickHarvesting = enableRightClickHarvestingOption.get(config);
        modifyCropDropsBreak = modifyCropDropsBreakOption.get(config);
        modifyCropDropsRightClick = modifyCropDropsRightClickOption.get(config);
        seedsPerHarvestRightClickMin = seedsPerHarvestRightClickMinOption.get(config);
        seedsPerHarvestRightClickMax = seedsPerHarvestRightClickMaxOption.get(config);
        seedsPerHarvestBreakMin = seedsPerHarvestBreakMinOption.get(config);
        seedsPerHarvestBreakMax = seedsPerHarvestBreakMaxOption.get(config);
        producePerHarvestRightClickMin = producePerHarvestRightClickMinOption.get(config);
        producePerHarvestRightClickMax = producePerHarvestRightClickMaxOption.get(config);
        producePerHarvestBreakMin = producePerHarvestBreakMinOption.get(config);
        producePerHarvestBreakMax = producePerHarvestBreakMaxOption.get(config);
        bonemealEffectiveness = bonemealEffectivenessOption.get(config);
        modifyBonemealGrowth = modifyBonemealGrowthOption.getBackwardsCompatible(config, difficultyScalingBoneMealOption);
        addCustomVillageField = addCustomVillageFieldOption.get(config);
        fieldNormalWeight = fieldNormalWeightOption.get(config);
        fieldReedWeight = fieldReedWeightOption.get(config);
        fieldStemWeight = fieldStemWeightOption.get(config);
        difficultyScaling = difficultyScalingOption.get(config);
        difficultyScalingBoneMeal = difficultyScalingBoneMealOption.get(config) && difficultyScaling;
        difficultyScalingEffects = difficultyScalingEffectsOption.get(config) && difficultyScaling;
        difficultyScalingHealing = difficultyScalingHealingOption.get(config) && difficultyScaling;
        difficultyScalingHunger = difficultyScalingHungerOption.get(config) && difficultyScaling;
        difficultyScalingRespawnHunger = difficultyScalingRespawnHungerOption.get(config) && difficultyScaling;
        modifyFoodValues = modifyFoodValuesOption.get(config);
        useHOFoodValues = useHOFoodValuesOption.get(config);
        addFoodTooltips = addFoodTooltipsOption.get(config);
        modifyFoodEatingSpeed = modifyFoodEatingSpeedOption.get(config);
        modifyFoodStackSize = modifyFoodStackSizeOption.get(config);
        foodStackSizeMultiplier = foodStackSizeMultiplierOption.get(config);

        foodHungerDivider = foodHungerDividerOption.get(config);

        foodSaturationDivider = foodSaturationDividerOption.get(config);
        foodHungerToSaturationDivider = foodHungerToSaturationDividerOption.get(config);
        addWellFedEffect = addWellFedEffectOption.get(config);
        wellFedDurationMultiplier = wellFedDurationMultiplierOption.get(config);
        wellFedEffectiveness = wellFedEffectivenessOption.get(config);
        addTradesButcher = addTradesButcherOption.get(config);
        addTradesFarmer = addCropTradesFarmerOption.get(config);
        addSaplingTradesFarmer = addSaplingTradesFarmerOption.get(config);
        addHarvestCraftChestLoot = addHarvestCraftChestLootOption.get(config);
        chestLootMaxStackSize = chestLootMaxStackSizeOption.get(config);
        chestLootChance = chestLootChanceOption.get(config);
        foodsUnplantable = foodsUnplantableOption.get(config);
        removeNaturaFlourCraftingRecipes = removeNaturaFlourCraftingRecipesOption.get(config);
        removeNaturaFlourSmeltingRecipe = removeNaturaFlourSmeltingRecipeOption.get(config);
        addAlternateNaturaFlourCraftingRecipes = addAlternateNaturaFlourCraftingRecipesOption.get(config);
        constantHungerLoss = constantHungerLossOption.get(config);
        damageOnStarve = damageOnStarveOption.get(config);
        enableRespawnHunger = enableRespawnHungerOption.get(config);
        respawnHungerValue = respawnHungerValueOption.get(config);
        respawnHungerDifficultyModifier = respawnHungerDifficultyModifierOption.get(config);
        disableHealingHungerDrain = disableHealingHungerDrainOption.get(config);
        hungerLossRatePercentage = hungerLossRatePercentageOption.get(config);
        addGuiText = addGuiTextOption.get(config);
        addLowStatEffects = addLowStatEffectsOption.get(config);
        addLowHealthNausea = addLowHealthNauseaOption.get(config);
        addLowHungerNausea = addLowHungerNauseaOption.get(config);
        addLowHealthSlowness = addLowHealthSlownessOption.get(config);
        addLowHungerSlowness = addLowHungerSlownessOption.get(config);
        addLowHealthWeakness = addLowHealthWeaknessOption.get(config);
        addLowHungerWeakness = addLowHungerWeaknessOption.get(config);
        addLowHealthMiningSlowdown = addLowHealthMiningSlowdownOption.get(config);
        addLowHungerMiningSlowdown = addLowHungerMiningSlowdownOption.get(config);
        foodRegensHealth = foodRegensHealthOption.get(config);
        minHungerToHeal = minHungerToHealOption.get(config);
        healthRegenRatePercentage = healthRegenRatePercentageOption.get(config);
        modifyRegenRateOnLowHealth = modifyRegenRateOnLowHealthOption.get(config);
        lowHealthRegenRateModifier = lowHealthRegenRateModifierOption.get(config);
        foodHealDivider = foodHealDividerOption.get(config);

        final boolean configChanged = config.hasChanged();
        if (configChanged)
        {
            config.save();
        }
        return configChanged;
    }

    public static boolean syncDefaultConfig()
    {
        if (defaultConfig == null)
        {
            return false;
        }

        defaultConfig.addCustomCategoryComment(" ", "This config contains all of the default Hunger Overhaul settings.\nIt's intended to serve as an easy backup for returning to the defaults.\nTo use, rename this file to HungerOverhaul.cfg (overwriting your current HungerOverhaul.cfg)");

        for (ConfigCategory configCategory : configCategories)
        {
            configCategory.create(defaultConfig);
        }
        for (ConfigOption<?> configOption : configOptions)
        {
            configOption.setToDefault(defaultConfig);
        }

        final boolean configChanged = defaultConfig.hasChanged();
        if (configChanged)
        {
            defaultConfig.save();
        }
        return configChanged;
    }

    public static boolean syncBlankStateConfig()
    {
        if (blankSlateConfig == null)
        {
            return false;
        }

        blankSlateConfig.addCustomCategoryComment(" ", "This config contains settings that will disable all of the changes that Hunger Overhaul makes.\nIt's intended to serve as a starting point for anyone wanting to only enable a few things and leave everything else untouched.\nTo use, rename this file to HungerOverhaul.cfg (overwriting your current HungerOverhaul.cfg)");

        for (ConfigCategory configCategory : configCategories)
        {
            configCategory.create(blankSlateConfig);
        }
        for (ConfigOption<?> configOption : configOptions)
        {
            configOption.setToBlankSlate(blankSlateConfig);
        }

        final boolean configChanged = blankSlateConfig.hasChanged();
        if (configChanged)
        {
            blankSlateConfig.save();
        }
        return configChanged;
    }

    //@formatter:off
    public static final List<ConfigCategory> configCategories = new ArrayList<ConfigCategory>();
    public static final List<ConfigOption<?>> configOptions = new ArrayList<ConfigOption<?>>();

    public static final ConfigCategory CATEGORY_GETTING_SEEDS = addCategory("getting seeds", "Options for obtaining seeds");
    public static final ConfigCategory CATEGORY_DELAYS = addCategory("delays", "Delays for various obtaining food related activities");
    public static final ConfigCategory CATEGORY_HARVESTING = addCategory("harvesting", "Options related to drops from crops");
    public static final ConfigCategory CATEGORY_CUSTOM_FIELD = addCategory("custom field", "Options for enabling and manipulating a custom field in Villages");
    public static final ConfigCategory CATEGORY_DIFFICULTY_SCALING = addCategory("difficulty scaling", "Options to scale the difficulty of certain elements based on game difficulty");
    public static final ConfigCategory CATEGORY_FOOD = addCategory("food", "Food related options");
    public static final ConfigCategory CATEGORY_HARVESTCRAFT = addCategory("harvestcraft", "Options related to HarvestCraft.\n" + TextFormatting.RED + "Only works if HarvestCraft is installed!");
    public static final ConfigCategory CATEGORY_NATURA = addCategory("natura", "Options related to Natura.\n" + TextFormatting.RED + "Only works if Natura is installed!");
    public static final ConfigCategory CATEGORY_HUNGER = addCategory("hunger", "Options related to hunger");
    public static final ConfigCategory CATEGORY_LOW_STATS = addCategory("low stats", "Options for how to handle when the player get low health/hunger");
    public static final ConfigCategory CATEGORY_HEALTH = addCategory("health", "Options related to health");

    public static ConfigCategory addCategory(String name, String comment)
    {
        return putCategoryInList(name, comment, configCategories);
    }

    public static ConfigCategory putCategoryInList(String name, String comment, List<ConfigCategory> list)
    {
        ConfigCategory category = new ConfigCategory(name, comment);
        list.add(category);
        return category;
    }

    public static <T> ConfigOption<T> addOption(ConfigCategory category, String name, T defaultValue, T blankSlate, String comment)
    {
        return addOption(category, name, defaultValue, null, null, blankSlate, comment);
    }

    public static <T> ConfigOption<T> addOption(ConfigCategory category, String name, T defaultValue, T minValue, T maxValue, T blankSlate, String comment)
    {
        return putOptionInList(category, name, defaultValue, minValue, maxValue, blankSlate, comment, configOptions);
    }

    public static <T> ConfigOption<T> putOptionInList(ConfigCategory category, String name, T defaultValue, T minValue, T maxValue, T blankSlate, String comment, List<ConfigOption<?>> list)
    {
        ConfigOption<T> configOption = new ConfigOption<T>(category.name, name, defaultValue, minValue, maxValue, blankSlate, comment);
        list.add(configOption);
        return configOption;
    }

    // seeds + hoes
    public static final ConfigOption<Boolean> allSeedsEqualOption = addOption(CATEGORY_GETTING_SEEDS, "allSeedsEqual", true, false, "Each seed has an equal chance to drop (grass drops and via hoes)");
    public static boolean allSeedsEqual;
    public static final ConfigOption<Boolean> removeTallGrassSeedsOption = addOption(CATEGORY_GETTING_SEEDS, "removeTallGrassSeeds", true, false, "Removes seed drops when breaking tall grass");
    public static boolean removeTallGrassSeeds;
    public static final ConfigOption<Boolean> modifyHoeUseOption = addOption(CATEGORY_GETTING_SEEDS, "modifyHoeUse", true, false, "Changes the use of hoes depending on the availability of water");
    public static boolean modifyHoeUse;
    public static final ConfigOption<Boolean> removeHoeRecipesOption = addOption(CATEGORY_GETTING_SEEDS, "removeHoeRecipes", true, false, "Whether wood and stone hoe recipes are removed");
    public static boolean removeHoeRecipes;
    public static final ConfigOption<Integer> hoeToolDamageMultiplierOption = addOption(CATEGORY_GETTING_SEEDS, "hoeToolDamageMultiplier", 5, 1, null, 1, "Multiplier on tool damage taken when a hoe is used ('modifyHoeUse' must be true)");
    public static int hoeToolDamageMultiplier;
    public static final ConfigOption<Integer> seedChanceOption = addOption(CATEGORY_GETTING_SEEDS, "seedChance", 20, 0, 100, 20, "Percent chance for seed to drop from hoe use on normal difficulty ('modifyHoeUse' must be true)");
    public static int seedChance;
    public static final ConfigOption<Boolean> addSeedsCraftingRecipeOption = addOption(CATEGORY_GETTING_SEEDS, "addSeedsCraftingRecipe", true, false, "Adds a crafting recipe to turn 1 wheat into 1 seed");
    public static boolean addSeedsCraftingRecipe;

    // delays
    public static final ConfigOption<Float> noSunlightRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "noSunlightRegrowthMultiplier", 2f, 0f, null, 1f, "Multipier on crop growth time without sunlight (1 to disable feature, 0 to make crops only grow in sunlight)");
    public static float noSunlightRegrowthMultiplier;
    public static final ConfigOption<Float> wrongBiomeRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "wrongBiomeRegrowthMultiplier", 2f, 0f, null, 1f, "Multipier on crop growth time (except sugarcane) in the wrong biome (1 to disable feature, 0 to make crops only grow in correct biome)");
    public static float wrongBiomeRegrowthMultiplier;
    public static final ConfigOption<Float> wrongBiomeRegrowthMultiplierSugarcaneOption = addOption(CATEGORY_DELAYS, "wrongBiomeRegrowthMultiplierSugarcane", 2f, 0f, null, 1f, "Multipier on sugarcane growth time in the wrong biome (1 to disable feature, 0 to make sugarcane only grow in correct biome)");
    public static float wrongBiomeRegrowthMultiplierSugarcane;
    public static final ConfigOption<Float> flowerRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "flowerRegrowthMultiplier", 1f, 0f, null, 1f, "Multiplier on the time it takes a WeeeFlower crop to grow");
    public static float flowerRegrowthMultiplier;
    public static final ConfigOption<Float> cropRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "cropRegrowthMultiplier", 4f, 0f, null, 1f, "Multiplier on the time it takes a non-tree crop to grow");
    public static float cropRegrowthMultiplier;
    public static final ConfigOption<Float> cactusRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "cactusRegrowthMultiplier", 4f, 0f, null, 1f, "Multiplier on the time it takes cactus to grow");
    public static float cactusRegrowthMultiplier;
    public static final ConfigOption<Float> cocoaRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "cocoaRegrowthMultiplier", 4f, 0f, null, 1f, "Multiplier on the time it takes cocoa to grow");
    public static float cocoaRegrowthMultiplier;
    public static final ConfigOption<Float> sugarcaneRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "sugarcaneRegrowthMultiplier", 4f, 0f, null, 1f, "Multiplier on the time it takes sugarcane to grow");
    public static float sugarcaneRegrowthMultiplier;
    public static final ConfigOption<Float> treeCropRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "treeCropRegrowthMultiplier", 4f, 0f, null, 1f, "Multiplier on the time it takes a tree crop to grow");
    public static float treeCropRegrowthMultiplier;
    public static final ConfigOption<Float> saplingRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "saplingRegrowthMultiplier", 4f, 0f, null, 1f, "Multiplier on the time it takes a sapling to grow into a tree");
    public static float saplingRegrowthMultiplier;
    public static final ConfigOption<Float> netherWartRegrowthMultiplierOption = addOption(CATEGORY_DELAYS, "netherWartRegrowthMultiplier", 4f, 0f, null, 1f, "Multiplier on the time it takes nether wart to grow");
    public static float netherWartRegrowthMultiplier;
    public static final ConfigOption<Float> dryingRackTimeMultiplierOption = addOption(CATEGORY_DELAYS, "dryingRackTimeMultiplier", 4f, 0f, null, 1f, "Multiplier on the time it takes food to dry on Tinkers' Construct drying racks");
    public static float dryingRackTimeMultiplier;
    public static final ConfigOption<Float> eggTimeoutMultiplierOption = addOption(CATEGORY_DELAYS, "eggTimeoutMultiplier", 4f, 0f, null, 1f, "Multiplier applied to the delay between chicken egg laying");
    public static float eggTimeoutMultiplier;
    public static final ConfigOption<Float> breedingTimeoutMultiplierOption = addOption(CATEGORY_DELAYS, "breedingTimeoutMultiplier", 4f, 0f, null, 1f, "Multiplier applied to the delay between breeding entities");
    public static float breedingTimeoutMultiplier;
    public static final ConfigOption<Float> childDurationMultiplierOption = addOption(CATEGORY_DELAYS, "childDurationMultiplier", 4f, 0f, null, 1f, "Multiplier applied to the delay before children become adults");
    public static float childDurationMultiplier;
    public static final ConfigOption<Integer> milkedTimeoutOption = addOption(CATEGORY_DELAYS, "milkedTimeout", 20, 0, null, 0, "Delay (in minutes) after milking a cow before it can be milked again");
    public static int milkedTimeout;

    // crop drops
    public static final ConfigOption<Boolean> enableRightClickHarvestingOption = addOption(CATEGORY_HARVESTING, "enableRightClickHarvesting", true, false, "Enables/disables harvest crops by right clicking them");
    public static boolean enableRightClickHarvesting;
    public static final ConfigOption<Boolean> modifyCropDropsRightClickOption = addOption(CATEGORY_HARVESTING, "modifyCropDropsRightClick", true, false, "Enables/disables modification of the item drops of crops when right clicking them (produce and seeds)");
    public static boolean modifyCropDropsRightClick;
    public static final ConfigOption<Boolean> modifyCropDropsBreakOption = addOption(CATEGORY_HARVESTING, "modifyCropDropsBreak", true, false, "Enables/disables modification of the item drops of crops when breaking them (produce and seeds)");
    public static boolean modifyCropDropsBreak;
    public static final ConfigOption<Integer> seedsPerHarvestRightClickMinOption = addOption(CATEGORY_HARVESTING, "seedsPerHarvestRightClickMin", 0, 0, null, 0, "Minimum number of seeds you get when harvesting a non-tree crop with right click (modifyCropDrops must be true)");
    public static int seedsPerHarvestRightClickMin;
    public static final ConfigOption<Integer> seedsPerHarvestRightClickMaxOption = addOption(CATEGORY_HARVESTING, "seedsPerHarvestRightClickMax", 0, 0, null, 0, "Maximum number of seeds you get when harvesting a non-tree crop with right click (modifyCropDrops must be true)");
    public static int seedsPerHarvestRightClickMax;
    public static final ConfigOption<Integer> seedsPerHarvestBreakMinOption = addOption(CATEGORY_HARVESTING, "seedsPerHarvestBreakMin", 0, 0, null, 0, "Minimum number of seeds you get when harvesting a non-tree crop by breaking it (modifyCropDrops must be true)");
    public static int seedsPerHarvestBreakMin;
    public static final ConfigOption<Integer> seedsPerHarvestBreakMaxOption = addOption(CATEGORY_HARVESTING, "seedsPerHarvestBreakMax", 0, 0, null, 0, "Maximum number of seeds you get when harvesting a non-tree crop by breaking it (modifyCropDrops must be tree)");
    public static int seedsPerHarvestBreakMax;
    public static final ConfigOption<Integer> producePerHarvestRightClickMinOption = addOption(CATEGORY_HARVESTING, "producePerHarvestRightClickMin", 2, 0, null, 2, "Minimum number of produce you get when harvesting a non-tree crop with right click (modifyCropDrops must be true)");
    public static int producePerHarvestRightClickMin;
    public static final ConfigOption<Integer> producePerHarvestRightClickMaxOption = addOption(CATEGORY_HARVESTING, "producePerHarvestRightClickMax", 4, 0, null, 4, "Maximum number of produce you get when harvesting a non-tree crop with right click (modifyCropDrops must be true)");
    public static int producePerHarvestRightClickMax;
    public static final ConfigOption<Integer> producePerHarvestBreakMinOption = addOption(CATEGORY_HARVESTING, "producePerHarvestBreakMin", 2, 0, null, 2, "Minimum number of produce you get when harvesting a non-tree crop by breaking it (modifyCropDrops must be true)");
    public static int producePerHarvestBreakMin;
    public static final ConfigOption<Integer> producePerHarvestBreakMaxOption = addOption(CATEGORY_HARVESTING, "producePerHarvestBreakMax", 4, 0, null, 4, "Maximum number of produce you get when harvesting a non-tree crop by breaking it (modifyCropDrops must be true)");
    public static int producePerHarvestBreakMax;
    public static final ConfigOption<Float> bonemealEffectivenessOption = addOption(CATEGORY_HARVESTING, "bonemealEffectiveness", 0.5f, 0.0f, 1.0f, 1.0f, "Multiplier on the effectiveness of bonemeal; the smaller this is, the more often bonemeal will fail to create growth. Set to 0 to disable bonemeal completely.");
    public static float bonemealEffectiveness;
    public static final ConfigOption<Boolean> modifyBonemealGrowthOption = addOption(CATEGORY_HARVESTING, "modifyBonemealGrowth", true, false, "Reduces the amount of growth from a successful bonemeal on certain plants (uses IguanaMan's opinionated values)");
    public static boolean modifyBonemealGrowth;

    // village field
    public static final ConfigOption<Boolean> addCustomVillageFieldOption = addOption(CATEGORY_CUSTOM_FIELD, "addCustomVillageField", true, false, "Adds a custom field to villages");
    public static boolean addCustomVillageField;
    public static final ConfigOption<Integer> fieldNormalWeightOption = addOption(CATEGORY_CUSTOM_FIELD, "fieldNormalWeight", 70, 0, null, 0, "Weighted chance for custom field to contain 'normal' crops");
    public static int fieldNormalWeight;
    public static final ConfigOption<Integer> fieldReedWeightOption = addOption(CATEGORY_CUSTOM_FIELD, "fieldReedWeight", 10, 0, null, 0, "Weighted chance for custom field to contain reeds");
    public static int fieldReedWeight;
    public static final ConfigOption<Integer> fieldStemWeightOption = addOption(CATEGORY_CUSTOM_FIELD, "fieldStemWeight", 10, 0, null, 0, "Weighted chance for custom field to contain pumpkins/melons");
    public static int fieldStemWeight;

    // difficulty scaling
    public static final ConfigOption<Boolean> difficultyScalingOption = addOption(CATEGORY_DIFFICULTY_SCALING, "difficultyScaling", true, false, "Enable/Disable all difficulty scaling effect in one setting");
    public static boolean difficultyScaling;
    public static final ConfigOption<Boolean> difficultyScalingBoneMealOption = addOption(CATEGORY_DIFFICULTY_SCALING, "difficultyScalingBoneMeal", true, false, "Effects of bone meal dependant of difficulty ('difficultyScaling' and 'modifyBonemealGrowth' must be true)");
    public static boolean difficultyScalingBoneMeal;
    public static final ConfigOption<Boolean> difficultyScalingEffectsOption = addOption(CATEGORY_DIFFICULTY_SCALING, "difficultyScalingEffects", true, false, "Negative effects on low health/hunger scale by difficulty ('difficultyScaling' must be true)");
    public static boolean difficultyScalingEffects;
    public static final ConfigOption<Boolean> difficultyScalingHealingOption = addOption(CATEGORY_DIFFICULTY_SCALING, "difficultyScalingHealing", true, false, "Healing rate scales by difficulty ('difficultyScaling' must be true)");
    public static boolean difficultyScalingHealing;
    public static final ConfigOption<Boolean> difficultyScalingHungerOption = addOption(CATEGORY_DIFFICULTY_SCALING, "difficultyScalingHunger", true, false, "Hunger loss rate scales by difficulty ('difficultyScaling' must be true)");
    public static boolean difficultyScalingHunger;
    public static final ConfigOption<Boolean> difficultyScalingRespawnHungerOption = addOption(CATEGORY_DIFFICULTY_SCALING, "difficultyScalingRespawnHunger", true, false, "Hunger value after respawn is affected by difficulty ('difficultyScaling' must be true)");
    public static boolean difficultyScalingRespawnHunger;

    // food
    public static final ConfigOption<Boolean> modifyFoodValuesOption = addOption(CATEGORY_FOOD, "modifyFoodValues", true, false, "Enables/disables all food value modification");
    public static boolean modifyFoodValues;
    public static final ConfigOption<Boolean> useHOFoodValuesOption = addOption(CATEGORY_FOOD, "useHOFoodValues", true, false, "Enables/disables Hunger Overhaul manually setting the food values for supported mods ('modifyFoodValues' must be true)");
    public static boolean useHOFoodValues;
    public static final ConfigOption<Boolean> addFoodTooltipsOption = addOption(CATEGORY_FOOD, "addFoodTooltips", true, false, "Add tooltips to food items showing hints at food value");
    public static boolean addFoodTooltips;
    public static final ConfigOption<Boolean> modifyFoodStackSizeOption = addOption(CATEGORY_FOOD, "modifyFoodStackSize", true, false, "Changes the stack size of food to be dependant on the food's replenishment value");
    public static boolean modifyFoodStackSize;
    public static final ConfigOption<Boolean> modifyFoodEatingSpeedOption = addOption(CATEGORY_FOOD, "modifyFoodEatingSpeed", true, false, "Changes the eating animation speed to be dependant on the food's replenishment value");
    public static boolean modifyFoodEatingSpeed;
    public static final ConfigOption<Integer> foodStackSizeMultiplierOption = addOption(CATEGORY_FOOD, "foodStackSizeMultiplier", 1, 1, null, 1, "Multiplier on the stack size of food ('modifyFoodStackSize' must be true)");
    public static int foodStackSizeMultiplier;
    public static final ConfigOption<Float> foodHungerDividerOption = addOption(CATEGORY_FOOD, "foodHungerDivider", 4.0f, 1.0f, "Food values not manually set (see 'useHOFoodValues') will have their hunger value divided by this ('modifyFoodValues' must be true)");
    public static float foodHungerDivider;
    public static final ConfigOption<Float> foodSaturationDividerOption = addOption(CATEGORY_FOOD, "foodSaturationDivider", 1.0f, 1.0f, "Food values not manually set (see 'useHOFoodValues') will have their saturation modifier divided by this ('modifyFoodValues' must be true)\nNote: Gets applied after 'foodHungerToSaturationDivider'");
    public static float foodSaturationDivider;
    public static final ConfigOption<Float> foodHungerToSaturationDividerOption = addOption(CATEGORY_FOOD, "foodHungerToSaturationDivider", 20.0f, 0.0f, "Food values not manually set (see 'useHOFoodValues') will have their saturation modifier set to <modified hunger> divided by this ('modifyFoodValues' must be true)\nSet to 0 to disable");
    public static float foodHungerToSaturationDivider;
    public static final ConfigOption<Boolean> addWellFedEffectOption = addOption(CATEGORY_FOOD, "addWellFedEffect", true, false, "Adds a 'well fed' effect that gives slight health regen");
    public static boolean addWellFedEffect;
    public static final ConfigOption<Float> wellFedDurationMultiplierOption = addOption(CATEGORY_FOOD, "wellFedDurationMultiplier", 1.0f, 1.0f, "The base duration (in ticks) for the Well fed effect is (hunger_value * 100)^1.2\nThe result above will be multiplied by this value");
    public static float wellFedDurationMultiplier;
    public static final ConfigOption<Float> wellFedEffectivenessOption = addOption(CATEGORY_FOOD, "wellFedEffectiveness", 0.25f, 0.25f, "How much faster the well fed effect will make health regen. E.g. 0.25 means that the health will regen 25% faster. Setting this to 1 means that health will regen at maximum speed (about 1 hp per tick)");
    public static float wellFedEffectiveness;

    // harvestcraft integration
    public static final ConfigOption<Boolean> addTradesButcherOption = addOption(CATEGORY_HARVESTCRAFT, "addTradesButcher", true, false, "Add high tier HarvestCraft foods to the items butcher villagers will sell (HarvestCraft)");
    public static boolean addTradesButcher;
    public static final ConfigOption<Boolean> addCropTradesFarmerOption = addOption(CATEGORY_HARVESTCRAFT, "addCropTradesFarmer", true, false, "Add HarvestCraft crop produce to the items farmer villagers will buy (HarvestCraft)");
    public static boolean addTradesFarmer;
    public static final ConfigOption<Boolean> addSaplingTradesFarmerOption = addOption(CATEGORY_HARVESTCRAFT, "addSaplingTradesFarmer", true, false, "Add HarvestCraft saplings to the items farmer villagers will sell (HarvestCraft)");
    public static boolean addSaplingTradesFarmer;
    public static final ConfigOption<Boolean> addHarvestCraftChestLootOption = addOption(CATEGORY_HARVESTCRAFT, "addHarvestCraftChestLoot", true, false, "HarvestCraft items added to dungeon/temple chests (Harvestcraft)");
    public static boolean addHarvestCraftChestLoot;
    public static final ConfigOption<Integer> chestLootMaxStackSizeOption = addOption(CATEGORY_HARVESTCRAFT, "chestLootMaxStackSize", 16, 1, null, 64, "Max stack size for food to be found in chests (addHarvestCraftChestLoot must be true)");
    public static int chestLootMaxStackSize;
    public static final ConfigOption<Integer> chestLootChanceOption = addOption(CATEGORY_HARVESTCRAFT, "chestLootChance", 5, 1, null, 5, "Weighted chance for food to be found in chests (addHarvestCraftChestLoot must be true)");
    public static int chestLootChance;
    public static final ConfigOption<Boolean> foodsUnplantableOption = addOption(CATEGORY_HARVESTCRAFT, "foodsUnplantable", true, false, "Makes HarvestCraft foods unplantable, meaning seeds are required to plant crops");
    public static boolean foodsUnplantable;

    // natura integration
    public static final ConfigOption<Boolean> removeNaturaFlourCraftingRecipesOption = addOption(CATEGORY_NATURA, "removeNaturaFlourCraftingRecipes", true, false, "Removes the default barley/wheat -> flour recipes.\nNote: These recipes are always removed if 'addSeedsCraftingRecipe' in 'getting seeds' is true because otherwise they'd conflict.");
    public static boolean removeNaturaFlourCraftingRecipes;
    public static final ConfigOption<Boolean> removeNaturaFlourSmeltingRecipeOption = addOption(CATEGORY_NATURA, "removeNaturaFlourSmeltingRecipe", true, false, "Removes the default barley/wheat flour -> bread smelting recipes.");
    public static boolean removeNaturaFlourSmeltingRecipe;
    public static final ConfigOption<Boolean> addAlternateNaturaFlourCraftingRecipesOption = addOption(CATEGORY_NATURA, "addAlternateNaturaFlourCraftingRecipes", false, false, "Adds alternate recipes to turn barley/wheat into flour: 2x wheat/barley -> flour.");
    public static boolean addAlternateNaturaFlourCraftingRecipes;

    // hunger
    public static final ConfigOption<Boolean> constantHungerLossOption = addOption(CATEGORY_HUNGER, "constantHungerLoss", true, false, "You lose hunger (very slowly) at all times");
    public static boolean constantHungerLoss;
    public static final ConfigOption<Integer> damageOnStarveOption = addOption(CATEGORY_HUNGER, "damageOnStarve", 200, 2, "Amount of damage you take when hunger hits zero");
    public static int damageOnStarve;
    public static final ConfigOption<Boolean> enableRespawnHungerOption = addOption(CATEGORY_HUNGER, "enableRespawnHunger", true, false, "Enable setting hunger after respawning (see 'respawnHungerValue')");
    public static boolean enableRespawnHunger;
    public static final ConfigOption<Integer> respawnHungerValueOption = addOption(CATEGORY_HUNGER, "respawnHungerValue", 20, 0, null, 20, "hunger value set after respawning for peaceful/easy difficulty");
    public static int respawnHungerValue;
    public static final ConfigOption<Integer> respawnHungerDifficultyModifierOption = addOption(CATEGORY_HUNGER, "respawnHungerDifficultyModifier", 4, 0, null, 4, "The amount difficulty modifies the hunger value set after respawning  ('difficultyScaling' and 'difficultyScalingRespawnHunger' must be true)");
    public static int respawnHungerDifficultyModifier;
    public static final ConfigOption<Boolean> disableHealingHungerDrainOption = addOption(CATEGORY_HUNGER, "disableHealingHungerDrain", true, false, "Disable the hunger drain when healing that was introduced in vanilla 1.6.2");
    public static boolean disableHealingHungerDrain;
    public static final ConfigOption<Float> hungerLossRatePercentageOption = addOption(CATEGORY_HUNGER, "hungerLossRatePercentage", 4F/3F * 100F, 0F, null, 100F, "Speed up or slow down the rate that hunger drops (set to 0 to disable hunger loss)");
    public static float hungerLossRatePercentage;

    // low stat
    public static final ConfigOption<Boolean> addGuiTextOption = addOption(CATEGORY_LOW_STATS, "addGuiText", true, false, "Shows onscreen text when hunger/health is low");
    public static boolean addGuiText;
    public static final ConfigOption<Boolean> addLowStatEffectsOption = addOption(CATEGORY_LOW_STATS, "addLowStatEffects", true, false, "Enables/disables all low hunger/health effects");
    public static boolean addLowStatEffects;
    public static final ConfigOption<Boolean> addLowHealthNauseaOption = addOption(CATEGORY_LOW_STATS, "addLowHealthNausea", true, false, "Nausea effect when health is really low ('addLowStatEffects' must be true)");
    public static boolean addLowHealthNausea;
    public static final ConfigOption<Boolean> addLowHungerNauseaOption = addOption(CATEGORY_LOW_STATS, "addLowHungerNausea", true, false, "Nausea effect when hunger is really low ('addLowStatEffects' must be true)");
    public static boolean addLowHungerNausea;
    public static final ConfigOption<Boolean> addLowHealthSlownessOption = addOption(CATEGORY_LOW_STATS, "addLowHealthSlowness", true, false, "Slowness effect when health is low ('addLowStatEffects' must be true)");
    public static boolean addLowHealthSlowness;
    public static final ConfigOption<Boolean> addLowHungerSlownessOption = addOption(CATEGORY_LOW_STATS, "addLowHungerSlowness", true, false, "Slowness effect when hunger is low ('addLowStatEffects' must be true)");
    public static boolean addLowHungerSlowness;
    public static final ConfigOption<Boolean> addLowHealthWeaknessOption = addOption(CATEGORY_LOW_STATS, "addLowHealthWeakness", true, false, "Weakness effect when health is low ('addLowStatEffects' must be true)");
    public static boolean addLowHealthWeakness;
    public static final ConfigOption<Boolean> addLowHungerWeaknessOption = addOption(CATEGORY_LOW_STATS, "addLowHungerWeakness", true, false, "Weakness effect when hunger is low ('addLowStatEffects' must be true)");
    public static boolean addLowHungerWeakness;
    public static final ConfigOption<Boolean> addLowHealthMiningSlowdownOption = addOption(CATEGORY_LOW_STATS, "addLowHealthMiningSlowdown", true, false, "Mining slowdown effect when health is low ('addLowStatEffects' must be true)");
    public static boolean addLowHealthMiningSlowdown;
    public static final ConfigOption<Boolean> addLowHungerMiningSlowdownOption = addOption(CATEGORY_LOW_STATS, "addLowHungerMiningSlowdown", true, false, "Mining slowdown effect when hunger is low ('addLowStatEffects' must be true)");
    public static boolean addLowHungerMiningSlowdown;

    // health
    public static final ConfigOption<Integer> minHungerToHealOption = addOption(CATEGORY_HEALTH, "minHungerToHeal", 7, 0, null, 18, "Minimum hunger level before healing starts");
    public static int minHungerToHeal;
    public static final ConfigOption<Boolean> foodRegensHealthOption = addOption(CATEGORY_HEALTH, "foodRegensHealth", false, false, "Eating food regenerates health");
    public static boolean foodRegensHealth;
    public static final ConfigOption<Integer> foodHealDividerOption = addOption(CATEGORY_HEALTH, "foodHealDivider", 4, 0, null, 4, "Amount of health food restores is the food value divided by this number (foodRegensHealth must be true)");
    public static int foodHealDivider;
    public static final ConfigOption<Integer> healthRegenRatePercentageOption = addOption(CATEGORY_HEALTH, "healthRegenRatePercentage", 100, 0, null, 100, "Speed up or slow down the rate that health regenerates (0 to disable regen)");
    public static int healthRegenRatePercentage;
    public static final ConfigOption<Boolean> modifyRegenRateOnLowHealthOption = addOption(CATEGORY_HEALTH, "modifyRegenRateOnLowHealth", true, false, "The lower your health the longer it takes to regen");
    public static boolean modifyRegenRateOnLowHealth;
    public static final ConfigOption<Integer> lowHealthRegenRateModifierOption = addOption(CATEGORY_HEALTH, "lowHealthRegenRateModifier", 5, 0, null, 5, "The lower your health the longer it takes to regen, modify the effect here (lower = less effect) (modifyRegenRateOnLowHealth must be true)");
    public static int lowHealthRegenRateModifier;

    static Configuration config;
    static Configuration blankSlateConfig;
    static Configuration defaultConfig;
    //@formatter:on
}
