package online.omnia.exoclicks;

import java.util.Date;

/**
 * Created by lollipop on 31.10.2017.
 */
public class JsonCampaignStatistics {
    private int hits;
    private Date ddate;
    private int clicks;
    private int value;
    private double ctr;
    private double cpm;
    private double avgCpc;
    private double cost;
    private int impressions;
    private double cpv;

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public Date getDdate() {
        return ddate;
    }

    public void setDdate(Date ddate) {
        this.ddate = ddate;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getCtr() {
        return ctr;
    }

    public void setCtr(double ctr) {
        this.ctr = ctr;
    }

    public double getCpm() {
        return cpm;
    }

    public void setCpm(double cpm) {
        this.cpm = cpm;
    }

    public double getAvgCpc() {
        return avgCpc;
    }

    public void setAvgCpc(double avgCpc) {
        this.avgCpc = avgCpc;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }

    public double getCpv() {
        return cpv;
    }

    public void setCpv(double cpv) {
        this.cpv = cpv;
    }

    @Override
    public String toString() {
        return "JsonCampaignStatistics{" +
                "hits=" + hits +
                ", ddate=" + ddate +
                ", clicks=" + clicks +
                ", value=" + value +
                ", ctr=" + ctr +
                ", cpm=" + cpm +
                ", avgCpc=" + avgCpc +
                ", cost=" + cost +
                ", impressions=" + impressions +
                ", cpv=" + cpv +
                '}';
    }
}
