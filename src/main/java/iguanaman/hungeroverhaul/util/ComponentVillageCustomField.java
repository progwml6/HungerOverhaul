package iguanaman.hungeroverhaul.util;

import com.google.common.collect.Lists;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.module.PamsModsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.Loader;

public class ComponentVillageCustomField extends StructureVillagePieces.Village
{
	private int averageGroundLevel = -1;
	private fieldType typeA;
	private fieldType typeB;

	public ComponentVillageCustomField() {}

	public ComponentVillageCustomField(Start par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
	{
		super(par1ComponentVillageStartPiece, par2);
		coordBaseMode = par5;
		boundingBox = par4StructureBoundingBox;
	}

	private enum fieldType { NORMAL, REED, STEM }

	/**
	 * Returns a crop type to be planted on this field.
	 */
	private Block getRandomStemCrop(Random par1Random)
	{
		ArrayList<Block> crops = Lists.newArrayList();
        crops.add(Blocks.pumpkin_stem);
		crops.add(Blocks.melon_stem);
		return crops.get(par1Random.nextInt(crops.size() - 1));
	}

	private Block getRandomCrop(Random random)
	{
		return Loader.isModLoaded("harvestcraft") ? getRandomCropHarvestCraft(random) : getRandomCropVanilla(random);
	}

	private Block getRandomCropVanilla(Random random)
	{
		ArrayList<Block> crops = new ArrayList<Block>();

		crops.add(Blocks.wheat);
		crops.add(Blocks.carrots);
		crops.add(Blocks.potatoes);

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

	@SuppressWarnings("rawtypes")
	public static ComponentVillageCustomField buildComponent(Start par0Start, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
	{
		StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 4, 9, par6);
		return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(par1List, var8) == null ? new ComponentVillageCustomField(par0Start, par7, par2Random, var8, par6) : null;
	}

	private fieldType randomFieldType(BiomeGenBase biome, Random random) {

		int normalWeight = IguanaConfig.fieldNormalWeight;
		int reedWeight = IguanaConfig.fieldReedWeight;
		int stemWeight = IguanaConfig.fieldStemWeight;

		if (BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE) || BiomeDictionary.isBiomeOfType(biome, Type.SWAMP)) {
			reedWeight *= 2;
			stemWeight *= 2;
		}

		int weights = normalWeight + reedWeight + stemWeight;
		int rnd = random.nextInt(weights);

		if (rnd < reedWeight) return fieldType.REED;
		else if (rnd < reedWeight + stemWeight) return fieldType.STEM;
		else return fieldType.NORMAL;

	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
	 * the end, it adds Fences...
	 */
	@Override
	public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
	{
		if (averageGroundLevel < 0)
		{
			averageGroundLevel = getAverageGroundLevel(par1World, par3StructureBoundingBox);
			if (averageGroundLevel < 0) return true;
			boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + 4 - 1, 0);
		}

		int x = par3StructureBoundingBox.getCenterX();
		int z = par3StructureBoundingBox.getCenterZ();

		BiomeGenBase biome = par1World.getBiomeGenForCoords(x, z);

		typeA = randomFieldType(biome, par2Random);
		typeB = randomFieldType(biome, par2Random);

		Block blockOutA = Blocks.farmland;
		Block blockInA = Blocks.flowing_water;
		Block blockOutB = Blocks.farmland;
		Block blockInB = Blocks.flowing_water;

		Block crop1A;
		Block crop2A;
		Block crop1B;
		Block crop2B;

		if (typeA == fieldType.REED) {
			if (BiomeDictionary.isBiomeOfType(biome, Type.SANDY))
				blockOutA = Blocks.sand;
			else
				blockOutA = Blocks.dirt;
			crop1A = Blocks.reeds;
			crop2A = Blocks.reeds;
		} else if (typeA == fieldType.STEM) {
			crop1A = getRandomStemCrop(par2Random);
			crop2A = getRandomStemCrop(par2Random);
		} else {
			crop1A = getRandomCrop(par2Random);
			crop2A = getRandomCrop(par2Random);
		}

		if (typeB == fieldType.REED) {
			if (BiomeDictionary.isBiomeOfType(biome, Type.SANDY))
				blockOutB = Blocks.sand;
			else
				blockOutB = Blocks.dirt;
			crop1B = Blocks.reeds;
			crop2B = Blocks.reeds;
		} else if (typeA == fieldType.STEM) {
			crop1B = getRandomStemCrop(par2Random);
			crop2B = getRandomStemCrop(par2Random);
		} else {
			crop1B = getRandomCrop(par2Random);
			crop2B = getRandomCrop(par2Random);
		}

		int cropMeta1A = getCropMeta(crop1A, par2Random);
		int cropMeta2A = getCropMeta(crop2A, par2Random);
		int cropMeta1B = getCropMeta(crop1B, par2Random);
		int cropMeta2B = getCropMeta(crop2B, par2Random);

		//BASE
		fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 12, 4, 8, Blocks.air, Blocks.air, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Blocks.log, Blocks.log, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Blocks.log, Blocks.log, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 12, 0, 0, 12, 0, 8, Blocks.log, Blocks.log, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 11, 0, 0, Blocks.log, Blocks.log, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 11, 0, 8, Blocks.log, Blocks.log, false);

		//A
		fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, blockOutA, blockOutA, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, blockInA, blockInA, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, blockOutA, blockOutA, false);

		//B
		fillWithBlocks(par1World, par3StructureBoundingBox, 7, 0, 1, 8, 0, 7, blockOutB, blockOutB, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 1, 9, 0, 7, blockInB, blockInB, false);
		fillWithBlocks(par1World, par3StructureBoundingBox, 10, 0, 1, 11, 0, 7, blockOutB, blockOutB, false);

		//CROPS
		for (int tmp = 1; tmp <= 7; tmp++)
		{
			if (typeA == fieldType.NORMAL) placeCropAtCurrentPosition(par1World, crop1A, cropMeta1A, 1, 1, tmp, par3StructureBoundingBox);
			placeCropAtCurrentPosition(par1World, crop1A, cropMeta1A, 2, 1, tmp, par3StructureBoundingBox);
			placeCropAtCurrentPosition(par1World, crop2A, cropMeta2A, 4, 1, tmp, par3StructureBoundingBox);
			if (typeA == fieldType.NORMAL) placeCropAtCurrentPosition(par1World, crop2A, cropMeta2A, 5, 1, tmp, par3StructureBoundingBox);
			if (typeA == fieldType.NORMAL) placeCropAtCurrentPosition(par1World, crop1B, cropMeta1B, 7, 1, tmp, par3StructureBoundingBox);
			placeCropAtCurrentPosition(par1World, crop1B, cropMeta1B, 8, 1, tmp, par3StructureBoundingBox);
			placeCropAtCurrentPosition(par1World, crop2B, cropMeta2B, 10, 1, tmp, par3StructureBoundingBox);
			if (typeA == fieldType.NORMAL) placeCropAtCurrentPosition(par1World, crop2B, cropMeta2B, 11, 1, tmp, par3StructureBoundingBox);
		}

		//FILLER
		for (int var4 = 0; var4 < 9; ++var4)
			for (int var5 = 0; var5 < 13; ++var5)
			{
				clearCurrentPositionBlocksUpwards(par1World, var5, 4, var4, par3StructureBoundingBox);
				func_151554_b(par1World, Blocks.dirt, 0, var5, -1, var4, par3StructureBoundingBox);
			}

		return true;
	}

	private void placeCropAtCurrentPosition(World world, Block block, int meta, int offsetX, int offsetY, int offsetZ, StructureBoundingBox box)
	{
		if (Loader.isModLoaded("harvestcraft"))
			placeHarvestCraftCropAtCurrentPosition(world, block, meta, offsetX, offsetY, offsetZ, box);
		else
			placeBlockAtCurrentPosition(world, block, meta, offsetX, offsetY, offsetZ, box);
	}

	private void placeHarvestCraftCropAtCurrentPosition(World world, Block block, int meta, int offsetX, int offsetY, int offsetZ, StructureBoundingBox box)
	{
		int x = getXWithOffset(offsetX, offsetZ);
		int y = getYWithOffset(offsetY);
		int z = getZWithOffset(offsetX, offsetZ);

		if (box.isVecInside(x, y, z))
		{
			world.setBlock(x, y, z, block, meta, 2);
		}
	}
}
