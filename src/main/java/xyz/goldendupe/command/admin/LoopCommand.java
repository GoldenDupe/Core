package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import com.mojang.brigadier.LiteralMessage;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;
import org.incendo.cloud.parser.standard.IntegerParser;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Cloud
public class LoopCommand extends GDCloudCommand {
	private final Map<UUID, ScheduledTask> tasks = new HashMap<>();
	public LoopCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
				Command.Builder<Player> builder = commandManager.commandBuilder(
						"loop",
						Description.of("Allows admins to loop chat "),
						"aloop")
						.senderType(Player.class)
						.permission(MemberType.ADMINISTRATOR.cloudOf("loop"))
						.argument(IntegerParser.integerComponent()
										.name("times")
								.suggestionProvider(IntegerParser.integerParser(0, 30).parser().suggestionProvider()))
						.argument(IntegerParser.integerComponent()
								.name("timespan")
								.suggestionProvider(IntegerParser.integerParser(0, 30).parser().suggestionProvider()))
						.argument(EnumParser.enumComponent(TimeUnit.class).name("timeunit"))
						.argument(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("executable")
								.suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(()->Bukkit.getCommandMap().getKnownCommands().values().stream().map(command-> TooltipSuggestion.suggestion(command.getName(), new LiteralMessage(command.getDescription()))).collect(Collectors.toList()));
											}
										}
								)
						).handler(context->{
							Player sender = context.sender();

							if (tasks.get(sender.getUniqueId()) != null){
								if (!tasks.get(sender.getUniqueId()).isCancelled()){
									messenger.message(sender, Translations.COMMAND_LOOP_ALREADY_LOOPING);
									return;
								}
							}

							TimeUnit timeUnit = context.get("timeunit");
							int times = context.get("times");
							int timeSpan = context.get("timespan");
							String executing = context.get("executable");

							AtomicInteger timesExecuted = new AtomicInteger();
							messenger.message(sender, Translations.COMMAND_LOOP_BEGIN, Placeholder.of("%command%", executing), Placeholder.of("times", times));
							if (times>200){
								messenger.message(sender, Translations.COMMAND_LOOP_WARN, Placeholder.of("%command%", executing), Placeholder.of("times", times));
							}
							goldenDupe().getServer().getAsyncScheduler().runAtFixedRate(goldenDupe(),
									task->{
										if (timesExecuted.get()>times){
											task.cancel();
											messenger.message(sender, Translations.COMMAND_LOOP_DONE, Placeholder.of("%command%", executing), Placeholder.of("times", times));
											return;
										}

										if (!sender.isOnline()){
											task.cancel();
											return;
										}

										sender.chat(executing);
										timesExecuted.getAndIncrement();
									},
									0,
									timeSpan,
									timeUnit);
						});
				commandManager.command(builder);
				commandManager.command(builder.literal("cancel")
						.handler(context->{
							Player sender = context.sender();
							if (tasks.get(sender.getUniqueId()) != null){
								if (!tasks.get(sender.getUniqueId()).isCancelled()){
									tasks.get(sender.getUniqueId()).cancel();
									messenger.message(sender, Translations.COMMAND_LOOP_CANCELED);
									return;
								}
							}
							messenger.message(sender, Translations.COMMAND_LOOP_NO_TASK_FOUND);
						})
				);
	}
}
