package hogent.hogentprojecteniii_groep10.models;

import java.util.List;

/**
 * Created by Nico Verbeeke on 7/11/2014.
 */

//Backend geeft een Object terug met een lijst van objecten in dus deze VacationResponse klasse
public class VacationResponse {
    private List<Vacation> vacations;

    public List<Vacation> getVacations(){
        return vacations;
    }
}
