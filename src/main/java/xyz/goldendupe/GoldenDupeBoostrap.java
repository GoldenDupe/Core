package xyz.goldendupe;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GoldenDupeBoostrap implements PluginBootstrap {
	@Override
	public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
	}

	@Override
	public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
		return  new GoldenDupe(this);
	}
}
