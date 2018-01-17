package online.omnia.exoclicks;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lollipop on 31.10.2017.
 */
public class JsonCampaignStatisticsDeserializer implements JsonDeserializer<List<JsonCampaignStatistics>> {
    @Override
    public List<JsonCampaignStatistics> deserialize(JsonElement jsonElement, Type type,
                                                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        JsonElement resultElement = object.get("result");
        JsonArray array = null;
        JsonObject resultObject = null;
        if (resultElement.isJsonArray()) {
            array = resultElement.getAsJsonArray();
        } else if (resultElement.isJsonObject()) {
            resultObject = resultElement.getAsJsonObject();
        }
        List<JsonCampaignStatistics> jsonCampaignStatistics = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JsonCampaignStatistics statistics;
        if (array != null) {
            for (JsonElement element : array) {
                statistics = new JsonCampaignStatistics();
                statistics.setAvgCpc(element.getAsJsonObject().get("cpc") == null ? 0 : element.getAsJsonObject().get("cpc").getAsInt());
                statistics.setClicks(element.getAsJsonObject().get("clicks").getAsInt());
                statistics.setCost(element.getAsJsonObject().get("cost").getAsDouble());
                statistics.setCpm(element.getAsJsonObject().get("cpm").getAsInt());
                statistics.setCtr(element.getAsJsonObject().get("ctr").getAsInt());
                statistics.setCpv(element.getAsJsonObject().get("cpv").getAsDouble());
                statistics.setImpressions(element.getAsJsonObject().get("impressions").getAsInt());
                try {
                    statistics.setDdate(simpleDateFormat.parse(element.getAsJsonObject().get("ddate").getAsString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                statistics.setHits(element.getAsJsonObject().get("video_hits").getAsInt());
                statistics.setValue(element.getAsJsonObject().get("value").getAsInt());
                jsonCampaignStatistics.add(statistics);
            }
        } else if (resultObject != null) {
            statistics = new JsonCampaignStatistics();
            statistics.setAvgCpc(resultObject.getAsJsonObject().get("avg_cpc").getAsInt());
            statistics.setClicks(resultObject.getAsJsonObject().get("clicks").getAsInt());
            statistics.setCost(resultObject.getAsJsonObject().get("cost").getAsInt());
            statistics.setCpm(resultObject.getAsJsonObject().get("cpm").getAsInt());
            statistics.setCtr(resultObject.getAsJsonObject().get("ctr").getAsInt());
            statistics.setImpressions(resultObject.getAsJsonObject().get("impressions").getAsInt());
            try {
                statistics.setDdate(simpleDateFormat.parse(resultObject.getAsJsonObject().get("ddate").getAsString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            statistics.setHits(resultObject.getAsJsonObject().get("hits").getAsInt());
            statistics.setValue(resultObject.getAsJsonObject().get("value").getAsInt());
            jsonCampaignStatistics.add(statistics);
        }
        return jsonCampaignStatistics;
    }
}
