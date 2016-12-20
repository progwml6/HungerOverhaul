package iguanaman.hungeroverhaul.util;

import java.util.List;
import java.util.Random;

import com.pam.harvestcraft.item.items.ItemPamSeedFood;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.module.ModuleGrassSeeds;
import iguanaman.hungeroverhaul.module.ModulePlantGrowth;
import iguanaman.hungeroverhaul.module.PamsModsHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityAgeable;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
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
            if (IguanaConfig.milkedTimeout > 0 && event.getEntityLiving() instanceof EntityCow && event.getEntityLiving().world.getTotalWorldTime() % 20 == 0)
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

                if (event.getEntityLiving() instanceof EntityPlayer && IguanaConfig.constantHungerLoss)
                {
                    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                    if (!player.capabilities.isCreativeMode && !player.isDead)
                        player.addExhaustion(0.01F);
                }

                if (IguanaConfig.addLowStatEffects)
                {
                    int difficultyModifierEffects = 2;
                    if (IguanaConfig.difficultyScalingEffects && event.getEntityLiving().world.getDifficulty() != null)
                    {
                        difficultyModifierEffects = event.getEntityLiving().world.getDifficulty().getDifficultyId();

                        if (!(event.getEntityLiving() instanceof EntityPlayer))
                            difficultyModifierEffects = difficultyModifierEffects * -1 + 3;
                    }

                    // low stat effects
                    if (!creative && isPlayer && !event.getEntityLiving().isDead && healthPercent > 0f)
                    {

                        if (IguanaConfig.addLowHealthSlowness || IguanaConfig.addLowHungerSlowness)
                            if ((IguanaConfig.addLowHungerSlowness && foodLevel <= 1) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.05F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, 1 + difficultyModifierEffects, true, true));
                            else if ((IguanaConfig.addLowHungerSlowness && foodLevel <= 2) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.10F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, difficultyModifierEffects, true, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 3) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.15F)) && difficultyModifierEffects >= 1)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, -1 + difficultyModifierEffects, true, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 4) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.20F)) && difficultyModifierEffects >= 2)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, -2 + difficultyModifierEffects, true, true));
                            else if (((IguanaConfig.addLowHungerSlowness && foodLevel <= 5) || (IguanaConfig.addLowHealthSlowness && healthPercent <= 0.25F)) && difficultyModifierEffects >= 3)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 19, -3 + difficultyModifierEffects, true, true));

                        if (IguanaConfig.addLowHealthMiningSlowdown || IguanaConfig.addLowHungerMiningSlowdown)
                            if ((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 1) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.05F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, 1 + difficultyModifierEffects, true, true));
                            else if ((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 2) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.10F))
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, difficultyModifierEffects, true, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 3) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.15F)) && difficultyModifierEffects >= 1)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, -1 + difficultyModifierEffects, true, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 4) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.20F)) && difficultyModifierEffects >= 2)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, -2 + difficultyModifierEffects, true, true));
                            else if (((IguanaConfig.addLowHungerMiningSlowdown && foodLevel <= 5) || (IguanaConfig.addLowHealthMiningSlowdown && healthPercent <= 0.25F)) && difficultyModifierEffects >= 3)
                                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 19, -3 + difficultyModifierEffects, true, true));

                        if (IguanaConfig.addLowHealthWeakness || IguanaConfig.addLowHungerWeakness)
                            //Weakness effect
                            if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 1) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.05F)) && difficultyModifierEffects >= 1)
                            event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 19, -1 + difficultyModifierEffects, true, true));
                            else if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 2) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.10F)) && difficultyModifierEffects >= 2)
                            event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 19, -2 + difficultyModifierEffects, true, true));
                            else if (((IguanaConfig.addLowHungerWeakness && foodLevel <= 3) || (IguanaConfig.addLowHealthWeakness && healthPercent <= 0.15F)) && difficultyModifierEffects >= 3)
                            event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 19, -3 + difficultyModifierEffects, true, true));

                        if ((IguanaConfig.addLowHungerNausea && foodLevel <= 1) || (IguanaConfig.addLowHealthNausea && healthPercent <= 0.05F))
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
        if (IguanaConfig.modifyHoeUse)
        {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            ItemStack itemStack = event.getCurrent();
            EntityPlayer player = event.getEntityPlayer();
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            //int fortune = itemStack;

            if ((block == Blocks.DIRT || block == Blocks.GRASS) && isWaterNearby(world, pos.getX(), pos.getY(), pos.getZ()))
            {
                if (IguanaConfig.hoeToolDamageMultiplier > 1)
                    itemStack.damageItem(IguanaConfig.hoeToolDamageMultiplier - 1, player);
            }
            else if (block == Blocks.GRASS && !isWaterNearby(world, pos.getX(), pos.getY(), pos.getZ()))
            {
                Block farmland = Blocks.FARMLAND;
                SoundType soundtype = farmland.getSoundType(farmland.getDefaultState(), world, pos, player);

                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                if (!world.isRemote && IguanaConfig.seedChance > 0)
                {
                    int seedChance = IguanaConfig.seedChance;
                    if (event.getWorld().getDifficulty().getDifficultyId() < 2)
                        seedChance *= 2;
                    else if (event.getWorld().getDifficulty().getDifficultyId() == 3)
                        seedChance = Math.max(Math.round(seedChance / 2f), 1);

                    if (world.rand.nextInt(100) <= seedChance)
                    {
                        ItemStack seed = ModuleGrassSeeds.getSeedFromTillingGrass(world.rand);
                        if (seed != null)
                            Block.spawnAsEntity(world, pos, seed);
                        //block.spawnAsEntity(world, pos, seed);
                    }
                    world.setBlockState(pos, Blocks.DIRT.getDefaultState());
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
            EntityPlayer player = mc.player;

            if (!player.isDead && !player.capabilities.isCreativeMode && !mc.gameSettings.showDebugInfo)
            {

                float healthPercent = player.getHealth() / player.getMaxHealth();

                if (healthPercent <= 0.15F)
                    event.getLeft().add(TextFormatting.RED + I18n.translateToLocal("hungeroverhaul.dying") + TextFormatting.RESET);
                else if (healthPercent <= 0.3F)
                    event.getLeft().add(TextFormatting.YELLOW + I18n.translateToLocal("hungeroverhaul.injured") + TextFormatting.RESET);
                else if (healthPercent < 0.5F)
                    event.getLeft().add(TextFormatting.WHITE + I18n.translateToLocal("hungeroverhaul.hurt") + TextFormatting.RESET);

                if (player.getFoodStats().getFoodLevel() <= 6)
                    event.getLeft().add(TextFormatting.RED + I18n.translateToLocal("hungeroverhaul.starving") + TextFormatting.RESET);
                else if (player.getFoodStats().getFoodLevel() <= 10)
                    event.getLeft().add(TextFormatting.YELLOW + I18n.translateToLocal("hungeroverhaul.hungry") + TextFormatting.RESET);
                else if (player.getFoodStats().getFoodLevel() <= 14)
                    event.getLeft().add(TextFormatting.WHITE + I18n.translateToLocal("hungeroverhaul.peckish") + TextFormatting.RESET);
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteractEvent(EntityInteract event)
    {
        if (IguanaConfig.milkedTimeout > 0 && event.getEntityPlayer() != null && event.getTarget() != null && event.getTarget() instanceof EntityCow)
        {
            EntityCow cow = (EntityCow) event.getTarget();
            EntityPlayer player = event.getEntityPlayer();
            ItemStack equipped = player.getActiveItemStack();
            if (equipped != null && equipped.getItem() != null)
            {
                Item item = equipped.getItem();
                FluidStack.loadFluidStackFromNBT(equipped.getTagCompound());
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

    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
    {
        if (lastRightClickCrop == event.getWorld().getTotalWorldTime())
            event.setCanceled(true);

        if (event.getEntityPlayer() == null)
            return;
        if (event.getHand() != EnumHand.MAIN_HAND)
            return;

        if (IguanaConfig.foodsUnplantable && Loader.isModLoaded("harvestcraft") && event.getEntityPlayer().getHeldItemMainhand() != null && event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemPamSeedFood)
        {
            if (event.getWorld().isRemote)
            {
                if (PamsModsHelper.canPlantSeedFoodAt(event.getEntityPlayer(), event.getEntityPlayer().getHeldItemMainhand(), event.getWorld(), event.getPos(), event.getFace()))
                {
                    event.setCanceled(true);
                }
            }
            else
                event.setUseItem(Result.DENY);
            return;
        }

        if (!IguanaConfig.enableRightClickHarvesting)
            return;

        final IBlockState blockState = event.getWorld().getBlockState(event.getPos());
        final Block clicked = blockState.getBlock();
        final int meta = clicked.getMetaFromState(blockState);
        int resultingMeta = -1;

        if (rightClickHarvestBlacklist.contains(clicked))
            return;

        /*if (Loader.isModLoaded("Natura") && clicked instanceof CropBlock)
        {
            if (meta == 3 || meta == 8)
                resultingMeta = meta == 3 ? 0 : 4;
        }
        else if (clicked instanceof BlockCrops && meta >= 7)*/
        if (clicked instanceof BlockCrops && meta >= 7)
        {
            resultingMeta = 0;
        }

        if (resultingMeta >= 0)
        {
            if (!event.getWorld().isRemote && !event.getWorld().restoringBlockSnapshots)
            {
                List<ItemStack> drops = clicked.getDrops(event.getWorld(), event.getPos(), blockState, 0);

                List<ItemStack> modifiedDrops = BlockHelper.modifyCropDrops(drops, blockState, IguanaConfig.seedsPerHarvestRightClickMin, IguanaConfig.seedsPerHarvestRightClickMax, IguanaConfig.producePerHarvestRightClickMin, IguanaConfig.producePerHarvestRightClickMax);

                for (ItemStack drop : modifiedDrops)
                {
                    Block.spawnAsEntity(event.getWorld(), event.getPos(), drop);
                    //clicked.spawnAsEntity(event.getWorld(), event.getPos(), drop);
                }
            }

            event.getWorld().setBlockState(event.getPos(), clicked.getStateFromMeta(resultingMeta), 2);

            lastRightClickCrop = event.getWorld().getTotalWorldTime();

            if (event.getWorld().isRemote)
            {
                ClientHelper.sendRightClickPacket(event.getPos(), event.getFace(), EnumHand.MAIN_HAND, 0f, 0f, 0f);
                event.setCanceled(true);
            }
            else
                event.setUseItem(Result.DENY);
        }
    }

    @SubscribeEvent
    public void onBlockHarvested(BlockEvent.HarvestDropsEvent event)
    {
        Block block = event.getState().getBlock();
        int blockMeta = block.getMetaFromState(event.getState());

        if (!IguanaConfig.modifyCropDropsBreak)
            return;

        // certain things we don't want to modify the drops of
        if (IguanaEventHook.harvestDropsBlacklist.contains(block))
            return;

        boolean isNaturaCrop = Loader.isModLoaded("Natura");//TODO: FIX && block instanceof CropBlock;
        boolean eligable = isNaturaCrop || block instanceof BlockCrops;

        if (!eligable)
            return;

        boolean fullyGrown = (!isNaturaCrop && blockMeta >= 7) || (isNaturaCrop && blockMeta == 3 || blockMeta == 8);

        if (!fullyGrown)
            return;

        List<ItemStack> modifiedDrops = BlockHelper.modifyCropDrops(event.getDrops(), event.getState(), IguanaConfig.seedsPerHarvestBreakMin, IguanaConfig.seedsPerHarvestBreakMax, IguanaConfig.producePerHarvestBreakMin, IguanaConfig.producePerHarvestBreakMax);
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

            if (adjective != null && I18n.canTranslate("hungeroverhaul." + adjective + "." + noun))
            {
                mealDescriptor = I18n.translateToLocal("hungeroverhaul." + adjective + "." + noun);
            }
            else
            {
                mealDescriptor = I18n.translateToLocal("hungeroverhaul." + noun);
                if (adjective != null)
                {
                    mealDescriptor = I18n.translateToLocalFormatted(I18n.translateToLocal("hungeroverhaul." + adjective), mealDescriptor);
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
                growthModification = ModulePlantGrowth.getPlantGrowthModification(((IPlantable) event.getItemStack().getItem()).getPlant(event.getEntityPlayer().world, BlockPos.ORIGIN).getBlock());
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
                event.getToolTip().add(I18n.translateToLocal("hungeroverhaul.crop.grows.best.in"));
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
                    if (par1World.getBlockState(new BlockPos(l, i1, j1)).getMaterial() == Material.WATER)
                        return true;

        return false;
    }
}
