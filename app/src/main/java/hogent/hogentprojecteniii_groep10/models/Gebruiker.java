package hogent.hogentprojecteniii_groep10.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Gebruiker implements Parcelable {


    @SerializedName("email")
    private String emailadres;
    @SerializedName("password")
    private String password;
    @SerializedName("phone_number")
    private String telNr;
    @SerializedName("first_name_mother")
    private String naam;
    @SerializedName("last_name_mother")
    private String voornaam;
    @SerializedName("nrn_mother")
    private String rrnMoeder;
    @SerializedName("first_name_father")
    private String voornaamOuder2;
    @SerializedName("last_name_father")
    private String naamOuder2;
    @SerializedName("nrn_father")
    private String rrnVader;

    public Gebruiker(String emailadres, String password, String telNr, String naam, String voornaam, String rrnMoeder, String voornaamOuder2, String naamOuder2, String rrnVader) {
        this.emailadres = emailadres;
        this.password = password;
        this.telNr = telNr;
        this.naam = naam;
        this.voornaam = voornaam;
        this.rrnMoeder = rrnMoeder;
        this.voornaamOuder2 = voornaamOuder2;
        this.naamOuder2 = naamOuder2;
        this.rrnVader = rrnVader;
    }



    /*public Gebruiker(String naam, String voornaam, String telNr, String emailadres, String password, String passwordConfirmed) {
        this.naam = naam;
        this.voornaam = voornaam;
        this.telNr = telNr;
        this.emailadres = emailadres;
        this.password=password;
        this.passwordConfirmed=passwordConfirmed;
    }*/

    public String getNaam() {
        return naam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getTelNr() {
        return telNr;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public String getPassword() {
        return password;
    }

    public String getRrnMoeder() {
        return rrnMoeder;
    }

    public String getVoornaamOuder2() {
        return voornaamOuder2;
    }

    public String getNaamOuder2() {
        return naamOuder2;
    }

    public String getRrnVader() {
        return rrnVader;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emailadres);
        dest.writeString(password);
        dest.writeString(telNr);
        dest.writeString(naam);
        dest.writeString(voornaam);
        dest.writeString(rrnMoeder);
        dest.writeString(voornaamOuder2);
        dest.writeString(naamOuder2);
        dest.writeString(rrnVader);
    }

    private Gebruiker() {

    }

    public static final Parcelable.Creator<Gebruiker> CREATOR = new Creator<Gebruiker>() {
        public Gebruiker createFromParcel(Parcel source) {
            Gebruiker gebruiker = new Gebruiker();
            gebruiker.emailadres = source.readString();
            gebruiker.password = source.readString();
            gebruiker.telNr = source.readString();
            gebruiker.naam = source.readString();
            gebruiker.voornaam = source.readString();
            gebruiker.rrnMoeder = source.readString();
            gebruiker.voornaamOuder2 = source.readString();
            gebruiker.naamOuder2 = source.readString();
            gebruiker.rrnVader = source.readString();
            return gebruiker;
        }

        @Override
        public Gebruiker[] newArray(int size) {
            return new Gebruiker[size];
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
