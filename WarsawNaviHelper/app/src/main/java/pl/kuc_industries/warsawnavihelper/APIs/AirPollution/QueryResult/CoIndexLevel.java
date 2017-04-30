
package pl.kuc_industries.warsawnavihelper.APIs.AirPollution.QueryResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoIndexLevel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("indexLevelName")
    @Expose
    private String indexLevelName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndexLevelName() {
        return indexLevelName;
    }

    public void setIndexLevelName(String indexLevelName) {
        this.indexLevelName = indexLevelName;
    }

}
