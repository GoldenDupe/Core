package xyz.goldendupe.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.perks.PerkTranslations;

import java.io.*;

import static bet.astral.messenger.v2.translation.serializer.gson.TranslationGsonHelper.getDefaults;

public class GenerateMessages implements Generate {
	public Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public void generate(@NotNull File folder) throws IOException {
		File messages = getOrCreate(new File(folder, "messages.json"));
		File perks = getOrCreate(new File(folder, "perk-translations.json"));
        write(getDefaults(Translations.class, MiniMessage.miniMessage(), gson), messages);
		write(getDefaults(PerkTranslations.class, MiniMessage.miniMessage(), gson), perks);
    }

	@Override
	public Gson getGson() {
		return gson;
	}
}
