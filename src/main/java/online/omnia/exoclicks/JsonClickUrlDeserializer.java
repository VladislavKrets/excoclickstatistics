package online.omnia.exoclicks;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by lollipop on 07.11.2017.
 */
public class JsonClickUrlDeserializer implements JsonDeserializer<String>{
    @Override
    public String deserialize(JsonElement jsonElement, Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject().get("result").getAsJsonObject();
        if (object.get("variations") == null) return null;
        JsonArray array = object.get("variations").getAsJsonArray();
        for (JsonElement element : array) {
            System.out.println(element);
            if (element.getAsJsonObject().get("url") != null)
            return element.getAsJsonObject().get("url").getAsString();
        }
        return null;
    }
}
