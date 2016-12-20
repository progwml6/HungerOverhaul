package iguanaman.hungeroverhaul.module;

public class ModuleWeeeFlowers
{

    public static void init()
    {
        /*  // Add Thaumcraft aspects
        if (Loader.isModLoaded("Thaumcraft"))
            // Flower seeds
            for (Item flowerSeed : PamsModsHelper.PamFlowerSeeds)
                if (!ThaumcraftApi.exists(flowerSeed, -1))
                    ThaumcraftApi.registerObjectTag(new ItemStack(flowerSeed), new int[]{-1}, new AspectList().add(Aspect.PLANT, 1));
        
        // Flower blocks
        Block[] flowers = new Block[]
        {
        weeeflowers.pamwhiteflowerVine, weeeflowers.pamorangeflowerVine, weeeflowers.pammagentaflowerVine,
        weeeflowers.pamlightblueflowerVine, weeeflowers.pamyellowflowerVine, weeeflowers.pamlimeflowerVine,
        weeeflowers.pampinkflowerVine, weeeflowers.pamlightgreyflowerVine, weeeflowers.pamdarkgreyflowerVine,
        weeeflowers.pamcyanflowerVine, weeeflowers.pampurpleflowerVine, weeeflowers.pamblueflowerVine,
        weeeflowers.pambrownflowerVine, weeeflowers.pamgreenflowerVine, weeeflowers.pamredflowerVine,
        weeeflowers.pamblackflowerVine, weeeflowers.pamFlower
        };
        
        AspectList flowerAspects = new AspectList().add(Aspect.PLANT, 1);
        
        for (Block flower : flowers)
            if (!ThaumcraftApi.exists(Item.getItemFromBlock(flower), -1))
                ThaumcraftApi.registerObjectTag(new ItemStack(flower), new int[]{-1}, flowerAspects);
        
        // Flower growth modification
        PlantGrowthModification flowerGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.flowerRegrowthMultiplier);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockPamFlowerCrop.class, flowerGrowthModification);
        */}

}
