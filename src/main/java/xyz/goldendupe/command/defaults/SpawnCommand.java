package xyz.goldendupe.command.defaults;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.cloudplusplus.annotations.Cloud;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.TimedTeleport;
import xyz.goldendupe.utils.impl.SpawnPosition;

import java.util.HashMap;
import java.util.Map;

@Cloud
public class SpawnCommand extends GDCloudCommand {
	private static final Map<MemberType, Integer> cooldowns = new HashMap<>();

	public SpawnCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		cooldowns.put(MemberType.DEFAULT, 125);
		cooldowns.put(MemberType.DONATOR, 87);
		cooldowns.put(MemberType.OG, 62);
		cooldowns.put(MemberType.MODERATOR, 87);
		cooldowns.put(MemberType.ADMINISTRATOR, 65);

		abstractSpawn("overworld", "spawn");
		abstractSpawn("nether", "nether");
		abstractSpawn("end", "end");
	}

	private void abstractSpawn(String spawn, String name) {
		Command.Builder<Player> commandBuilder = commandManager.commandBuilder(
						name,
						Description.of("Allows player to teleport to " + name)
				)
				.senderType(Player.class)
				.handler(context -> {
					Player sender = context.sender();
					GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
					SpawnPosition oldTeleport = player.teleportingSpawn();

					SpawnPosition newSpawn = goldenDupe.getGlobalData().getSpawns().get(spawn);
					if (newSpawn == null){
						if (sender.hasPermission(MemberType.ADMINISTRATOR.permissionOf("spawn-info"))){
							sender.sendRichMessage("Spawn is not set for spawn <white>"+spawn);
						}
						commandMessenger.message(sender, name+".message-unusable");
					}
					//noinspection DataFlowIssue
					if (!sender.hasPermission(newSpawn.getPermission())) {
						commandMessenger.message(sender, name + ".message-no-teleport-permissions");
						return;
					}

					if (oldTeleport != null) {
						if (oldTeleport.equals(newSpawn)) {
							commandMessenger.message(sender, name + ".message-already-teleporting", new Placeholder("old", oldTeleport.getName()), new Placeholder("new", newSpawn.getName()));
						} else {
							commandMessenger.message(sender, name + ".message-cancel-teleport-rewrite", new Placeholder("old", oldTeleport.getName()), new Placeholder("new", newSpawn.getName()));
						}
						return;
					}

					commandMessenger.message(sender, name + ".message-teleporting", new Placeholder("new", newSpawn.getName()));
					Integer cooldown = cooldowns.get(MemberType.of(sender));
					if (cooldown == null) {
						cooldown = 0;
					}
					new TimedTeleport(commandMessenger,
							spawn,
							sender,
							newSpawn.asLocation(),
							false,
							cooldown)
							.setMoveConsumer(entity -> player.setTeleportingSpawn(null))
							.setTeleportConsumer(entity -> player.setTeleportingSpawn(null))
							.setTeleportConsumer(entity -> player.setTeleportingSpawn(newSpawn));
				});
		commandManager.command(commandBuilder);

		commandManager.command(
				commandBuilder
						.permission(MemberType.ADMINISTRATOR.cloudOf("spawn.teleport-others"))
						.argument(
								PlayerParser.playerComponent().name("who-to-teleport"))
						.handler(context -> {
							Player sender = context.sender();
							Player whoToTeleport = context.sender();
							SpawnPosition newSpawn = goldenDupe.getGlobalData().getSpawns().get(name);

							whoToTeleport.teleportAsync(newSpawn.asLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
							commandMessenger.message(sender, name + ".message-admin-teleport", new Placeholder("new", newSpawn.getName()), new Placeholder("player", whoToTeleport.name()));
							commandMessenger.message(sender, name + ".message-teleported", new Placeholder("new", newSpawn.getName()));
						})
		);
	}
}