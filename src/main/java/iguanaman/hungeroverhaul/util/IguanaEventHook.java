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
import net.minecraftforge.event.world.BlockEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;

import com.pam.harvestcraft.item.items.ItemPamSeedFood;

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
        if (event.getEntityLiving() instanceof EntityAnimal)
        {
            float rndBreed = RandomHelper.nextFloat(rand, IguanaConfig.breedingTimeoutMultiplier);
            float rndChild = RandomHelper.nextFloat(rand, IguanaConfig.childDurationMultiplier);
            EntityAgeable ageable = (EntityAgeable) event.getEntityLiving();
            int growingAge = ageable.getGrowingAge();
            if (growingAge > 0 && rndBreed >= 1)
                ageable.setGrowingAge(++growingAge);
            else if (growingAge < 0 && rndChild >= 1)
                ageable.setGrowingAge(--growingAge);

            if (IguanaConfig.eggTimeoutMultiplier > 1 && event.getEntityLiving() instanceof EntityChicken)
            {
                float rnd = RandomHelper.nextFloat(rand, IguanaConfig.eggTimeoutMultiplier);
                EntityChicken chicken = (EntityChicken) event.getEntityLiving();
                if (chicken.timeUntilNextEgg > 0 && rnd >= 1)
                    chicken.timeUntilNextEgg += 1;
            }

            // Reduced milked value every second
            if (IguanaConfig.milkedTimeout > 0 && event.getEntityLiving() instanceof EntityCow && event.getEntityLiving().worldObj.getTotalWorldTime() % 20 == 0)
            {
                NBTTagCompound tags = event.getEntityLiving().getEntityData();
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

        if (!event.getEntityLiving().worldObj.isRemote)
        {
            NBTTagCompound tags = event.getEntityLiving().getEntityData();

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
                float healthPercent = event.getEntityLiving().getHealth() / event.getEntityLiving().getMaxHealth();
                int foodLevel = 20;
                boolean creative = false;
                boolean isPlayer = false;
                if (event.getEntityLiving() instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                    creative = player.capabilities.isCreativeMode;
                    foodLevel = player.getFoodStats().getFoodLevel();
                    isPlayer = true;
                }
                else
                    healthPercent /= 2;

                if (event.getEntityLiving() instanceof EntityPlayer && IguanaConfig.constantHungerLoss)
                {
                    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                    if (!player.capabilities.isCreativeMode && !player.isDead)
                        player.addExhaustion(0.01F);
                }

                if (IguanaConfig.addLowStatEffects)
                {
                    int difficultyModifierEffects = 2;
                    if (IguanaConfig.difficultyScalingEffects && event.getEntityLiving().worldObj.getDifficulty() != null)
                    {
                        difficultyModifierEffects = event.getEntityLiving().worldObj.getDifficulty().getDifficultyId();

                        if (!(event.getEntityLiving() instanceof EntityPlayer))
                            difficultyModifierEffects = difficultyModifierEffects * -1 + 3;
                    }

                    // low stat effects
                    if (!creative && isPlayer && !event.getEntityLiving().isDead && healthPercent > 0f)
                    {

                        if (IguanaConfig.addLowHealthSlowness || IguanaConfig.addLowHungerSlowness)
                            if ((IguanaConfig.addLowHungerSlowness && foodLevel <= 1) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.05F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, 1 + difficultyModifierEffects, true));
                            else if ((IguanaConfig.addLowHungerSlowness && foodLevel <= 2) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.10F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 3) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.15F)) && difficultyModifierEffects >= 1)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, -1 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 4) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.20F)) && difficultyModifierEffects >= 2)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, -2 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 5) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.25F)) && difficultyModifierEffects >= 3)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 19, -3 + difficultyModifierEffects, true));

                        if (IguanaConfig.addLowHealthMiningSlowdown || IguanaConfig.addLowHungerMiningSlowdown)
                            if ((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 1) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.05F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, 1 + difficultyModifierEffects, true));
                            else if ((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 2) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.10F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 3) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.15F)) && difficultyModifierEffects >= 1)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, -1 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 4) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.20F)) && difficultyModifierEffects >= 2)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, -2 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 5) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.25F)) && difficultyModifierEffects >= 3)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 19, -3 + difficultyModifierEffects, true));

                        if (IguanaConfig.addLowHealthWeakness || IguanaConfig.addLowHungerWeakness)
                            //Weakness effect
                            if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 1) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.05F)) && difficultyModifierEffects >= 1)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.weakness.id, 19, -1 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 2) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.10F)) && difficultyModifierEffects >= 2)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.weakness.id, 19, -2 + difficultyModifierEffects, true));
                            else if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 3) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.15F)) && difficultyModifierEffects >= 3)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.weakness.id, 19, -3 + difficultyModifierEffects, true));

                        if ((IguanaConfig.addLowHungerNausea && foodLevel <= 1) || (IguanaConfig.addLowHealthNausea && healthPercent <= 0.05F))
                            event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.confusion.id, 19, 0, true));
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
            Block block = event.getWorld().getBlock(event.getPos());

            if ((block == Blocks.DIRT || block == Blocks.GRASS) && isWaterNearby(event.getWorld(), event.getPos()))
            {
                if (IguanaConfig.hoeToolDamageMultiplier > 1)
                    event.getCurrent().damageItem(IguanaConfig.hoeToolDamageMultiplier - 1, event.entityPlayer);
            }
            else if (block == Blocks.GRASS && !isWaterNearby(event.getWorld(), event.getPos()))
            {

                Block block1 = Blocks.FARMLAND;
                event.getWorld().playSoundEffect(event.x + 0.5F, event.y + 0.5F, event.z + 0.5F, block1.stepSound.soundName, (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
                if (!event.getWorld().isRemote && IguanaConfig.seedChance > 0)
                {
                    int seedChance = IguanaConfig.seedChance;
                    if (event.getWorld().getDifficulty().getDifficultyId() < 2)
                        seedChance *= 2;
                    else if (event.getWorld().getDifficulty().getDifficultyId() == 3)
                        seedChance = Math.max(Math.round(seedChance / 2f), 1);

                    if (event.getWorld().rand.nextInt(100) <= seedChance)
                    {
                        ItemStack seed = ModuleGrassSeeds.getSeedFromTillingGrass(event.getWorld());
                        if (seed != null)
                            block.dropBlockAsItem(event.getWorld(), event.getPos(), seed);
                    }
                    event.getWorld().setBlock(event.getPos(), Blocks.DIRT);
                }

                if (IguanaConfig.hoeToolDamageMultiplier > 1)
                    event.getCurrent().damageItem(IguanaConfig.hoeToolDamageMultiplier - 1, event.getEntityPlayer());
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
                    event.getLeft().add(EnumChatFormatting.RED + StatCollector.translateToLocal("hungeroverhaul.dying") + EnumChatFormatting.RESET);
                else if (healthPercent <= 0.3F)
                    event.getLeft().add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("hungeroverhaul.injured") + EnumChatFormatting.RESET);
                else if (healthPercent < 0.5F)
                    event.getLeft().add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("hungeroverhaul.hurt") + EnumChatFormatting.RESET);

                if (player.getFoodStats().getFoodLevel() <= 6)
                    event.getLeft().add(EnumChatFormatting.RED + StatCollector.translateToLocal("hungeroverhaul.starving") + EnumChatFormatting.RESET);
                else if (player.getFoodStats().getFoodLevel() <= 10)
                    event.getLeft().add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("hungeroverhaul.hungry") + EnumChatFormatting.RESET);
                else if (player.getFoodStats().getFoodLevel() <= 14)
                    event.getLeft().add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("hungeroverhaul.peckish") + EnumChatFormatting.RESET);
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
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
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
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR && lastRightClickCrop == event.getWorld().getTotalWorldTime())
        {
            event.setCanceled(true);
        }
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            return;

        // unplantable harvestcraft foods
        if (IguanaConfig.foodsUnplantable && Loader.isModLoaded("harvestcraft") && event.getEntityPlayer().getHeldItem(event.getHand()) != null && event.getEntityPlayer().getHeldItem(event.getHand()).getItem() instanceof ItemPamSeedFood)
        {
            if (event.getWorld().isRemote)
            {
                // hacky workaround:
                // we need to make the client aware that this is disallowed,
                // but the client will ignore event.useItem = Result.DENY, and
                // setCanceled will stop the packet from getting sent to the server,
                // so we have to manually detect whether or not we are trying to
                // plant the food and only cancel it then, otherwise you won't be
                // able to activate any blocks with an ItemPamSeedFood in your hand
                if (PamsModsHelper.canPlantSeedFoodAt(event.getEntityPlayer(), event.getEntityPlayer().getHeldItem(event.getHand()), event.getWorld(), event.getPos(), event.getFace()))
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

        Block clicked = event.getWorld().getBlock(event.getPos());
        int meta = event.getWorld().getBlockMetadata(event.getPos());
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
            if (!event.getWorld().isRemote && !event.getWorld().restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
            {
                ArrayList<ItemStack> drops = clicked.getDrops(event.getWorld(), event.getPos(), meta, 0);
                float odds = ForgeEventFactory.fireBlockHarvesting(drops, event.getWorld(), clicked, event.getPos(), meta, 0, 1.0f, false, event.getEntityPlayer());

                List<ItemStack> modifiedDrops = BlockHelper.modifyCropDrops(drops, clicked, meta, IguanaConfig.seedsPerHarvestRightClickMin, IguanaConfig.seedsPerHarvestRightClickMax, IguanaConfig.producePerHarvestRightClickMin, IguanaConfig.producePerHarvestRightClickMax);

                for (ItemStack drop : modifiedDrops)
                {
                    clicked.dropBlockAsItem(event.getWorld(), event.getPos(), drop);
                }
            }

            event.getWorld().setBlockMetadataWithNotify(event.getPos(), resultingMeta, 2);

            lastRightClickCrop = event.getWorld().getTotalWorldTime();

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
            if (event.getWorld().isRemote)
            {
                ClientHelper.sendRightClickPacket(event.getPos(), event.getFace(), event.getEntityPlayer().inventory.getCurrentItem(), 0f, 0f, 0f);
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

        List<ItemStack> modifiedDrops = BlockHelper.modifyCropDrops(event.getDrops(), event.block, event.blockMetadata, IguanaConfig.seedsPerHarvestBreakMin, IguanaConfig.seedsPerHarvestBreakMax, IguanaConfig.producePerHarvestBreakMin, IguanaConfig.producePerHarvestBreakMax);
        event.getDrops().clear();
        for (ItemStack drop : modifiedDrops)
        {
            event.getDrops().add(drop);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderTooltips(ItemTooltipEvent event)
    {
        if (IguanaConfig.addFoodTooltips && AppleCoreAPI.accessor.isFood(event.getItemStack()))
        {
            FoodValues values = FoodValues.get(event.getItemStack());
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

            int topIndex = event.getToolTip().size() > 0 ? 1 : 0;
            event.getToolTip().add(topIndex, mealDescriptor.substring(0, 1).toUpperCase() + mealDescriptor.substring(1));
        }
        if (IguanaConfig.wrongBiomeRegrowthMultiplier > 1)
        {
            PlantGrowthModification growthModification = null;
            if (event.getItemStack().getItem() instanceof IPlantable)
            {
                growthModification = ModulePlantGrowth.getPlantGrowthModification(((IPlantable) event.getItemStack().getItem()).getPlant(event.entityPlayer.worldObj, 0, 0, 0));
            }
            else if (event.getItemStack().getItem() instanceof ItemBlock)
            {
                Block block = Block.getBlockFromItem(event.getItemStack().getItem());
                if (block != null)
                    growthModification = ModulePlantGrowth.getPlantGrowthModification(block);
            }
            else
            {
                Block block = PamsModsHelper.fruitItemToBlockMap.get(event.getItemStack().getItem());
                if (block != null)
                    growthModification = ModulePlantGrowth.getPlantGrowthModification(block);
            }

            if (growthModification != null && !growthModification.biomeGrowthModifiers.isEmpty())
            {
                String tooltip = "";
                for (BiomeDictionary.Type biomeType : growthModification.biomeGrowthModifiers.keySet())
                    tooltip += biomeType.toString().substring(0, 1).toUpperCase() + biomeType.toString().substring(1).toLowerCase() + ", ";
                event.getToolTip().add(StatCollector.translateToLocal("hungeroverhaul.crop.grows.best.in"));
                event.getToolTip().add(tooltip.substring(0, tooltip.length() - 2));
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
                    if (par1World.getBlock(l, i1, j1).getMaterial() == Material.WATER)
                        return true;

        return false;
    }
}
