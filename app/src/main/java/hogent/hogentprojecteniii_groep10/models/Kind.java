package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fabrice on 21/11/2014.
 */
public class Kind implements Parcelable{

    @SerializedName("lastName")
    private String naam;
    @SerializedName("firstName")
    private String voornaam;
    @SerializedName("nrn")
    private String rrn;
    @SerializedName("streetName")
    private String straat;
    @SerializedName("houseNumber")
    private String huisnummer;
    @SerializedName("city")
    private String stad;
    /*@SerializedName("lastName")
    private String postcode;*/

    public Kind(String naam, String voornaam, String rrn, String straat, String huisnummer, String stad/*, String postcode*/) {
        this.naam = naam;
        this.voornaam = voornaam;
        this.rrn = rrn;
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.stad = stad;
        //this.postcode = postcode;
    }

    public String getNaam() {
        return naam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getRrn() {
        return rrn;
    }

    public String getStraat() {
        return straat;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public String getStad() {
        return stad;
    }

   /*public String getPostcode() {
        return postcode;
    }*/

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naam);
        dest.writeString(voornaam);
        dest.writeString(rrn);
        dest.writeString(straat);
        dest.writeString(huisnummer);
        dest.writeString(stad);
        //dest.writeString(postcode);
    }

    private Kind() {

    }

    public static final Parcelable.Creator<Kind> CREATOR = new Parcelable.Creator<Kind>() {
        public Kind createFromParcel(Parcel source) {
            Kind kind = new Kind();
            kind.naam = source.readString();
            kind.voornaam = source.readString();
            kind.rrn = source.readString();
            kind.straat = source.readString();
            kind.huisnummer = source.readString();
            kind.stad = source.readString();
            //kind.postcode = source.readString();

            return kind;
        }

        @Override
        public Kind[] newArray(int size) {
            return new Kind[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return this.voornaam + " " + this.naam;
    }

}
