package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import com.mojang.brigadier.LiteralMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Cloud
public class TestCommand extends GDCloudCommand {
	public TestCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);

		commandManager.command(
				commandManager.commandBuilder("brigadiertest",
						"bt"
						)
						.argument(
								StringParser.stringComponent(
										StringParser.StringMode.SINGLE
								).suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> {
													List<Suggestion> suggestions = new LinkedList<>();
													suggestions.add(
															TooltipSuggestion.tooltipSuggestion(
																	"Hello!",
																	new LiteralMessage("Bye!")));
													suggestions.add(
															TooltipSuggestion.tooltipSuggestion(
																	"Hopefully-new-line!",
																	new LiteralMessage("New\nLine")));
													suggestions.add(
															TooltipSuggestion.tooltipSuggestion(
																	"Do components work?!",
																	new LiteralMessage(GsonComponentSerializer.gson().serialize(Component.text("Hello", NamedTextColor.RED)))));
													suggestions.add(
															TooltipSuggestion.tooltipSuggestion(
																	"Translatable?",
																	new LiteralMessage(GsonComponentSerializer.gson().serialize(Component.translatable(Material.DIAMOND_SWORD.translationKey())))));
													// Looked at cloud, and they are not done implementing component-based tooltips
													return suggestions;
												});

											}
										}
								).name("hello")
						)
						.handler(context->{
							context.sender().sendMessage("Hi!");
						})
		);
	}
}
