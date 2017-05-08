
package pl.kuc_industries.warsawnavihelper.APIs.Veturilo.QueryResult;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Extra {

    @SerializedName("bike_uids")
    @Expose
    private List<String> bikeUids = null;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("slots")
    @Expose
    private Integer slots;
    @SerializedName("uid")
    @Expose
    private String uid;

    public List<String> getBikeUids() {
        return bikeUids;
    }

    public void setBikeUids(List<String> bikeUids) {
        this.bikeUids = bikeUids;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getSlots() {
        return slots;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
