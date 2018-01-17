package online.omnia.exoclicks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by lollipop on 30.10.2017.
 */
public class Main {
    public static int days;
    public static long deltaTime = 24L * 60 * 60 * 1000;

    public static void main(String[] args) throws IOException {
        if (args.length !=1) return;
        if (!args[0].matches("\\d+")) return;
        if (Integer.parseInt(args[0]) == 0) {
            days = 0;
            deltaTime = 0;
        }
        days = Integer.parseInt(args[0]);

        List<AccountsEntity> accountsEntities = MySQLDaoImpl.getInstance().getAccountsEntities("exoclick");
        GsonBuilder campaignBuilder = new GsonBuilder();
        campaignBuilder.registerTypeAdapter(List.class, new JsonCampaignListDeserializer());
        campaignBuilder.registerTypeAdapter(JsonTokenEntity.class, new JsonTokenDeserializer());
        Gson gsonCampaign = campaignBuilder.create();
        GsonBuilder statisticsBuilder = new GsonBuilder();
        statisticsBuilder.registerTypeAdapter(List.class, new JsonCampaignStatisticsDeserializer());
        statisticsBuilder.registerTypeAdapter(String.class, new JsonClickUrlDeserializer());
        Gson gsonStatistics = statisticsBuilder.create();
        String answer;
        List<JsonCampaignEntity> jsonCampaignEntities;
        List<JsonCampaignStatistics> jsonCampaignStatisticsList;
        SourceStatisticsEntity sourceStatisticsEntity;
        JsonTokenEntity jsonTokenEntity;
        SourceStatisticsEntity statisticsEntity;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC/Etc"));
        java.util.Date date = new java.util.Date();
        for (AccountsEntity accountsEntity : accountsEntities) {
           answer = HttpMethodUtils.getToken(accountsEntity.getApiKey());

           jsonTokenEntity = gsonCampaign.fromJson(answer, JsonTokenEntity.class);
           answer = HttpMethodUtils.getMethod("https://api.exoclick.com/v2/campaigns?limit=1000", jsonTokenEntity.getToken());

           jsonCampaignEntities = gsonCampaign.fromJson(answer, List.class);
           String url;
            Map<String, String> parameters;
           for (JsonCampaignEntity jsonCampaignEntity : jsonCampaignEntities) {
                answer = HttpMethodUtils.getMethod("https://api.exo" +
                                "click.com/v2/statistics/advertiser/date?campaignid="
                        + jsonCampaignEntity.getId() + "&date-from=" + simpleDateFormat.format(new java.util.Date(date.getTime() - deltaTime - days * 24L * 60 * 60 * 1000))
                        + "&date-to=" + simpleDateFormat.format(new java.util.Date(date.getTime() - deltaTime)),
                        jsonTokenEntity.getToken());
                System.out.println(answer);
                jsonCampaignStatisticsList = gsonStatistics.fromJson(answer, List.class);
                for (JsonCampaignStatistics statistics : jsonCampaignStatisticsList) {
                    sourceStatisticsEntity = new SourceStatisticsEntity();
                    sourceStatisticsEntity.setAccount_id(accountsEntity.getAccountId());
                    sourceStatisticsEntity.setBuyerId(accountsEntity.getBuyerId());
                    answer = HttpMethodUtils.getMethod("https://api.exoclick.com/v2/campaigns/" + jsonCampaignEntity.getId(), jsonTokenEntity.getToken());
                    url = gsonStatistics.fromJson(answer, String.class);
                    if (url != null) {
                        parameters = Utils.getUrlParameters(url);
                        if (parameters.containsKey("cab")) {
                            if (parameters.get("cab").matches("\\d+")
                                    && MySQLDaoImpl.getInstance().getAffiliateByAfid(Integer.parseInt(parameters.get("cab"))) != null) {
                                sourceStatisticsEntity.setAfid(Integer.parseInt(parameters.get("cab")));
                            } else {
                                sourceStatisticsEntity.setAfid(0);
                            }
                        }
                        else sourceStatisticsEntity.setAfid(2);
                    }
                    sourceStatisticsEntity.setCampaignId(String.valueOf(jsonCampaignEntity.getId()));
                    sourceStatisticsEntity.setCampaignName(jsonCampaignEntity.getName());
                    sourceStatisticsEntity.setClicks(statistics.getClicks());
                    sourceStatisticsEntity.setCpc(statistics.getAvgCpc());
                    sourceStatisticsEntity.setCpm(statistics.getCpm());
                    sourceStatisticsEntity.setCtr(statistics.getCtr());
                    try {
                        sourceStatisticsEntity.setDate(new Date(dateFormat.parse(dateFormat.format(statistics.getDdate())).getTime() + 4 * 60L * 60 * 1000));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sourceStatisticsEntity.setReceiver("API");
                    sourceStatisticsEntity.setSpent(statistics.getCost());
                    sourceStatisticsEntity.setImpressions(statistics.getImpressions());
                    System.out.println(jsonCampaignEntity.getId());
                    System.out.println(sourceStatisticsEntity);
                    if (Main.days != 0) {
                        statisticsEntity = MySQLDaoImpl.getInstance()
                                .getSourceStatistics(sourceStatisticsEntity.getAccount_id(),
                                        sourceStatisticsEntity.getCampaignId(),
                                        sourceStatisticsEntity.getDate());
                        if (statisticsEntity == null) {
                            MySQLDaoImpl.getInstance().addSourceStatistics(sourceStatisticsEntity);
                        } else {
                            sourceStatisticsEntity.setId(statisticsEntity.getId());
                            MySQLDaoImpl.getInstance().updateSourceStatistics(sourceStatisticsEntity);
                            statisticsEntity = null;
                        }
                    }
                    else {
                        if (MySQLDaoImpl.getInstance().isDateInTodayAdsets(sourceStatisticsEntity.getDate(), sourceStatisticsEntity.getAccount_id(), sourceStatisticsEntity.getCampaignId())) {
                            MySQLDaoImpl.getInstance().updateTodayAdset(Utils.getAdset(sourceStatisticsEntity));
                        } else MySQLDaoImpl.getInstance().addTodayAdset(Utils.getAdset(sourceStatisticsEntity));

                    }
                }
           }

        }
        MySQLDaoImpl.getSessionFactory().close();
    }
}
