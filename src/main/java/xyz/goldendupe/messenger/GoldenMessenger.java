package xyz.goldendupe.messenger;

import bet.astral.messenger.v2.delay.Delay;
import bet.astral.messenger.v3.minecraft.paper.PaperMessenger;
import bet.astral.messenger.v2.permission.Permission;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.slf4j.Logger;
import xyz.goldendupe.GoldenDupe;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;


public class GoldenMessenger extends PaperMessenger {
	private static final DecimalFormat format = new DecimalFormat(".xx");

	public GoldenMessenger() {
		super(null);
	}

	public static String format(double number) {
		return format.format(number);
	}

	public static String format(float number) {
		return format.format(number);
	}

	public List<Placeholder> createCooldownPlaceholders(long left) {
		List<Placeholder> placeholders = new LinkedList<>();
		Duration durationLeft = Duration.ofMillis(left);
		placeholders.add(Placeholder.of("%cooldown_millis%", durationLeft.toMillis()));
		placeholders.add(Placeholder.of("%cooldown_seconds%", durationLeft.toSeconds()));
		placeholders.add(Placeholder.of("%cooldown_minutes%", durationLeft.toMinutes()));
		placeholders.add(Placeholder.of("%cooldown_hours%", durationLeft.toHours()));
		return placeholders;
	}

	public void broadcast(MessageChannel channel, TranslationKey messageKey, Delay delay, List<Placeholder> placeholders) {
		placeholders = new LinkedList<>(placeholders);
		broadcast(Permission.of(channel.permission), delay, messageKey, placeholders);
	}

	public void broadcast(MessageChannel channel, TranslationKey messageKey, Delay delay, Placeholder... placeholders) {
		broadcast(Permission.of(channel.permission), delay, messageKey, placeholders);
	}

	public void broadcast(MessageChannel channel, TranslationKey messageKey, List<Placeholder> placeholders) {
		broadcast(Permission.of(channel.permission), messageKey, placeholders);
	}

	public void broadcast(MessageChannel channel, TranslationKey messageKey, Placeholder... placeholders) {
		broadcast(Permission.of(channel.permission), messageKey, placeholders);
	}


	@Override
	public Logger getLogger() {
		return GoldenDupe.instance().getComponentLogger();
	}

	public enum MessageChannel {
		DONATOR("goldendupe.channel.donator", "channels.donator"),
		STAFF("goldendupe.channel.staff", "channels.staff"),
		ADMIN("goldendupe.channel.admin", "channels.admin"),
		OG("goldendupe.channel.og", "channels.og"),
		BOOSTER("goldendupe.channel.booster", "channels.booster");

		private final String permission;
		private final String key;

		MessageChannel(String permission, String key) {
			this.permission = permission;
			this.key = key;
		}

		public String permission() {
			return permission;
		}

		public String key() {
			return key;
		}
	}

}














