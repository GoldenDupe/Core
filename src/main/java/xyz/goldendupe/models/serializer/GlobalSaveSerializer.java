package xyz.goldendupe.models.serializer;

import com.google.gson.*;
import xyz.goldendupe.models.GDSavedData;

import java.lang.reflect.Type;

public class GlobalSaveSerializer implements JsonSerializer<GDSavedData>, JsonDeserializer<GDSavedData> {
	@Override
	public GDSavedData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		return new GDSavedData(
				object.get("times-duped").getAsLong(),
				object.get("items-duped").getAsLong(),
				object.get("items-generated").getAsLong()
		);
	}

	@Override
	public JsonElement serialize(GDSavedData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		object.addProperty("times-duped", src.getTimesDuped());
		object.addProperty("items-duped", src.getItemsDuped());
		object.addProperty("items-generated", src.getItemsGenerated());
		return object;
	}
}
