package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
/**
 * De klasse die een Gebruiker voorstelt in de applicatie.
 * Is Parcelable om doorgave mogelijk te maken tussen activities.
 */
public class Gebruiker implements Parcelable{

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

    public Gebruiker(String emailadres, String password, String telNr, String naam, String voornaam) {
        this.emailadres = emailadres;
        this.password = password;
        this.telNr = telNr;
        this.naam = naam;
        this.voornaam = voornaam;
    }

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

    @Override
    public String toString() {
        return this.voornaam + " " + this.naam;
    }

    /**
     * Constructor voor gebruiker die als parameter een parcel object meekrijgt
     * en die het parcel object uitleest om een gebruiker aan te maken
     * @param in de parcel die meegegeven wordt en die de gegevens van de gebruiker bevat
     */
    protected Gebruiker(Parcel in) {
        emailadres = in.readString();
        password = in.readString();
        telNr = in.readString();
        naam = in.readString();
        voornaam = in.readString();
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
     * Maakt het mogelijk om gebruiker in een parcel te steken
     * @param dest is de parcel waarin de gebruiker terechtkomt
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emailadres);
        dest.writeString(password);
        dest.writeString(telNr);
        dest.writeString(naam);
        dest.writeString(voornaam);
    }
    /**
     * Maakt het lezen van een gebruiker uit een parcel mogelijk
     * Gebruikt een constructor van gebruiker die als parameter een parcel object meekrijgt
     */
    public static final Parcelable.Creator<Gebruiker> CREATOR = new Parcelable.Creator<Gebruiker>() {
        @Override
        public Gebruiker createFromParcel(Parcel in) {
            return new Gebruiker(in);
        }

        @Override
        public Gebruiker[] newArray(int size) {
            return new Gebruiker[size];
        }
    };
}
