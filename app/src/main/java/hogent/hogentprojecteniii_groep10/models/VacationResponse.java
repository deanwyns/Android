package hogent.hogentprojecteniii_groep10.models;

import java.util.Arrays;
import java.util.List;

//Backend geeft een Object terug met een lijst van objecten in dus deze VacationResponse klasse
public class VacationResponse {
    private List<Vacation> vacations;

    public List<Vacation> getVacations(){
        return vacations;
    }

}
