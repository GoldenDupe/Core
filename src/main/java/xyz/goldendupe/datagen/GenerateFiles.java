package xyz.goldendupe.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.models.GDSavedData;
import xyz.goldendupe.models.GDSettings;
import xyz.goldendupe.models.serializer.GlobalSaveSerializer;
import xyz.goldendupe.models.serializer.SettingsSerializer;

import java.io.*;
import java.util.List;

public class GenerateFiles {
	public Gson gson = new GsonBuilder().registerTypeAdapter(GDSettings.class, new SettingsSerializer()).registerTypeAdapter(GDSavedData.class, new GlobalSaveSerializer()).setPrettyPrinting().disableHtmlEscaping().create();
	public void generate(@NotNull File folder) throws IOException {
		File config = getOrCreate(new File(folder, "config.json"));
		File data = getOrCreate(new File(folder, "global-data.json"));
		write(gson.toJsonTree(new SettingsData(), GDSettings.class).getAsJsonObject(), config);
		write(gson.toJsonTree(new SavedDataData(), GDSavedData.class).getAsJsonObject(), data);
	}

	public void write(@NotNull JsonObject json, File file){
		try {
			FileReader reader = new FileReader(file);
			JsonObject current = gson.fromJson(reader, JsonObject.class);
			if (current != null) {
				checkUntilJsonEnd(json, current);
			} else {
				current = json;
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(gson.toJson(current));
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private JsonElement checkUntilJsonEnd(@Nullable JsonObject json, JsonObject saving){
		if (json==null){
			return null;
		}
		List.copyOf(json.keySet()).forEach(val->{
			if (saving.get(val)==null){
				saving.add(val, json.get(val));
			} else if (saving.get(val).isJsonObject()){
				JsonObject object;
				if (json.get(val)==null||json.get(val).isJsonNull()||!json.get(val).isJsonObject()){
					object = new JsonObject();
				} else {
					object = json.get(val).getAsJsonObject();
				}
				JsonElement element = checkUntilJsonEnd(object, saving.get(val).getAsJsonObject());
				saving.add(val, element);
			}
		});
		return saving;
	}

	public File getOrCreate(@NotNull File file) throws IOException {
		if (!file.exists()){
			if (!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}
}