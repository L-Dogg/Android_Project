
package pl.kuc_industries.warsawnavihelper.APIs.ZTM.QueryResult;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import com.google.gson.annotations.Expose;

public class TramBus{

    @SerializedName("result")
    @Expose
    private List<TramBusQueryResult> result = null;

    public List<TramBusQueryResult> getResult() {
        return result;
    }

    public void setResult(List<TramBusQueryResult> result) {
        this.result = result;
    }

}