package foxie.bettersleeping.api;

import java.util.UUID;

public interface ISavedDataProvider {
   PlayerData getPlayerData(UUID uuid);
}
