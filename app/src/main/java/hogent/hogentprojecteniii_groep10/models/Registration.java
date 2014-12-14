package hogent.hogentprojecteniii_groep10.models;


import com.google.gson.annotations.SerializedName;

/**
 * Een registratie is een tussentabel tussen vacation en child.
 */
public class Registration {

    private long id;
    private boolean isPaid;
    @SerializedName("child_id")
    private long childId;
    @SerializedName("vacation_id")
    private long vacationId;

    public long getId() {
        return id;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public long getChildId() {
        return childId;
    }

    public long getVacationId() {
        return vacationId;
    }
}
