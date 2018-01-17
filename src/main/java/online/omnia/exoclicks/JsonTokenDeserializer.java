package online.omnia.exoclicks;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by lollipop on 01.11.2017.
 */
public class JsonTokenDeserializer implements JsonDeserializer<JsonTokenEntity>{
    @Override
    public JsonTokenEntity deserialize(JsonElement jsonElement, Type type,
                                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        JsonTokenEntity jsonTokenEntity = new JsonTokenEntity();
        jsonTokenEntity.setToken(object.get("token").getAsString());
        jsonTokenEntity.setType(object.get("type").getAsString());
        jsonTokenEntity.setExpiresIn(object.get("expires_in").getAsInt());
        return jsonTokenEntity;
    }
}
