package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fabrice on 12/12/2014.
 */
public class Address implements Parcelable {

    private long id;
    @SerializedName("street_name")
    private String streetName;
    @SerializedName("house_number")
    private String houseNumber;
    @SerializedName("city")
    private String city;
    @SerializedName("postal_code")
    private String postalCode;

    public Address(String streetName, String houseNumber, String city, String postalCode) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Address(long id, String streetName, String houseNumber, String city, String postalCode) {
        this.id = id;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
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

    public long getId(){
        return id;
    }

    protected Address(Parcel in) {
        id = in.readLong();
        streetName = in.readString();
        houseNumber = in.readString();
        city = in.readString();
        postalCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(streetName);
        dest.writeString(houseNumber);
        dest.writeString(city);
        dest.writeString(postalCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
