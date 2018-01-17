package online.omnia.exoclicks;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lollipop on 31.10.2017.
 */
public class JsonCampaignListDeserializer implements JsonDeserializer<List<JsonCampaignEntity>>{
    @Override
    public List<JsonCampaignEntity> deserialize(JsonElement jsonElement, Type type,
                                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject().get("result").getAsJsonObject();
        System.out.println(object);
        String str = object.toString().replaceAll("\"\\d+\":", "");
        int strLength = str.length();
        str = str.substring(1, strLength - 1);
        str = "[" + str + "]";
        System.out.println(str);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(str).getAsJsonArray();
        List<JsonCampaignEntity> jsonCampaignEntities = new ArrayList<>();
        JsonCampaignEntity jsonCampaignEntity;
        for (JsonElement element : array) {
            jsonCampaignEntity = new JsonCampaignEntity();
            jsonCampaignEntity.setId(element.getAsJsonObject().get("id").getAsInt());
            jsonCampaignEntity.setName(element.getAsJsonObject().get("name").getAsString());
            jsonCampaignEntities.add(jsonCampaignEntity);
        }
        return jsonCampaignEntities;
    }
}
