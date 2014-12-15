package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * De klasse die een Kind voorstelt in de applicatie.
 * Is Parcelable om doorgave mogelijk te maken tussen activities.
 */
public class Kind implements Parcelable {

    private long id;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("nrn")
    private String nrn;
    @SerializedName("street_name")
    private String streetName;
    @SerializedName("house_number")
    private String houseNumber;
    @SerializedName("city")
    private String city;
    @SerializedName("postal_code")
    private String postalCode;
    @SerializedName("date_of_birth")
    private String dateOfBirth;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public Kind(String naam, String voornaam, String rrn, String straat, String huisnummer, String stad, String postcode, String dateOfBirth) {
        this.lastName = naam;
        this.firstName = voornaam;
        this.nrn = rrn;
        this.streetName = straat;
        this.houseNumber = huisnummer;
        this.city = stad;
        this.postalCode = postcode;
        this.dateOfBirth=dateOfBirth;

    }

    public Kind(String lastName, String firstName, String nrn, String city) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.nrn = nrn;
        this.city = city;
    }

    public long getId() {
        return id;
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

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getCity() {
        return city;
    }


    public String getPostalCode() {
        return postalCode;
    }

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
        dest.writeString(streetName);
        dest.writeString(houseNumber);
        dest.writeString(city);
        dest.writeString(postalCode);
        dest.writeLong(id);
        dest.writeString(dateOfBirth);
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
            streetName = in.readString();
            houseNumber = in.readString();
            city = in.readString();
            postalCode = in.readString();
            id = in.readLong();
            dateOfBirth=in.readString();
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
