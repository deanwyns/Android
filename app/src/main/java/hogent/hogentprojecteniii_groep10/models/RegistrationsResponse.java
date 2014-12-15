package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Hulpklasse om registraties op te vangen van de server
 */
public class RegistrationsResponse {
    @SerializedName("data")
    private List<Registration> registrations;

    public List<Registration> getRegistrations() {
        return registrations;
    }
}
