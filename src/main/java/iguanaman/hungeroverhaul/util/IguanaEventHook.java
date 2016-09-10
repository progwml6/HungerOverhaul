package iguanaman.hungeroverhaul.util;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.module.ModuleGrassSeeds;
import iguanaman.hungeroverhaul.module.ModulePlantGrowth;
import iguanaman.hungeroverhaul.module.PamsModsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.progwml6.natura.blocks.crops.CropBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;

import com.pam.harvestcraft.ItemPamSeedFood;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IguanaEventHook
{
    private static long lastRightClickCrop = 0;
    public static ItemAndBlockList rightClickHarvestBlacklist = new ItemAndBlockList();
    public static ItemAndBlockList harvestDropsBlacklist = new ItemAndBlockList();
    static
    {
        if (Loader.isModLoaded("ExtraUtilities"))
        {
            Block enderLilly = Block.getBlockFromName("ExtraUtilities:plant/ender_lilly");
            if (enderLilly != null)
            {
                rightClickHarvestBlacklist.add(enderLilly);
                harvestDropsBlacklist.add(enderLilly);
            }
        }

        if (Loader.isModLoaded("ThaumicTinkerer"))
        {
            Block infusedGrain = Block.getBlockFromName("ThaumicTinkerer:infusedGrainBlock");
            if (infusedGrain != null)
            {
                harvestDropsBlacklist.add(infusedGrain);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event)
    {

        Random rand = new Random();

        // Slow growth and egg rates
        if (event.entityLiving instanceof EntityAnimal)
        {
            float rndBreed = RandomHelper.nextFloat(rand, IguanaConfig.breedingTimeoutMultiplier);
            float rndChild = RandomHelper.nextFloat(rand, IguanaConfig.childDurationMultiplier);
            EntityAgeable ageable = (EntityAgeable) event.entityLiving;
            int growingAge = ageable.getGrowingAge();
            if (growingAge > 0 && rndBreed >= 1)
                ageable.setGrowingAge(++growingAge);
            else if (growingAge < 0 && rndChild >= 1)
                ageable.setGrowingAge(--growingAge);

            if (IguanaConfig.eggTimeoutMultiplier > 1 && event.entityLiving instanceof EntityChicken)
            {
                float rnd = RandomHelper.nextFloat(rand, IguanaConfig.eggTimeoutMultiplier);
                EntityChicken chicken = (EntityChicken) event.entityLiving;
                if (chicken.timeUntilNextEgg > 0 && rnd >= 1)
                    chicken.timeUntilNextEgg += 1;
            }

            // Reduced milked value every second
            if (IguanaConfig.milkedTimeout > 0 && event.entityLiving instanceof EntityCow && event.entityLiving.worldObj.getTotalWorldTime() % 20 == 0)
            {
                NBTTagCompound tags = event.entityLiving.getEntityData();
                if (tags.hasKey("Milked"))
                {
                    int milked = tags.getInteger("Milked");
                    if (--milked <= 0)
                        tags.removeTag("Milked");
                    else
                        tags.setInteger("Milked", milked);
                }
            }
        }

        if (!event.entityLiving.worldObj.isRemote)
        {
            NBTTagCompound tags = event.entityLiving.getEntityData();

            // low stat effects
            if (tags.hasKey("HungerOverhaulCheck"))
            {
                int lastCheck = tags.getInteger("HungerOverhaulCheck");
                if (--lastCheck <= 0)
                    tags.removeTag("HungerOverhaulCheck");
                else
                    tags.setInteger("HungerOverhaulCheck", lastCheck);
            }
            else
            {
                float healthPercent = event.entityLiving.getHealth() / event.entityLiving.getMaxHealth();
                int foodLevel = 20;
                boolean creative = false;
                boolean isPlayer = false;
                if (event.entityLiving instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) event.entityLiving;
                    creative = player.capabilities.isCreativeMode;
                    foodLevel = player.getFoodStats().getFoodLevel();
                    isPlayer = true;
                }
                else
                    healthPercent /= 2;

                if (event.entityLiving instanceof EntityPlayer && IguanaConfig.constantHungerLoss)
                {
                    EntityPlayer player = (EntityPlayer) event.entityLiving;
                    if (!player.capabilities.isCreativeMode && !player.isDead)
                        player.addExhaustion(0.01F);
                }

                if (IguanaConfig.addLowStatEffects)
                {
                    int difficultyModifierEffects = 2;
                    if (IguanaConfig.difficultyScalingEffects && event.entityLiving.worldObj.difficultySetting != null)
                    {
                        difficultyModifierEffects = event.entityLiving.worldObj.difficultySetting.getDifficultyId();

                        if (!(event.entityLiving instanceof EntityPlayer))
                            difficultyModifierEffects = difficultyModifierEffects * -1 + 3;
                    }

                    // low stat effects
                    if (!creative && isPlayer && !event.entityLiving.isDead && healthPercent > 0f)
                    {

                        if (IguanaConfig.addLowHealthSlowness || IguanaConfig.addLowHungerSlowness)
                            if ((IguanaConfig.addLowHungerSlowness && foodLevel <= 1) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.05F))
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, 1 + difficultyModifierEffects, true));
                            else if ((IguanaConfig.addLowHungerSlowness && foodLevel <= 2) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.10F))
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 3) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.15F)) && difficultyModifierEffects >= 1)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, -1 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 4) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.20F)) && difficultyModifierEffects >= 2)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, -2 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 5) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.25F)) && difficultyModifierEffects >= 3)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, -3 + difficultyModifierEffects, true));

                        if (IguanaConfig.addLowHealthMiningSlowdown || IguanaConfig.addLowHungerMiningSlowdown)
                            if ((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 1) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.05F))
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, 1 + difficultyModifierEffects, true));
                            else if ((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 2) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.10F))
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 3) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.15F)) && difficultyModifierEffects >= 1)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, -1 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 4) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.20F)) && difficultyModifierEffects >= 2)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, -2 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 5) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.25F)) && difficultyModifierEffects >= 3)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, -3 + difficultyModifierEffects, true));

                        if (IguanaConfig.addLowHealthWeakness || IguanaConfig.addLowHungerWeakness)
                            //Weakness effect
                            if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 1) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.05F)) && difficultyModifierEffects >= 1)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 19, -1 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 2) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.10F)) && difficultyModifierEffects >= 2)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 19, -2 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 3) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.15F)) && difficultyModifierEffects >= 3)
                                event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 19, -3 + difficultyModifierEffects, true));

                        if ((IguanaConfig.addLowHungerNausea && foodLevel <= 1) || (IguanaConfig.addLowHealthNausea && healthPercent <= 0.05F))
                            event.entityLiving.addPotionEffect(new PotionEffect(Potion.confusion.id, 19, 0, true));
                    }
                }

                tags.setInteger("HungerOverhaulCheck", 9);
            }
        }
    }

    @SubscribeEvent
    public void onUseHoe(UseHoeEvent event)
    {
        if (IguanaConfig.modifyHoeUse)
        {
            Block block = event.world.getBlock(event.x, event.y, event.z);

            if ((block == Blocks.dirt || block == Blocks.grass) && isWaterNearby(event.world, event.x, event.y, event.z))
            {
                if (IguanaConfig.hoeToolDamageMultiplier > 1)
                    event.current.damageItem(IguanaConfig.hoeToolDamageMultiplier - 1, event.entityPlayer);
            }
            else if (block == Blocks.grass && !isWaterNearby(event.world, event.x, event.y, event.z))
            {

                Block block1 = Blocks.farmland;
                event.world.playSoundEffect(event.x + 0.5F, event.y + 0.5F, event.z + 0.5F, block1.stepSound.soundName, (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
                if (!event.world.isRemote && IguanaConfig.seedChance > 0)
                {
                    int seedChance = IguanaConfig.seedChance;
                    if (event.world.difficultySetting.getDifficultyId() < 2)
                        seedChance *= 2;
                    else if (event.world.difficultySetting.getDifficultyId() == 3)
                        seedChance = Math.max(Math.round(seedChance / 2f), 1);

                    if (event.world.rand.nextInt(100) <= seedChance)
                    {
                        ItemStack seed = ModuleGrassSeeds.getSeedFromTillingGrass(event.world);
                        if (seed != null)
                            block.dropBlockAsItem(event.world, event.x, event.y, event.z, seed);
                    }
                    event.world.setBlock(event.x, event.y, event.z, Blocks.dirt);
                }

                if (IguanaConfig.hoeToolDamageMultiplier > 1)
                    event.current.damageItem(IguanaConfig.hoeToolDamageMultiplier - 1, event.entityPlayer);
                event.setResult(Result.ALLOW);
            }
            else
                event.setCanceled(true);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event)
    {
        if (IguanaConfig.addGuiText)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.thePlayer;

            if (!player.isDead && !player.capabilities.isCreativeMode && !mc.gameSettings.showDebugInfo)
            {

                float healthPercent = player.getHealth() / player.getMaxHealth();

                if (healthPercent <= 0.15F)
                    event.left.add(EnumChatFormatting.RED + StatCollector.translateToLocal("hungeroverhaul.dying") + EnumChatFormatting.RESET);
                else if (healthPercent <= 0.3F)
                    event.left.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("hungeroverhaul.injured") + EnumChatFormatting.RESET);
                else if (healthPercent < 0.5F)
                    event.left.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("hungeroverhaul.hurt") + EnumChatFormatting.RESET);

                if (player.getFoodStats().getFoodLevel() <= 6)
                    event.right.add(EnumChatFormatting.RED + StatCollector.translateToLocal("hungeroverhaul.starving") + EnumChatFormatting.RESET);
                else if (player.getFoodStats().getFoodLevel() <= 10)
                    event.right.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("hungeroverhaul.hungry") + EnumChatFormatting.RESET);
                else if (player.getFoodStats().getFoodLevel() <= 14)
                    event.right.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("hungeroverhaul.peckish") + EnumChatFormatting.RESET);
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteractEvent(EntityInteractEvent event)
    {
        if (IguanaConfig.milkedTimeout > 0 && event.entityPlayer != null && event.target != null && event.target instanceof EntityCow)
        {
            EntityCow cow = (EntityCow) event.target;
            EntityPlayer player = event.entityPlayer;
            ItemStack equipped = player.getCurrentEquippedItem();
            if (equipped != null && equipped.getItem() != null)
            {
                Item item = equipped.getItem();
                if (item instanceof ItemBucket && ((ItemBucket) item).isFull == Blocks.air || cow instanceof EntityMooshroom && item == Items.bowl)
                {
                    NBTTagCompound tags = cow.getEntityData();
                    if (tags.hasKey("Milked"))
                    {
                        event.setCanceled(true);
                        if (!player.worldObj.isRemote)
                            cow.playSound("mob.cow.hurt", 0.4F, (event.entity.worldObj.rand.nextFloat() - event.entity.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
                    }
                    else
                        tags.setInteger("Milked", IguanaConfig.milkedTimeout * 60);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurtEvent(LivingHurtEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            AppleCoreAPI.mutator.setHealthRegenTickCounter(player, 0);
        }
    }

    @SubscribeEvent
    public void onPlayerInteraction(PlayerInteractEvent event)
    {
        // slightly hacky workaround:
        // if RIGHT_CLICK_BLOCK is canceled or useItem == Result.DENY, then
        // the right click falls through to RIGHT_CLICK_AIR. To correctly cancel the RIGHT_CLICK_AIR,
        // we need to make sure that it is happening on the same tick that the right click was performed
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR && lastRightClickCrop == event.world.getTotalWorldTime())
        {
            event.setCanceled(true);
        }
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            return;

        // unplantable harvestcraft foods
        if (IguanaConfig.foodsUnplantable && Loader.isModLoaded("harvestcraft") && event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof ItemPamSeedFood)
        {
            if (event.world.isRemote)
            {
                // hacky workaround:
                // we need to make the client aware that this is disallowed,
                // but the client will ignore event.useItem = Result.DENY, and
                // setCanceled will stop the packet from getting sent to the server,
                // so we have to manually detect whether or not we are trying to
                // plant the food and only cancel it then, otherwise you won't be
                // able to activate any blocks with an ItemPamSeedFood in your hand
                if (PamsModsHelper.canPlantSeedFoodAt(event.entityPlayer, event.entityPlayer.getHeldItem(), event.world, event.x, event.y, event.z, event.face))
                {
                    event.setCanceled(true);
                }
            }
            else
                event.useItem = Result.DENY;
            return;
        }

        // right-click to harvest
        if (!IguanaConfig.enableRightClickHarvesting)
            return;

        Block clicked = event.world.getBlock(event.x, event.y, event.z);
        int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
        int resultingMeta = -1;

        // certain things we don't want to add right-click harvest support for
        if (rightClickHarvestBlacklist.contains(clicked))
            return;

        if (Loader.isModLoaded("Natura") && clicked instanceof CropBlock)
        {
            if (meta == 3 || meta == 8)
                resultingMeta = meta == 3 ? 0 : 4;
        }
        else if (clicked instanceof BlockCrops && meta >= 7)
        {
            resultingMeta = 0;
        }

        if (resultingMeta >= 0)
        {
            // BlockEvent.HarvestDropsEvent gets fired from within this function
            // therefore, the drops will be modified by our onBlockHarvested method
            // but we re-modify them using the right-click specific config options
            if (!event.world.isRemote && !event.world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
            {
                ArrayList<ItemStack> drops = clicked.getDrops(event.world, event.x, event.y, event.z, meta, 0);
                float odds = ForgeEventFactory.fireBlockHarvesting(drops, event.world, clicked, event.x, event.y, event.z, meta, 0, 1.0f, false, event.entityPlayer);

                List<ItemStack> modifiedDrops = BlockHelper.modifyCropDrops(drops, clicked, meta, IguanaConfig.seedsPerHarvestRightClickMin, IguanaConfig.seedsPerHarvestRightClickMax, IguanaConfig.producePerHarvestRightClickMin, IguanaConfig.producePerHarvestRightClickMax);

                for (ItemStack drop : modifiedDrops)
                {
                    clicked.dropBlockAsItem(event.world, event.x, event.y, event.z, drop);
                }
            }

            event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, resultingMeta, 2);

            lastRightClickCrop = event.world.getTotalWorldTime();

            // hacky workaround:
            // if the client deems it is unable to place the block that is held,
            // the right click packet will not be sent to the server at all
            // so, instead, we have to manually send the packet and then
            // cancel the event (canceling the event will stop the client from
            // doing any further processing)
            //
            // this fixes client desyncs when right clicking a mature crop
            // while holding an ItemBlock; the crop will get reset on the client
            // but the packet wouldn't get sent to the server because of the above
            // so it would remain unharvested on the server
            if (event.world.isRemote)
            {
                ClientHelper.sendRightClickPacket(event.x, event.y, event.z, event.face, event.entityPlayer.inventory.getCurrentItem(), 0f, 0f, 0f);
                event.setCanceled(true);
            }
            else
                event.useItem = Result.DENY;
        }
    }

    @SubscribeEvent
    public void onBlockHarvested(BlockEvent.HarvestDropsEvent event)
    {
        if (!IguanaConfig.modifyCropDropsBreak)
            return;

        // certain things we don't want to modify the drops of
        if (IguanaEventHook.harvestDropsBlacklist.contains(event.block))
            return;

        boolean isNaturaCrop = Loader.isModLoaded("Natura") && event.block instanceof CropBlock;
        boolean eligable = isNaturaCrop || event.block instanceof BlockCrops;

        if (!eligable)
            return;

        boolean fullyGrown = (!isNaturaCrop && event.blockMetadata >= 7) || (isNaturaCrop && event.blockMetadata == 3 || event.blockMetadata == 8);

        if (!fullyGrown)
            return;

        List<ItemStack> modifiedDrops = BlockHelper.modifyCropDrops(event.drops, event.block, event.blockMetadata, IguanaConfig.seedsPerHarvestBreakMin, IguanaConfig.seedsPerHarvestBreakMax, IguanaConfig.producePerHarvestBreakMin, IguanaConfig.producePerHarvestBreakMax);
        event.drops.clear();
        for (ItemStack drop : modifiedDrops)
        {
            event.drops.add(drop);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderTooltips(ItemTooltipEvent event)
    {
        if (IguanaConfig.addFoodTooltips && AppleCoreAPI.accessor.isFood(event.itemStack))
        {
            FoodValues values = FoodValues.get(event.itemStack);
            int hungerFill = values.hunger;
            float satiation = values.saturationModifier * 20 - hungerFill;

            String mealDescriptor = "";
            String noun;
            String adjective = null;

            if (hungerFill <= 1)
                noun = "morsel";
            else if (hungerFill <= 2)
                noun = "snack";
            else if (hungerFill <= 5)
                noun = "lightmeal";
            else if (hungerFill <= 8)
                noun = "meal";
            else if (hungerFill <= 11)
                noun = "largemeal";
            else
                noun = "feast";

            if (satiation >= 3.0F)
                adjective = "hearty";
            else if (satiation >= 2.0F)
                adjective = "wholesome";
            else if (satiation > 0.0F)
                adjective = "nourishing";
            else if (satiation < 0.0F)
                adjective = "unfulfilling";

            if (adjective != null && StatCollector.canTranslate("hungeroverhaul." + adjective + "." + noun))
            {
                mealDescriptor = StatCollector.translateToLocal("hungeroverhaul." + adjective + "." + noun);
            }
            else
            {
                mealDescriptor = StatCollector.translateToLocal("hungeroverhaul." + noun);
                if (adjective != null)
                {
                    mealDescriptor = StatCollector.translateToLocalFormatted(StatCollector.translateToLocal("hungeroverhaul." + adjective), mealDescriptor);
                }
            }

            int topIndex = event.toolTip.size() > 0 ? 1 : 0;
            event.toolTip.add(topIndex, mealDescriptor.substring(0, 1).toUpperCase() + mealDescriptor.substring(1));
        }
        if (IguanaConfig.wrongBiomeRegrowthMultiplier > 1)
        {
            PlantGrowthModification growthModification = null;
            if (event.itemStack.getItem() instanceof IPlantable)
            {
                growthModification = ModulePlantGrowth.getPlantGrowthModification(((IPlantable) event.itemStack.getItem()).getPlant(event.entityPlayer.worldObj, 0, 0, 0));
            }
            else if (event.itemStack.getItem() instanceof ItemBlock)
            {
                Block block = Block.getBlockFromItem(event.itemStack.getItem());
                if (block != null)
                    growthModification = ModulePlantGrowth.getPlantGrowthModification(block);
            }
            else
            {
                Block block = PamsModsHelper.fruitItemToBlockMap.get(event.itemStack.getItem());
                if (block != null)
                    growthModification = ModulePlantGrowth.getPlantGrowthModification(block);
            }

            if (growthModification != null && !growthModification.biomeGrowthModifiers.isEmpty())
            {
                String tooltip = "";
                for (BiomeDictionary.Type biomeType : growthModification.biomeGrowthModifiers.keySet())
                    tooltip += biomeType.toString().substring(0, 1).toUpperCase() + biomeType.toString().substring(1).toLowerCase() + ", ";
                event.toolTip.add(StatCollector.translateToLocal("hungeroverhaul.crop.grows.best.in"));
                event.toolTip.add(tooltip.substring(0, tooltip.length() - 2));
            }
        }
    }

    /**
     * returns true if there's water nearby (x-4 to x+4, y to y+1, k-4 to k+4)
     */
    private boolean isWaterNearby(World par1World, int par2, int par3, int par4)
    {
        for (int l = par2 - 4; l <= par2 + 4; ++l)
            for (int i1 = par3; i1 <= par3 + 1; ++i1)
                for (int j1 = par4 - 4; j1 <= par4 + 4; ++j1)
                    if (par1World.getBlock(l, i1, j1).getMaterial() == Material.water)
                        return true;

        return false;
    }
}
