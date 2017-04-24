
package pl.kuc_industries.warsawnavihelper.ZTM.QueryResult;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Lat",
    "Lon",
    "Time",
    "Lines",
    "Brigade"
})
public class TramBusQueryResult {

    @JsonProperty("Lat")
    private Double lat;
    @JsonProperty("Lon")
    private Double lon;
    @JsonProperty("Time")
    private String time;
    @JsonProperty("Lines")
    private String lines;
    @JsonProperty("Brigade")
    private String brigade;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Lat")
    public Double getLat() {
        return lat;
    }

    @JsonProperty("Lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @JsonProperty("Lon")
    public Double getLon() {
        return lon;
    }

    @JsonProperty("Lon")
    public void setLon(Double lon) {
        this.lon = lon;
    }

    @JsonProperty("Time")
    public String getTime() {
        return time;
    }

    @JsonProperty("Time")
    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("Lines")
    public String getLines() {
        return lines;
    }

    @JsonProperty("Lines")
    public void setLines(String lines) {
        this.lines = lines;
    }

    @JsonProperty("Brigade")
    public String getBrigade() {
        return brigade;
    }

    @JsonProperty("Brigade")
    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
