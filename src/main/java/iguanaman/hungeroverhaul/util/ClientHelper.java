package iguanaman.hungeroverhaul.util;

import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHelper
{
    public static void sendRightClickPacket(int x, int y, int z, int side, ItemStack currentItem, float hitX, float hitY, float hitZ)
    {
        FMLClientHandler.instance().getClientPlayerEntity().sendQueue.addToSendQueue(new CPacketPlayerTryUseItem(x, y, z, side, currentItem, hitX, hitY, hitZ));
    }
}
