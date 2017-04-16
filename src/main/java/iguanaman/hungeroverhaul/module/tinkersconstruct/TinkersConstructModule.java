package iguanaman.hungeroverhaul.module.tinkersconstruct;

public class TinkersConstructModule
{

}
// DISABLE UNTIL TCON IS UPDATED TODO: RE-ENABLE
/*import iguanaman.hungeroverhaul.common.config.Config;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.shared.TinkerCommons;

public class TinkersConstructModule
{
    public static void init()
    {
        if (Config.dryingRackTimeMultiplier != 1)
        {
            int dryingTime = 20 * 60 * 20; //in minutes

            if (TConstruct.pulseManager.isPulseLoaded(TinkerGadgets.PulseId))
            {
                NBTTagCompound rottonFleshTag = new NBTTagCompound();
                rottonFleshTag.setTag("input", new ItemStack(Items.ROTTEN_FLESH).writeToNBT(new NBTTagCompound()));
                rottonFleshTag.setTag("output", TinkerCommons.jerkyMonster.writeToNBT(new NBTTagCompound()));
                rottonFleshTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", rottonFleshTag);

                NBTTagCompound beefTag = new NBTTagCompound();
                beefTag.setTag("input", new ItemStack(Items.BEEF).writeToNBT(new NBTTagCompound()));
                beefTag.setTag("output", TinkerCommons.jerkyBeef.writeToNBT(new NBTTagCompound()));
                beefTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", beefTag);

                NBTTagCompound chickenTag = new NBTTagCompound();
                chickenTag.setTag("input", new ItemStack(Items.CHICKEN).writeToNBT(new NBTTagCompound()));
                chickenTag.setTag("output", TinkerCommons.jerkyChicken.writeToNBT(new NBTTagCompound()));
                chickenTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", chickenTag);

                NBTTagCompound porkchopTag = new NBTTagCompound();
                porkchopTag.setTag("input", new ItemStack(Items.PORKCHOP).writeToNBT(new NBTTagCompound()));
                porkchopTag.setTag("output", TinkerCommons.jerkyPork.writeToNBT(new NBTTagCompound()));
                porkchopTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", porkchopTag);

                NBTTagCompound muttonTag = new NBTTagCompound();
                muttonTag.setTag("input", new ItemStack(Items.MUTTON).writeToNBT(new NBTTagCompound()));
                muttonTag.setTag("output", TinkerCommons.jerkyMutton.writeToNBT(new NBTTagCompound()));
                muttonTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", muttonTag);

                NBTTagCompound rabbitTag = new NBTTagCompound();
                rabbitTag.setTag("input", new ItemStack(Items.RABBIT).writeToNBT(new NBTTagCompound()));
                rabbitTag.setTag("output", TinkerCommons.jerkyRabbit.writeToNBT(new NBTTagCompound()));
                rabbitTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", rabbitTag);

                NBTTagCompound fishTag = new NBTTagCompound();
                fishTag.setTag("input", new ItemStack(Items.FISH, 1, 0).writeToNBT(new NBTTagCompound()));
                fishTag.setTag("output", TinkerCommons.jerkyFish.writeToNBT(new NBTTagCompound()));
                fishTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", fishTag);

                NBTTagCompound salmonTag = new NBTTagCompound();
                salmonTag.setTag("input", new ItemStack(Items.FISH, 1, 1).writeToNBT(new NBTTagCompound()));
                salmonTag.setTag("output", TinkerCommons.jerkySalmon.writeToNBT(new NBTTagCompound()));
                salmonTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", salmonTag);

                NBTTagCompound clownfishTag = new NBTTagCompound();
                clownfishTag.setTag("input", new ItemStack(Items.FISH, 1, 1).writeToNBT(new NBTTagCompound()));
                clownfishTag.setTag("output", TinkerCommons.jerkyClownfish.writeToNBT(new NBTTagCompound()));
                clownfishTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", clownfishTag);

                NBTTagCompound pufferTag = new NBTTagCompound();
                pufferTag.setTag("input", new ItemStack(Items.FISH, 1, 1).writeToNBT(new NBTTagCompound()));
                pufferTag.setTag("output", TinkerCommons.jerkyClownfish.writeToNBT(new NBTTagCompound()));
                pufferTag.setInteger("time", dryingTime);
                FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", pufferTag);
            }
        }
    }
}*/
