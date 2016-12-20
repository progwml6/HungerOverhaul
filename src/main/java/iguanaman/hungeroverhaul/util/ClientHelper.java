package iguanaman.hungeroverhaul.util;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHelper
{
    public static void sendRightClickPacket(BlockPos pos, EnumFacing face, EnumHand hand, float facingXIn, float facingYIn, float facingZIn)
    {
        FMLClientHandler.instance().getClientPlayerEntity().connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, face, hand, facingXIn, facingYIn, facingZIn));
    }
}
