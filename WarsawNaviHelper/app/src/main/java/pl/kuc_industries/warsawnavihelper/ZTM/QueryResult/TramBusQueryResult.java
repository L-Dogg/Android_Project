package pl.kuc_industries.warsawnavihelper.ZTM.QueryResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TramBusQueryResult {

    @SerializedName("Lat")
    @Expose
    private Double lat;
    @SerializedName("Lon")
    @Expose
    private Double lon;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("Lines")
    @Expose
    private String lines;
    @SerializedName("Brigade")
    @Expose
    private String brigade;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getBrigade() {
        return brigade;
    }

    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }

}