package foxie.bettersleeping;

public class EventHandlers {

   public static EventHandlers INSTANCE;

   public EventHandlers() {
      INSTANCE = this;
   }
/*
   @SubscribeEvent
   public void onPlayerDeath(LivingDeathEvent event) {
      if (event.entity.worldObj.isRemote)
         return;

      if (!Config.enableSleepCounter)
         return;

      if (!Config.resetCounterOnDeath)
         return;

      if (event.entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer) event.entity;
         PlayerData data = FoxieSavedData.instance().getPlayerData(player.getUniqueID());
         data.reset(Config.spawnSleepCounter);
         FoxieSavedData.instance().markDirty();
      }
   }

   @SubscribeEvent
   public void onPreWorldTick(TickEvent.WorldTickEvent event) {
      if (!(event.world instanceof WorldServer))
         return;

      if (event.phase != TickEvent.Phase.START)
         return;

      WorldServer world = (WorldServer) event.world;

      if (world.areAllPlayersAsleep()) {
         Alarm.sleepWorld(world);
      }
   }

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.START)
         return;

      PlayerData data = null;
      if (event.player.worldObj.isRemote)
         return;

      if (!event.player.isEntityAlive())
         return;

      if (Config.enableSleepCounter) {
         data = FoxieSavedData.instance().getData(event.player);
         data.ticksSinceUpdate++;
         double ticksPerSleepCounter = Config.ticksPerSleepCounter;

         if (event.player.isSprinting())
            ticksPerSleepCounter /= Config.multiplicatorWhenSprinting;

         if (data.ticksSinceUpdate >= ticksPerSleepCounter) {
            data.ticksSinceUpdate = 0;

            if (!event.player.capabilities.isCreativeMode)
               data.decreaseSleepLevel();

         }

         if (event.player.isPlayerSleeping() && Config.giveSleepCounterOnSleep > 0) {
            data.increaseSleepLevel(Config.giveSleepCounterOnSleep);
         }

         // send update about tiredness to the client
         DebuffLogic.updateClientIfNeeded(event.player, data);
      }

      if (data == null)
         return; // safety, should not happen except maybe some edge cases

      if (Config.enableDebuffs && Config.enableSleepCounter && ticksSinceUpdate > 20) {
         // check for debuffs
         DebuffLogic.checkForDebuffs(event, data);

         if (Config.enableCaffeine) {
            CaffeineLogic.checkDebuff(event.player);
         }

         if (Config.enviromineSanityDecrease > 0 && Loader.isModLoaded("enviromine")) {
            if (Config.enviromineSanityAt > ((float) data.getSleepLevel() / Config.maximumSleepCounter) * 100f) {
            }
            //CompatibilityEnviroMine.changeSanity(event.player, Config.enviromineSanityDecrease * (-1)); // TODO removed
         }

         ticksSinceUpdate = 0;
      }

      FoxieSavedData.instance().markDirty();

      ticksSinceUpdate++;
   }

   @SubscribeEvent
   public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
      if (event.entityPlayer.worldObj.isRemote)
         return;

      if (Config.disableSleeping) {
         event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("msg.sleepingDisabled"));
         event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
         return;
      }

      if (Config.enableSleepCounter) {
         PlayerData data = FoxieSavedData.instance().getData(event.entityPlayer);

         if (data.getSleepLevel() >= Config.maximumSleepCounter) {
            event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("msg.notTired"));
            event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
         }
      }

      // check for amount of people sleeping in this dimension
      AlternateSleep.trySleepingWorld(event.entityPlayer.worldObj);
   }

   @SubscribeEvent
   public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
      if (event.player.worldObj == null)
         return;

      if (event.player.worldObj.isRemote)
         return;

      if (Config.percentPeopleToSleep > 1)
         return;

      AlternateSleep.trySleepingWorld(event.player.worldObj, true);
   }

   @SubscribeEvent
   public void onEntityJump(LivingEvent.LivingJumpEvent event) {
      if (event.entity instanceof EntityPlayer && Config.tirednessJump > 0) {
         EntityPlayer player = (EntityPlayer) event.entity;
         if (player.worldObj.isRemote)
            return;

         if (player.capabilities.isCreativeMode)
            return;

         PlayerData data = FoxieSavedData.instance().getData(player);
         if (player.isSprinting())
            data.decreaseSleepLevel((long) (Config.tirednessJump * Config.multiplicatorWhenSprinting));
         else
            data.decreaseSleepLevel(Config.tirednessJump);

         FoxieSavedData.instance().markDirty();

      }
   }

   @SubscribeEvent
   public void onPlayerWakeUpEvent(PlayerWakeUpEvent event) {
      if (event.entityPlayer.worldObj.isRemote)
         return;

      Random rand = event.entityPlayer.worldObj.rand;

      if (rand.nextFloat() < Config.chanceToGetBadNight) {
         if (rand.nextFloat() >= 0.5f)
            event.entityPlayer.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 60, rand.nextInt(2) + 1));
         else
            event.entityPlayer.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 60, rand.nextInt(2) + 1));
      } else if (rand.nextFloat() < Config.chanceToGetGoodNight) {
         if (rand.nextFloat() >= 0.5f)
            event.entityPlayer.addPotionEffect(new PotionEffect(Potion.heal.getId(), 60, rand.nextInt(2) + 1));
         else
            event.entityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 60, rand.nextInt(2) + 1));
      }
   }

   @SubscribeEvent
   public void onPlayerUseItem(PlayerUseItemEvent.Finish event) {
      if (event.entityPlayer.worldObj.isRemote)
         return;

      PlayerData data = FoxieSavedData.instance().getData(event.entityPlayer);

      if (CaffeineLogic.isCoffee(event.item)) {
         if (event.result.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) event.result.getItem();
            float hunger = food.func_150905_g(event.result);
            float saturation = food.func_150906_h(event.result);
            hunger *= Config.itemFoodHungerMult;
            saturation *= Config.itemFoodSaturationMult;
            data.increaseCaffeineLevel(hunger);
            data.increaseSleepLevel((int) saturation);
         } else {
            data.increaseCaffeineLevel(Config.caffeinePerItem);
            data.increaseSleepLevel(Config.tirednessPerCaffeine);
         }

         FoxieSavedData.instance().markDirty();
      }

      if (CaffeineLogic.isPill(event.item)) {
         data.increasePillLevel(Config.pillPerPill);
         FoxieSavedData.instance().markDirty();
      }

      if (CaffeineLogic.isSleepingPill(event.item)) {
         data.decreaseSleepLevel(Config.sleepingPillAmount);
         FoxieSavedData.instance().markDirty();
      }

      // send update about tiredness to the client
      DebuffLogic.updateClientIfNeeded(event.entityPlayer, data);
   }*/
}
