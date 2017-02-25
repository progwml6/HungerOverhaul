package iguanaman.hungeroverhaul.module.event;

import java.util.List;
import java.util.Random;

import com.pam.harvestcraft.blocks.growables.BlockPamCrop;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamFruitLog;
import com.pam.harvestcraft.item.items.ItemPamSeedFood;
import com.progwml6.natura.overworld.block.crops.BlockNaturaBarley;
import com.progwml6.natura.overworld.block.crops.BlockNaturaCotton;

import iguanaman.hungeroverhaul.common.BlockHelper;
import iguanaman.hungeroverhaul.common.ClientHelper;
import iguanaman.hungeroverhaul.common.RandomHelper;
import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.library.ItemAndBlockList;
import iguanaman.hungeroverhaul.module.growth.PlantGrowthModule;
import iguanaman.hungeroverhaul.module.growth.modification.PlantGrowthModification;
import iguanaman.hungeroverhaul.module.harvestcraft.helper.PamsModsHelper;
import iguanaman.hungeroverhaul.module.reflection.ReflectionModule;
import iguanaman.hungeroverhaul.module.seed.GrassSeedsModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockPotato;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;

@SuppressWarnings("deprecation")
public class HungerOverhaulEventHook
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
            float rndBreed = RandomHelper.nextFloat(rand, Config.breedingTimeoutMultiplier);
            float rndChild = RandomHelper.nextFloat(rand, Config.childDurationMultiplier);

            EntityAgeable ageable = (EntityAgeable) event.getEntityLiving();
            int growingAge = ageable.getGrowingAge();

            if (growingAge > 0 && rndBreed >= 1)
                ageable.setGrowingAge(++growingAge);
            else if (growingAge < 0 && rndChild >= 1)
                ageable.setGrowingAge(--growingAge);

            if (Config.eggTimeoutMultiplier > 1 && event.getEntityLiving() instanceof EntityChicken)
            {
                float rnd = RandomHelper.nextFloat(rand, Config.eggTimeoutMultiplier);
                EntityChicken chicken = (EntityChicken) event.getEntityLiving();
                if (chicken.timeUntilNextEgg > 0 && rnd >= 1)
                    chicken.timeUntilNextEgg += 1;
            }

            // Reduced milked value every second
            if (Config.milkedTimeout > 0 && event.getEntityLiving() instanceof EntityCow && event.getEntityLiving().world.getTotalWorldTime() % 20 == 0)
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

        if (!event.getEntityLiving().world.isRemote)
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

                if (event.getEntityLiving() instanceof EntityPlayer && Config.constantHungerLoss)
                {
                    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                    if (!player.capabilities.isCreativeMode && !player.isDead)
                        player.addExhaustion(0.01F);
                }

                if (Config.addLowStatEffects)
                {
                    int difficultyModifierEffects = 2;
                    if (Config.difficultyScalingEffects && event.getEntityLiving().world.getDifficulty() != null)
                    {
                        difficultyModifierEffects = event.getEntityLiving().world.getDifficulty().getDifficultyId();

                        if (!(event.getEntityLiving() instanceof EntityPlayer))
                            difficultyModifierEffects = difficultyModifierEffects * -1 + 3;
                    }

                    // low stat effects
                    if (!creative && isPlayer && !event.getEntityLiving().isDead && healthPercent > 0f)
                    {

                        if (Config.addLowHealthSlowness || Config.addLowHungerSlowness)
                            if ((Config.addLowHungerSlowness && foodLevel <= 1) || (Config.addLowHealthSlowness && healthPercent <= 0.05F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, 1 + difficultyModifierEffects, true, true));
                            else if ((Config.addLowHungerSlowness && foodLevel <= 2) || (Config.addLowHealthSlowness && healthPercent <= 0.10F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, difficultyModifierEffects, true, true));
                            else if (((Config.addLowHungerSlowness && foodLevel <= 3) || (Config.addLowHealthSlowness && healthPercent <= 0.15F)) && difficultyModifierEffects >= 1)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, -1 + difficultyModifierEffects, true, true));
                            else if (((Config.addLowHungerSlowness && foodLevel <= 4) || (Config.addLowHealthSlowness && healthPercent <= 0.20F)) && difficultyModifierEffects >= 2)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, -2 + difficultyModifierEffects, true, true));
                            else if (((Config.addLowHungerSlowness && foodLevel <= 5) || (Config.addLowHealthSlowness && healthPercent <= 0.25F)) && difficultyModifierEffects >= 3)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, -3 + difficultyModifierEffects, true, true));

                        if (Config.addLowHealthMiningSlowdown || Config.addLowHungerMiningSlowdown)
                            if ((Config.addLowHungerMiningSlowdown && foodLevel <= 1) || (Config.addLowHealthMiningSlowdown && healthPercent <= 0.05F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, 1 + difficultyModifierEffects, true, true));
                            else if ((Config.addLowHungerMiningSlowdown && foodLevel <= 2) || (Config.addLowHealthMiningSlowdown && healthPercent <= 0.10F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, difficultyModifierEffects, true, true));
                            else if (((Config.addLowHungerMiningSlowdown && foodLevel <= 3) || (Config.addLowHealthMiningSlowdown && healthPercent <= 0.15F)) && difficultyModifierEffects >= 1)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, -1 + difficultyModifierEffects, true, true));
                            else if (((Config.addLowHungerMiningSlowdown && foodLevel <= 4) || (Config.addLowHealthMiningSlowdown && healthPercent <= 0.20F)) && difficultyModifierEffects >= 2)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, -2 + difficultyModifierEffects, true, true));
                            else if (((Config.addLowHungerMiningSlowdown && foodLevel <= 5) || (Config.addLowHealthMiningSlowdown && healthPercent <= 0.25F)) && difficultyModifierEffects >= 3)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, -3 + difficultyModifierEffects, true, true));

                        if (Config.addLowHealthWeakness || Config.addLowHungerWeakness)
                            //Weakness effect
                            if (((Config.addLowHungerWeakness && foodLevel <= 1) || (Config.addLowHealthWeakness && healthPercent <= 0.05F)) && difficultyModifierEffects >= 1)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 19, -1 + difficultyModifierEffects, true, true));
                            else if (((Config.addLowHungerWeakness && foodLevel <= 2) || (Config.addLowHealthWeakness && healthPercent <= 0.10F)) && difficultyModifierEffects >= 2)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 19, -2 + difficultyModifierEffects, true, true));
                            else if (((Config.addLowHungerWeakness && foodLevel <= 3) || (Config.addLowHealthWeakness && healthPercent <= 0.15F)) && difficultyModifierEffects >= 3)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 19, -3 + difficultyModifierEffects, true, true));

                        if ((Config.addLowHungerNausea && foodLevel <= 1) || (Config.addLowHealthNausea && healthPercent <= 0.05F))
                            event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 19, 0, true, true));
                    }
                }

                tags.setInteger("HungerOverhaulCheck", 9);
            }
        }
    }

    @SubscribeEvent
    public void onUseHoe(UseHoeEvent event)
    {
        if (Config.modifyHoeUse)
        {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            ItemStack itemStack = event.getCurrent();
            EntityPlayer player = event.getEntityPlayer();
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if ((block == Blocks.DIRT || block == Blocks.GRASS) && isWaterNearby(world, pos))
            {
                if (Config.hoeToolDamageMultiplier > 1)
                    itemStack.damageItem(Config.hoeToolDamageMultiplier - 1, player);
            }
            else if (block == Blocks.GRASS && !isWaterNearby(world, pos))
            {
                Block farmland = Blocks.FARMLAND;
                SoundType soundtype = farmland.getSoundType(farmland.getDefaultState(), world, pos, player);

                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                if (!event.getWorld().isRemote && Config.seedChance > 0)
                {
                    int seedChance = Config.seedChance;

                    if (world.getDifficulty().getDifficultyId() < 2)
                        seedChance *= 2;
                    else if (world.getDifficulty().getDifficultyId() == 3)
                        seedChance = Math.max(Math.round(seedChance / 2f), 1);

                    if (event.getWorld().rand.nextInt(100) <= seedChance)
                    {
                        ItemStack seed = GrassSeedsModule.getSeedFromTillingGrass(event.getWorld().rand);

                        if (seed != null)
                        {
                            EntityItem ei = new EntityItem(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, seed.copy());
                            ei.motionY = 0.025000000372529D;

                            world.spawnEntityInWorld(ei);
                        }
                    }
                    world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                }

                if (Config.hoeToolDamageMultiplier > 1)
                    itemStack.damageItem(Config.hoeToolDamageMultiplier - 1, player);
                event.setResult(Result.ALLOW);
            }
            else
                event.setCanceled(true);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderGameOverlay(Text event)
    {
        if (Config.addGuiText)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;

            if (!player.isDead && !player.capabilities.isCreativeMode && !mc.gameSettings.showDebugInfo)
            {
                float healthPercent = player.getHealth() / player.getMaxHealth();

                if (healthPercent <= 0.15F)
                    event.getLeft().add(TextFormatting.RED + I18n.translateToLocal("ui.health.dying") + TextFormatting.RESET);
                else if (healthPercent <= 0.3F)
                    event.getLeft().add(TextFormatting.YELLOW + I18n.translateToLocal("ui.health.injured") + TextFormatting.RESET);
                else if (healthPercent < 0.5F)
                    event.getLeft().add(TextFormatting.WHITE + I18n.translateToLocal("ui.health.hurt") + TextFormatting.RESET);

                if (player.getFoodStats().getFoodLevel() <= 6)
                    event.getRight().add(TextFormatting.RED + I18n.translateToLocal("ui.hunger.starving") + TextFormatting.RESET);
                else if (player.getFoodStats().getFoodLevel() <= 10)
                    event.getRight().add(TextFormatting.YELLOW + I18n.translateToLocal("ui.hunger.hungry") + TextFormatting.RESET);
                else if (player.getFoodStats().getFoodLevel() <= 14)
                    event.getRight().add(TextFormatting.WHITE + I18n.translateToLocal("ui.hunger.peckish") + TextFormatting.RESET);
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteractEvent(EntityInteract event)
    {
        if (Config.milkedTimeout > 0 && event.getEntityPlayer() != null && event.getTarget() != null && event.getTarget() instanceof EntityCow)
        {
            EntityCow cow = (EntityCow) event.getTarget();
            EntityPlayer player = event.getEntityPlayer();
            ItemStack equipped = player.getActiveItemStack();

            if (equipped != null && equipped.getItem() != null)
            {
                Item item = equipped.getItem();

                if (item instanceof ItemBucket && FluidStack.loadFluidStackFromNBT(equipped.getTagCompound()) == null || cow instanceof EntityMooshroom && item == Items.BOWL)
                {
                    NBTTagCompound tags = cow.getEntityData();

                    if (tags.hasKey("Milked"))
                    {
                        event.setCanceled(true);
                        if (!player.world.isRemote)
                            cow.playSound(SoundEvents.ENTITY_COW_HURT, 0.4F, (event.getEntity().world.rand.nextFloat() - event.getEntity().world.rand.nextFloat()) * 0.2F + 1.0F);
                    }
                    else
                        tags.setInteger("Milked", Config.milkedTimeout * 60);
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
    public void onRightClickBlock(RightClickBlock event)
    {
        if (lastRightClickCrop == event.getWorld().getTotalWorldTime())
        {
            event.setCanceled(true);
        }

        if (Config.foodsUnplantable && Loader.isModLoaded("harvestcraft") && event.getEntityPlayer().getHeldItem(event.getHand()) != null && event.getEntityPlayer().getHeldItem(event.getHand()).getItem() instanceof ItemPamSeedFood)
        {
            if (event.getWorld().isRemote)
            {
                if (PamsModsHelper.canPlantSeedFoodAt(event.getEntityPlayer().getHeldItem(event.getHand()), event.getEntityPlayer(), event.getWorld(), event.getPos(), event.getFace()))
                {
                    event.setCanceled(true);
                    return;
                }
            }
            else
            {
                event.setUseItem(Result.DENY);
            }

            return;
        }

        if (!Config.enableRightClickHarvesting)
            return;

        IBlockState clicked_state = event.getWorld().getBlockState(event.getPos());
        IBlockState real_state = clicked_state.getBlock().getActualState(clicked_state, event.getWorld(), event.getPos());

        Block clicked_block = clicked_state.getBlock();

        IBlockState resultingState = null;

        if (rightClickHarvestBlacklist.contains(clicked_block))
            return;

        if (Loader.isModLoaded("natura") && clicked_block.getClass() == BlockNaturaCotton.class)
        {
            if (real_state.getValue(BlockNaturaCotton.AGE) == 4)
                resultingState = real_state.withProperty(BlockNaturaCotton.AGE, 0);
        }
        else if (Loader.isModLoaded("natura") && clicked_block.getClass() == BlockNaturaBarley.class)
        {
            if (real_state.getValue(BlockNaturaBarley.AGE) == 3)
                resultingState = real_state.withProperty(BlockNaturaBarley.AGE, 0);
        }
        else if (clicked_block.getClass() == BlockCrops.class || clicked_block.getClass() == BlockCarrot.class || clicked_block.getClass() == BlockPotato.class)
        {
            if (real_state.getValue(BlockCrops.AGE) >= 7)
                resultingState = real_state.withProperty(BlockCrops.AGE, 0);
        }
        else if (clicked_block.getClass() == BlockBeetroot.class)
        {
            if (real_state.getValue(BlockBeetroot.BEETROOT_AGE) >= 3)
                resultingState = real_state.withProperty(BlockBeetroot.BEETROOT_AGE, 0);
        }
        //TODO: REMOVE REFLECTION HELPER IN 1.11
        else if (Loader.isModLoaded("harvestcraft") && clicked_block.getClass() == BlockPamCrop.class)
        {
            if (ReflectionModule.pamCropAgeFound && real_state.getValue(ReflectionModule.pamCropAge) >= 3)
                resultingState = real_state.withProperty(ReflectionModule.pamCropAge, 0);
        }
        else if (Loader.isModLoaded("harvestcraft") && clicked_block.getClass() == BlockPamFruit.class)
        {
            if (ReflectionModule.pamFruitAgeFound && real_state.getValue(ReflectionModule.pamFruitAge) >= 2)
                resultingState = real_state.withProperty(ReflectionModule.pamFruitAge, 0);
        }
        else if (Loader.isModLoaded("harvestcraft") && clicked_block.getClass() == BlockPamFruitLog.class)
        {
            if (ReflectionModule.pamFruitLogAgeFound && real_state.getValue(ReflectionModule.pamFruitLogAge) >= 2)
                resultingState = real_state.withProperty(ReflectionModule.pamFruitLogAge, 0);
        }

        if (resultingState != null)
        {
            if (!event.getWorld().isRemote && !event.getWorld().restoringBlockSnapshots)
            {
                List<ItemStack> drops = clicked_block.getDrops(event.getWorld(), event.getPos(), clicked_state, 0);

                List<ItemStack> modifiedDrops = BlockHelper.modifyCropDrops(drops, clicked_state, Config.seedsPerHarvestRightClickMin, Config.seedsPerHarvestRightClickMax, Config.producePerHarvestRightClickMin, Config.producePerHarvestRightClickMax);

                for (ItemStack drop : modifiedDrops)
                {
                    Block.spawnAsEntity(event.getWorld(), event.getPos(), drop);
                }
            }

            event.getWorld().setBlockState(event.getPos(), resultingState, 2);

            lastRightClickCrop = event.getWorld().getTotalWorldTime();

            if (event.getWorld().isRemote)
            {
                ClientHelper.sendRightClickPacket(event.getPos(), event.getFace(), event.getHand(), 0f, 0f, 0f);
                event.setCanceled(true);
            }
            else
            {
                event.setUseItem(Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void onBlockHarvested(HarvestDropsEvent event)
    {
        //TODO: REMOVE REFLECTION HELPER IN 1.11

        if (!Config.modifyCropDropsBreak)
            return;

        // certain things we don't want to modify the drops of
        if (HungerOverhaulEventHook.harvestDropsBlacklist.contains(event.getState().getBlock()))
            return;

        IBlockState state = event.getState();
        Block block = state.getBlock();

        boolean isPamCrop = Loader.isModLoaded("harvestcraft") && block.getClass() == BlockPamCrop.class;
        boolean isPamFruit = Loader.isModLoaded("harvestcraft") && block.getClass() == BlockPamFruit.class;
        boolean isPamFruitLog = Loader.isModLoaded("harvestcraft") && block.getClass() == BlockPamFruitLog.class;

        boolean isCottonCrop = Loader.isModLoaded("natura") && block.getClass() == BlockNaturaCotton.class;
        boolean isBarleyCrop = Loader.isModLoaded("natura") && block.getClass() == BlockNaturaBarley.class;

        boolean isBeetrootCrop = block.getClass() == BlockBeetroot.class;

        boolean isVanillaCrop = block.getClass() == BlockCrops.class || block.getClass() == BlockCarrot.class || block.getClass() == BlockPotato.class;

        boolean eligable = isCottonCrop || isBarleyCrop || isVanillaCrop || isBeetrootCrop || isPamCrop || isPamFruit || isPamFruitLog;

        if (!eligable)
            return;

        boolean fullyGrown = (isVanillaCrop && state.getValue(BlockCrops.AGE) >= 7)
                || (isBeetrootCrop && state.getValue(BlockBeetroot.BEETROOT_AGE) >= 3)
                || (isCottonCrop && state.getValue(BlockNaturaCotton.AGE) == 4)
                || (isBarleyCrop && state.getValue(BlockNaturaBarley.AGE) == 3)
                || (ReflectionModule.pamCropAgeFound && (isPamCrop && state.getValue(ReflectionModule.pamCropAge) == 3))
                || (ReflectionModule.pamFruitAgeFound && (isPamFruit && state.getValue(ReflectionModule.pamFruitAge) == 2))
                || (ReflectionModule.pamFruitLogAgeFound && (isPamFruitLog && state.getValue(ReflectionModule.pamFruitLogAge) == 2));

        if (!fullyGrown)
            return;

        List<ItemStack> modifiedDrops = BlockHelper.modifyCropDrops(event.getDrops(), state, Config.seedsPerHarvestBreakMin, Config.seedsPerHarvestBreakMax, Config.producePerHarvestBreakMin, Config.producePerHarvestBreakMax);
        event.getDrops().clear();
        for (ItemStack drop : modifiedDrops)
        {
            event.getDrops().add(drop);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderTooltips(ItemTooltipEvent event)
    {
        if (Config.addFoodTooltips && AppleCoreAPI.accessor.isFood(event.getItemStack()))
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

            if (adjective != null && I18n.canTranslate("tooltip.meal." + adjective + "_" + noun))
            {
                mealDescriptor = I18n.translateToLocal("tooltip.meal." + adjective + "_" + noun);
            }
            else
            {
                mealDescriptor = I18n.translateToLocal("tooltip.meal." + noun);
                if (adjective != null)
                {
                    mealDescriptor = I18n.translateToLocalFormatted(I18n.translateToLocal("tooltip.meal." + adjective), mealDescriptor);
                }
            }

            int topIndex = event.getToolTip().size() > 0 ? 1 : 0;
            event.getToolTip().add(topIndex, mealDescriptor.substring(0, 1).toUpperCase() + mealDescriptor.substring(1));
        }
        if (Config.wrongBiomeRegrowthMultiplier > 1)
        {
            PlantGrowthModification growthModification = null;
            if (event.getItemStack().getItem() instanceof IPlantable)
            {
                growthModification = PlantGrowthModule.getPlantGrowthModification(((IPlantable) event.getItemStack().getItem()).getPlant(event.getEntityPlayer().world, BlockPos.ORIGIN).getBlock());
            }
            else if (event.getItemStack().getItem() instanceof ItemBlock)
            {
                Block block = Block.getBlockFromItem(event.getItemStack().getItem());
                if (block != null)
                    growthModification = PlantGrowthModule.getPlantGrowthModification(block);
            }
            else
            {
                Block block = PamsModsHelper.fruitItemToBlockMap.get(event.getItemStack().getItem());
                if (block != null)
                    growthModification = PlantGrowthModule.getPlantGrowthModification(block);
            }

            if (growthModification != null && !growthModification.biomeGrowthModifiers.isEmpty())
            {
                String tooltip = "";
                for (BiomeDictionary.Type biomeType : growthModification.biomeGrowthModifiers.keySet())
                    tooltip += biomeType.toString().substring(0, 1).toUpperCase() + biomeType.toString().substring(1).toLowerCase() + ", ";
                event.getToolTip().add(I18n.translateToLocal("tooltip.meal.crop_grows_best_in"));
                event.getToolTip().add(tooltip.substring(0, tooltip.length() - 2));
            }
        }
    }

    /**
     * returns true if there's water nearby (x-4 to x+4, y to y+1, k-4 to k+4)
     */
    private boolean isWaterNearby(World world, BlockPos pos)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        MutableBlockPos mutableblockpos = new MutableBlockPos();

        for (int l = i - 4; l <= i + 4; ++l)
        {
            for (int i1 = j; i1 <= j + 1; ++i1)
            {
                for (int j1 = k - 4; j1 <= k + 4; ++j1)
                {
                    if (world.getBlockState(mutableblockpos.setPos(l, i1, j1)).getMaterial() == Material.WATER)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
