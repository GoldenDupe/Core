package xyz.goldendupe.utils.reference;

import net.kyori.adventure.identity.Identity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public interface OfflinePlayerReference extends Identity {
	@NotNull
	default OfflinePlayer offlinePlayer() {
		return Bukkit.getOfflinePlayer(uuid());
	}
}
