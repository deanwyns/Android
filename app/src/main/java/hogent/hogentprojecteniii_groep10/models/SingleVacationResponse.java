package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

/**
 * HulpObject om vakanties op te vragen
 */
public class SingleVacationResponse {
    @SerializedName("data")
    private Vacation vacation;

    public Vacation getVacation() {
        return vacation;
    }
}
