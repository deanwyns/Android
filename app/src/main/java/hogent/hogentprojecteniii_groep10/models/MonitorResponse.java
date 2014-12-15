package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Klasse die monitor query opvangt.
 */
public class MonitorResponse {
    @SerializedName("users_monitors")
    private List<Monitor> monitors;

    public List<Monitor> getMonitors() {
        return monitors;
    }
}
