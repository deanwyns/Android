package hogent.hogentprojecteniii_groep10.models;


import com.google.gson.annotations.SerializedName;

/**
 * Een registratie is een tussentabel tussen vacation en child.
 */
public class Registration {

    private long id;
    @SerializedName("is_paid")
    private int isPaid;
    @SerializedName("child_id")
    private long childId;
    @SerializedName("vacation_id")
    private long vacationId;
    @SerializedName("child_first_name")
    private String childFirstName;
    @SerializedName("child_last_name")
    private String childLastName;
    @SerializedName("vacation_title")
    private String vacationTitle;
    @SerializedName("facturation_first_name")
    private String facturationFirstName;
    @SerializedName("facturation_last_name")
    private String facturationLastName;
    @SerializedName("facturation_address")
    private Address facturation_address;

    public long getId() {
        return id;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public long getChildId() {
        return childId;
    }

    public long getVacationId() {
        return vacationId;
    }

    public String getChildFirstName() {
        return childFirstName;
    }

    public String getChildLastName() {
        return childLastName;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public String getFacturationFirstName() {
        return facturationFirstName;
    }

    public String getFacturationLastName() {
        return facturationLastName;
    }

    public Address getFacturation_address() {
        return facturation_address;
    }
}
