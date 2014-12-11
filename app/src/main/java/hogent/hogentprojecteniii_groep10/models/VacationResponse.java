package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

//Backend geeft een Object terug met een lijst van objecten in dus deze VacationResponse klasse
public class VacationResponse {
    @SerializedName("data")
    private List<Vacation> vacations;

    public List<Vacation> getVacations(){
        return vacations;
    }

}
