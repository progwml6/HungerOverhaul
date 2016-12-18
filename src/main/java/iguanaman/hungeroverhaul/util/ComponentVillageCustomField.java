package iguanaman.hungeroverhaul.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.module.PamsModsHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.Loader;

public class ComponentVillageCustomField extends StructureVillagePieces.Village
{
    private fieldType typeA;

    private fieldType typeB;

    public ComponentVillageCustomField()
    {
    }

    public ComponentVillageCustomField(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing)
    {
        super(start, type);
        this.setCoordBaseMode(facing);
        this.boundingBox = structurebb;
    }

    private enum fieldType
    {
        NORMAL, REED, STEM
    }

    /**
     * Returns a crop type to be planted on this field.
     */
    private Block getRandomStemCrop(Random par1Random)
    {
        ArrayList<Block> crops = Lists.newArrayList();
        crops.add(Blocks.PUMPKIN_STEM);
        crops.add(Blocks.MELON_STEM);
        return crops.get(par1Random.nextInt(crops.size() - 1));
    }

    private Block getRandomCrop(Random random)
    {
        return Loader.isModLoaded("harvestcraft") ? getRandomCropHarvestCraft(random) : getRandomCropVanilla(random);
    }

    private Block getRandomCropVanilla(Random random)
    {
        ArrayList<Block> crops = new ArrayList<Block>();

        crops.add(Blocks.WHEAT);
        crops.add(Blocks.CARROTS);
        crops.add(Blocks.POTATOES);

        return crops.get(random.nextInt(crops.size()));
    }

    private Block getRandomCropHarvestCraft(Random random)
    {
        int length = PamsModsHelper.PamCrops.length;

        return random.nextInt(length + 3) <= length ? PamsModsHelper.PamCrops[random.nextInt(length)] : getRandomCropVanilla(random);
    }

    private int getCropMeta(Block block, Random random)
    {
        return random.nextInt(5);
    }

    public static ComponentVillageCustomField createPiece(StructureVillagePieces.Start start, List<StructureComponent> listIn, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(structureMinX, structureMinY, structureMinZ, 0, 0, 0, 4, 6, 5, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(listIn, structureboundingbox) == null ? new ComponentVillageCustomField(start, type, rand, structureboundingbox, facing) : null;
    }

    private fieldType randomFieldType(Biome biome, Random random)
    {
        int normalWeight = IguanaConfig.fieldNormalWeight;
        int reedWeight = IguanaConfig.fieldReedWeight;
        int stemWeight = IguanaConfig.fieldStemWeight;

        if (BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE) || BiomeDictionary.isBiomeOfType(biome, Type.SWAMP))
        {
            reedWeight *= 2;
            stemWeight *= 2;
        }

        int weights = normalWeight + reedWeight + stemWeight;
        int rnd = random.nextInt(weights);

        if (rnd < reedWeight)
            return fieldType.REED;
        else if (rnd < reedWeight + stemWeight)
            return fieldType.STEM;
        else
            return fieldType.NORMAL;

    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        if (this.averageGroundLvl < 0)
        {
            this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

            if (this.averageGroundLvl < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 12 - 1, 0);
        }

        BlockPos center = new BlockPos(this.boundingBox.getCenter());

        Biome biome = worldIn.getBiome(center);

        typeA = randomFieldType(biome, randomIn);
        typeB = randomFieldType(biome, randomIn);

        IBlockState blockStateOutA = Blocks.FARMLAND.getDefaultState();
        IBlockState blockStateInA = Blocks.FLOWING_WATER.getDefaultState();
        IBlockState blockStateOutB = Blocks.FARMLAND.getDefaultState();
        IBlockState blockStateInB = Blocks.FLOWING_WATER.getDefaultState();

        Block crop1A;
        Block crop2A;
        Block crop1B;
        Block crop2B;

        if (typeA == fieldType.REED)
        {
            if (BiomeDictionary.isBiomeOfType(biome, Type.SANDY))
                blockStateOutA = Blocks.SAND.getDefaultState();
            else
                blockStateOutA = Blocks.DIRT.getDefaultState();
            crop1A = Blocks.REEDS;
            crop2A = Blocks.REEDS;
        }
        else if (typeA == fieldType.STEM)
        {
            crop1A = getRandomStemCrop(randomIn);
            crop2A = getRandomStemCrop(randomIn);
        }
        else
        {
            crop1A = getRandomCrop(randomIn);
            crop2A = getRandomCrop(randomIn);
        }

        if (typeB == fieldType.REED)
        {
            if (BiomeDictionary.isBiomeOfType(biome, Type.SANDY))
                blockStateOutB = Blocks.SAND.getDefaultState();
            else
                blockStateOutB = Blocks.DIRT.getDefaultState();
            crop1B = Blocks.REEDS;
            crop2B = Blocks.REEDS;
        }
        else if (typeA == fieldType.STEM)
        {
            crop1B = getRandomStemCrop(randomIn);
            crop2B = getRandomStemCrop(randomIn);
        }
        else
        {
            crop1B = getRandomCrop(randomIn);
            crop2B = getRandomCrop(randomIn);
        }

        int cropMeta1A = getCropMeta(crop1A, randomIn);
        int cropMeta2A = getCropMeta(crop2A, randomIn);
        int cropMeta1B = getCropMeta(crop1B, randomIn);
        int cropMeta2B = getCropMeta(crop2B, randomIn);

        IBlockState cropState1A = crop1A.getStateFromMeta(cropMeta1A);
        IBlockState cropState2A = crop1A.getStateFromMeta(cropMeta2A);
        IBlockState cropState1B = crop1A.getStateFromMeta(cropMeta1B);
        IBlockState cropState2B = crop1A.getStateFromMeta(cropMeta2B);

        IBlockState AIR = Blocks.AIR.getDefaultState();
        IBlockState LOG = Blocks.LOG.getDefaultState();

        //BASE
        fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 12, 4, 8, AIR, AIR, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, LOG, LOG, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, LOG, LOG, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 0, 0, 12, 0, 8, LOG, LOG, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 11, 0, 0, LOG, LOG, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 11, 0, 8, LOG, LOG, false);

        //A
        fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, blockStateOutA, blockStateOutA, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, blockStateInA, blockStateInA, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, blockStateOutA, blockStateOutA, false);

        //B
        fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 8, 0, 7, blockStateOutB, blockStateOutB, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 1, 9, 0, 7, blockStateInB, blockStateInB, false);
        fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 1, 11, 0, 7, blockStateOutB, blockStateOutB, false);

        //CROPS
        for (int tempZ = 1; tempZ <= 7; tempZ++)
        {
            if (typeA == fieldType.NORMAL)
                placeCropAtCurrentPosition(worldIn, cropState1A, 1, 1, tempZ, structureBoundingBoxIn);
            placeCropAtCurrentPosition(worldIn, cropState1A, 2, 1, tempZ, structureBoundingBoxIn);
            placeCropAtCurrentPosition(worldIn, cropState2A, 4, 1, tempZ, structureBoundingBoxIn);
            if (typeA == fieldType.NORMAL)
                placeCropAtCurrentPosition(worldIn, cropState2A, 5, 1, tempZ, structureBoundingBoxIn);
            if (typeA == fieldType.NORMAL)
                placeCropAtCurrentPosition(worldIn, cropState1B, 7, 1, tempZ, structureBoundingBoxIn);
            placeCropAtCurrentPosition(worldIn, cropState1B, 8, 1, tempZ, structureBoundingBoxIn);
            placeCropAtCurrentPosition(worldIn, cropState2B, 10, 1, tempZ, structureBoundingBoxIn);
            if (typeA == fieldType.NORMAL)
                placeCropAtCurrentPosition(worldIn, cropState2B, 11, 1, tempZ, structureBoundingBoxIn);
        }

        //FILLER
        for (int z1 = 0; z1 < 9; ++z1)
            for (int x1 = 0; x1 < 13; ++x1)
            {
                clearCurrentPositionBlocksUpwards(worldIn, x1, 4, z1, structureBoundingBoxIn);
                replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), x1, -1, z1, structureBoundingBoxIn);
            }

        return true;
    }

    private void placeCropAtCurrentPosition(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
    {
        if (Loader.isModLoaded("harvestcraft"))
            placeHarvestCraftCropAtCurrentPosition(worldIn, blockstateIn, x, y, z, boundingboxIn);
        else
            replaceAirAndLiquidDownwards(worldIn, blockstateIn, x, y, z, boundingboxIn);
    }

    private void placeHarvestCraftCropAtCurrentPosition(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
    {
        int i = this.getXWithOffset(x, z);
        int j = this.getYWithOffset(y);
        int k = this.getZWithOffset(x, z);

        if (boundingboxIn.isVecInside(new BlockPos(i, j, k)))
        {
            worldIn.setBlockState(new BlockPos(i, j, k), blockstateIn, 2);
        }
    }
}
