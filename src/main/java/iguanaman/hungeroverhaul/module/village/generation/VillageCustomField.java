package iguanaman.hungeroverhaul.module.village.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.module.harvestcraft.helper.PamsModsHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class VillageCustomField extends Village
{
    private FieldType typeA;

    private FieldType typeB;

    private int groundLevel = -1;

    public VillageCustomField(Start villagePiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, EnumFacing facing)
    {
        super(villagePiece, par2);
        this.setCoordBaseMode(facing);
        this.boundingBox = par4StructureBoundingBox;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        if (groundLevel < 0)
        {
            groundLevel = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

            if (groundLevel < 0)
                return true;

            boundingBox.offset(0, groundLevel - boundingBox.maxY + 4 - 1, 0);
        }

        BlockPos center = new BlockPos(this.boundingBox.getCenter());

        Biome biome = worldIn.getBiome(center);

        typeA = randomFieldType(biome, randomIn);
        typeB = randomFieldType(biome, randomIn);

        IBlockState blockStateOutA = Blocks.FARMLAND.getDefaultState();
        IBlockState blockStateInA = Blocks.FLOWING_WATER.getDefaultState();
        IBlockState blockStateOutB = Blocks.FARMLAND.getDefaultState();
        IBlockState blockStateInB = Blocks.FLOWING_WATER.getDefaultState();

        IBlockState AIR = Blocks.AIR.getDefaultState();
        IBlockState LOG = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());

        Block crop1A;
        Block crop2A;
        Block crop1B;
        Block crop2B;

        if (typeA == FieldType.REED)
        {
            if (BiomeDictionary.isBiomeOfType(biome, Type.SANDY))
                blockStateOutA = Blocks.SAND.getDefaultState();
            else
                blockStateOutA = Blocks.DIRT.getDefaultState();
            crop1A = Blocks.REEDS;
            crop2A = Blocks.REEDS;
        }
        else if (typeA == FieldType.STEM)
        {
            crop1A = this.getRandomStemCrop(randomIn);
            crop2A = this.getRandomStemCrop(randomIn);
        }
        else
        {
            crop1A = this.getRandomCrop(randomIn);
            crop2A = this.getRandomCrop(randomIn);
        }

        if (typeB == FieldType.REED)
        {
            if (BiomeDictionary.isBiomeOfType(biome, Type.SANDY))
                blockStateOutB = Blocks.SAND.getDefaultState();
            else
                blockStateOutB = Blocks.DIRT.getDefaultState();
            crop1B = Blocks.REEDS;
            crop2B = Blocks.REEDS;
        }
        else if (typeA == FieldType.STEM)
        {
            crop1B = this.getRandomStemCrop(randomIn);
            crop2B = this.getRandomStemCrop(randomIn);
        }
        else
        {
            crop1B = this.getRandomCrop(randomIn);
            crop2B = this.getRandomCrop(randomIn);
        }

        int cropMeta1A = getCropMeta(crop1A, randomIn);
        int cropMeta2A = getCropMeta(crop2A, randomIn);
        int cropMeta1B = getCropMeta(crop1B, randomIn);
        int cropMeta2B = getCropMeta(crop2B, randomIn);

        //BASE
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 12, 4, 8, AIR, AIR, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, LOG, LOG, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, LOG, LOG, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 0, 0, 12, 0, 8, LOG, LOG, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 11, 0, 0, LOG, LOG, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 11, 0, 8, LOG, LOG, false);

        //A
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, blockStateOutA, blockStateOutA, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, blockStateInA, blockStateInA, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, blockStateOutA, blockStateOutA, false);

        //B
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 8, 0, 7, blockStateOutB, blockStateOutB, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 1, 9, 0, 7, blockStateInB, blockStateInB, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 1, 11, 0, 7, blockStateOutB, blockStateOutB, false);

        //CROPS
        for (int i = 1; i <= 7; i++)
        {
            if (typeA == FieldType.NORMAL)
                this.placeCropAtCurrentPosition(worldIn, crop1A.getStateFromMeta(cropMeta1A), 1, 1, i, structureBoundingBoxIn);

            this.placeCropAtCurrentPosition(worldIn, crop1A.getStateFromMeta(cropMeta1A), 2, 1, i, structureBoundingBoxIn);
            this.placeCropAtCurrentPosition(worldIn, crop2A.getStateFromMeta(cropMeta2A), 4, 1, i, structureBoundingBoxIn);

            if (typeA == FieldType.NORMAL)
                this.placeCropAtCurrentPosition(worldIn, crop2A.getStateFromMeta(cropMeta2A), 5, 1, i, structureBoundingBoxIn);
            if (typeA == FieldType.NORMAL)
                this.placeCropAtCurrentPosition(worldIn, crop1B.getStateFromMeta(cropMeta1B), 7, 1, i, structureBoundingBoxIn);

            this.placeCropAtCurrentPosition(worldIn, crop1B.getStateFromMeta(cropMeta1B), 8, 1, i, structureBoundingBoxIn);
            this.placeCropAtCurrentPosition(worldIn, crop2B.getStateFromMeta(cropMeta2B), 10, 1, i, structureBoundingBoxIn);

            if (typeA == FieldType.NORMAL)
                this.placeCropAtCurrentPosition(worldIn, crop2B.getStateFromMeta(cropMeta2B), 11, 1, i, structureBoundingBoxIn);
        }

        //FILLER
        for (int j2 = 0; j2 < 9; ++j2)
        {
            for (int k2 = 0; k2 < 13; ++k2)
            {
                this.clearCurrentPositionBlocksUpwards(worldIn, k2, 4, j2, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), k2, -1, j2, structureBoundingBoxIn);
            }
        }

        return true;
    }

    private enum FieldType
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
        return Loader.isModLoaded("harvestcraft") ? this.getRandomCropHarvestCraft(random) : this.getRandomCropVanilla(random);
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

        return random.nextInt(length + 3) <= length ? PamsModsHelper.PamCrops[random.nextInt(length)] : this.getRandomCropVanilla(random);
    }

    private int getCropMeta(Block block, Random random)
    {
        return random.nextInt(4);
    }

    private FieldType randomFieldType(Biome biome, Random random)
    {
        int normalWeight = Config.fieldNormalWeight;
        int reedWeight = Config.fieldReedWeight;
        int stemWeight = Config.fieldStemWeight;

        if (BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE) || BiomeDictionary.isBiomeOfType(biome, Type.SWAMP))
        {
            reedWeight *= 2;
            stemWeight *= 2;
        }

        int weights = normalWeight + reedWeight + stemWeight;
        int rnd = random.nextInt(weights);

        if (rnd < reedWeight)
            return FieldType.REED;
        else if (rnd < reedWeight + stemWeight)
            return FieldType.STEM;
        else
            return FieldType.NORMAL;

    }

    private void placeCropAtCurrentPosition(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
    {
        if (Loader.isModLoaded("harvestcraft"))
            this.placeHarvestCraftCropAtCurrentPosition(worldIn, blockstateIn, x, y, z, boundingboxIn);
        else
            this.replaceAirAndLiquidDownwards(worldIn, blockstateIn, x, y, z, boundingboxIn);
    }

    private void placeHarvestCraftCropAtCurrentPosition(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
    {
        int i = this.getXWithOffset(x, z);
        int j = this.getYWithOffset(y);
        int k = this.getZWithOffset(x, z);

        BlockPos pos = new BlockPos(i, j, k);

        if (boundingboxIn.isVecInside(pos))
        {
            worldIn.setBlockState(pos, blockstateIn, 2);
        }
    }

    public static class VillageManager implements IVillageCreationHandler
    {
        @Override
        public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 13, 4, 9, facing);
            return (!canVillageGoDeeper(box)) || (StructureComponent.findIntersecting(pieces, box) != null) ? null : new VillageCustomField(startPiece, p5, random, box, facing);
        }

        @Override
        public PieceWeight getVillagePieceWeight(Random random, int i)
        {
            return new PieceWeight(VillageCustomField.class, 15, MathHelper.getInt(random, 0 + i, 1 + i));
        }

        @Override
        public Class<?> getComponentClass()
        {
            return VillageCustomField.class;
        }
    }

}
