
package pl.kuc_industries.warsawnavihelper.APIs.Veturilo.QueryResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VeturiloQueryResult {

    @SerializedName("network")
    @Expose
    private Network network;

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

}
