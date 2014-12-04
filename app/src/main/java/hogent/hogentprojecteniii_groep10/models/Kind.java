package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fabrice on 21/11/2014.
 */

public class Kind implements Parcelable {

    @SerializedName("lastName")
    private String lastName;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("nrn")
    private String nrn;
    @SerializedName("streetName")
    private String streetName;
    @SerializedName("houseNumber")
    private String houseNumber;
    @SerializedName("city")
    private String city;
    @SerializedName("postalCode")
    private String postalCode;

    public Kind(String naam, String voornaam, String rrn, String straat, String huisnummer, String stad, String postcode) {
        this.lastName = naam;
        this.firstName = voornaam;
        this.nrn = rrn;
        this.streetName = straat;
        this.houseNumber = huisnummer;
        this.city = stad;
        this.postalCode = postcode;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lastName);
        dest.writeString(firstName);
        dest.writeString(nrn);
        dest.writeString(streetName);
        dest.writeString(houseNumber);
        dest.writeString(city);
        dest.writeString(postalCode);
    }

        protected Kind(Parcel in) {
            lastName = in.readString();
            firstName = in.readString();
            nrn = in.readString();
            streetName = in.readString();
            houseNumber = in.readString();
            city = in.readString();
            postalCode = in.readString();
        }



        @SuppressWarnings("unused")
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

    @Override
    public int describeContents() {
        return 0;
    }

}
