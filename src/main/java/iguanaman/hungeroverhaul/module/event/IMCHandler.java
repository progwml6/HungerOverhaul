package iguanaman.hungeroverhaul.module.event;

import com.google.common.collect.ImmutableList;
import iguanaman.hungeroverhaul.HungerOverhaul;
import iguanaman.hungeroverhaul.library.ItemAndBlockList;
import iguanaman.hungeroverhaul.module.food.FoodModifier;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCHandler
{
    public static String BLACKLIST_RIGHT_CLICK = "BlacklistRightClick";

    public static String BLACKLIST_DROPS = "BlacklistDrops";

    public static String BLACKLIST_FOOD = "BlacklistFood";

    public static void processMessages(ImmutableList<IMCMessage> messages)
    {
        for (final IMCMessage message : messages)
        {
            ItemAndBlockList blacklist = null;

            if (message.key.equalsIgnoreCase(BLACKLIST_RIGHT_CLICK))
            {
                blacklist = HungerOverhaulEventHook.rightClickHarvestBlacklist;
            }
            else if (message.key.equalsIgnoreCase(BLACKLIST_DROPS))
            {
                blacklist = HungerOverhaulEventHook.harvestDropsBlacklist;
            }
            else if (message.key.equalsIgnoreCase(BLACKLIST_FOOD))
            {
                blacklist = FoodModifier.blacklist;
            }

            if (blacklist != null)
            {
                if (message.isItemStackMessage() && message.getItemStackValue() != null)
                {
                    blacklist.add(message.getItemStackValue());
                }
                else if (message.isStringMessage() && message.getStringValue() != null)
                {
                    try
                    {
                        blacklist.add(message.getStringValue());
                    }
                    catch (ClassNotFoundException e)
                    {
                        HungerOverhaul.log.error("Class to blacklist not found (IMC sent by mod " + message.getSender() + ")");

                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
