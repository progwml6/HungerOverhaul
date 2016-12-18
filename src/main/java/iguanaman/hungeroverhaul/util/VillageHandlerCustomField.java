package iguanaman.hungeroverhaul.util;

import java.util.List;
import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class VillageHandlerCustomField implements IVillageCreationHandler
{

    public VillageHandlerCustomField()
    {
    }

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int i)
    {
        return new PieceWeight(ComponentVillageCustomField.class, 12, MathHelper.getInt(random, 1 + i, 2 + i));
    }

    @Override
    public Class<?> getComponentClass()
    {
        return ComponentVillageCustomField.class;
    }

    @Override
    public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces, Random random, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int type)
    {
        return ComponentVillageCustomField.createPiece(startPiece, pieces, random, structureMinX, structureMinY, structureMinZ, facing, type);
    }
}
