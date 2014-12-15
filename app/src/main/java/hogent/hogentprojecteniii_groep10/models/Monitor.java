package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * De klasse die een Monitor voorstelt in de applicatie.
 * Is Parcelable om doorgave mogelijk te maken tussen activities.
 */
public class Monitor implements Parcelable  {

    private long id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;

    public Monitor(String naam, String voornaam) {
        this.firstName = voornaam;
        this.lastName = naam;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String toString(){
        return firstName + " " + lastName;
    }
    /**
     * Constructor voor monitor die als parameter een parcel object meekrijgt
     * en die het parcel object uitleest om een monitor aan te maken
     * @param in de parcel die meegegeven wordt en die de gegevens van de monitor bevat
     */
    protected Monitor(Parcel in) {
        id = in.readLong();
        firstName = in.readString();
        lastName = in.readString();
    }
    /**
     * Nodig voor elke parcellable. In 99% van de gevallen is return 0 goed.
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Maakt het mogelijk om monitor in een parcel te steken
     * @param dest is de parcel waarin de monitor terechtkomt
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
    /**
     * Maakt het lezen van een monitor uit een parcel mogelijk
     * Gebruikt een constructor van monitor die als parameter een parcel object meekrijgt
     */
    public static final Parcelable.Creator<Monitor> CREATOR = new Parcelable.Creator<Monitor>() {
        @Override
        public Monitor createFromParcel(Parcel in) {
            return new Monitor(in);
        }

        @Override
        public Monitor[] newArray(int size) {
            return new Monitor[size];
        }
    };
}
