package foxie.bettersleeping.modules;

import net.minecraft.util.DamageSource;

public class TirednessModule extends Module {
   public static DamageSource tirednessDamage = new DamageSource("tiredness").setDamageBypassesArmor();
}
