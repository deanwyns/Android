package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * De klasse die een Kind voorstelt in de applicatie.
 * Is Parcelable om doorgave mogelijk te maken tussen activities.
 */
public class Kind implements Parcelable {

    @SerializedName("lastName")
    private String lastName;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("nrn")
    private String nrn;
    /*@SerializedName("streetName")
    private String streetName;
    @SerializedName("houseNumber")
    private String houseNumber;*/
    @SerializedName("city")
    private String city;
    /*@SerializedName("postalCode")
    private String postalCode;*/

    public Kind(String naam, String voornaam, String rrn/*, String straat, String huisnummer*/, String stad/*, String postcode*/) {
        this.lastName = naam;
        this.firstName = voornaam;
        this.nrn = rrn;
        /*this.streetName = straat;
        this.houseNumber = huisnummer;*/
        this.city = stad;
        /*this.postalCode = postcode;*/
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNrn() {
        return nrn;
    }

    /*public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }
*/
    public String getCity() {
        return city;
    }

/*
    public String getPostalCode() {
        return postalCode;
    }*/

    /**
     * Maakt het mogelijk om kind in een parcel te steken
     * @param dest is de parcel waarin het kind terechtkomt
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lastName);
        dest.writeString(firstName);
        dest.writeString(nrn);
        /*dest.writeString(streetName);
        dest.writeString(houseNumber);*/
        dest.writeString(city);
        /*dest.writeString(postalCode);*/
    }
    /**
     * Constructor voor Kind die als parameter een parcel object meekrijgt
     * en die het parcel object uitleest om een kind aan te maken
     * @param in de parcel die meegegeven wordt en die de gegevens van de monitor bevat
     */
        protected Kind(Parcel in) {
            lastName = in.readString();
            firstName = in.readString();
            nrn = in.readString();
            /*streetName = in.readString();
            houseNumber = in.readString();*/
            city = in.readString();
            /*postalCode = in.readString();*/
        }


    /**
     * Maakt het lezen van een kind uit een parcel mogelijk
     * Gebruikt een constructor van kind die als parameter een parcel object meekrijgt
     */
        public static final Parcelable.Creator<Kind> CREATOR = new Parcelable.Creator<Kind>() {
            @Override
            public Kind createFromParcel(Parcel in) {
                return new Kind(in);
            }

            @Override
            public Kind[] newArray(int size) {
                return new Kind[size];
            }
        };

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
    /**
     * Nodig voor elke parcellable. In 99% van de gevallen is return 0 goed.
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

}
