package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/**
 * De klasse die een Ouder voorstelt in de applicatie.
 * Is Parcelable om doorgave mogelijk te maken tussen activities.
 */
public class Ouder extends Gebruiker{

    @SerializedName("rrn_mother")
    private String rrnMoeder;
    @SerializedName("rrn_father")
    private String rrnVader;
    @SerializedName("last_name_father")
    private String naamVader;
    @SerializedName("first_name_father")
    private String voornaamVader;

    public Ouder(String naam, String voornaam, String rrnMoeder, String naamVader, String voornaamVader, String rrnVader,String telNr, String emailadres, String password) {
        super(emailadres, password, telNr, naam, voornaam);
        this.rrnMoeder = rrnMoeder;
        this.rrnVader = rrnVader;
        this.naamVader = naamVader;
        this.voornaamVader = voornaamVader;
    }

    public String getRrnMoeder() {
        return rrnMoeder;
    }

    public String getRrnVader() {
        return rrnVader;
    }

    public String getNaamVader() {
        return naamVader;
    }

    public String getVoornaamVader() {
        return voornaamVader;
    }

    /**
     * Constructor voor ouder die als parameter een parcel object meekrijgt
     * en die het parcel object uitleest en een ouder object aanmaakt
     * @param in de parcel die meegegeven wordt en de gegevens van de ouder bevat
     */
    protected Ouder(Parcel in) {
        super(in);
        rrnMoeder = in.readString();
        rrnVader = in.readString();
        naamVader = in.readString();
        voornaamVader = in.readString();
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
     * Maakt het mogelijk om ouder in een parcel te steken, roept de writeToParcel methode aan
     * in Gebruiker
     * @param dest is de parcel waarin de ouder terechtkomt
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(rrnMoeder);
        dest.writeString(rrnVader);
        dest.writeString(naamVader);
        dest.writeString(voornaamVader);
    }

    /**
     * Maakt het lezen van een ouder uit een parcel mogelijk
     * Gebruikt een constructor van ouder die als parameter een parcel object meekrijgt
     */
    public static final Parcelable.Creator<Ouder> CREATOR = new Parcelable.Creator<Ouder>() {
        @Override
        public Ouder createFromParcel(Parcel in) {
            return new Ouder(in);
        }

        @Override
        public Ouder[] newArray(int size) {
            return new Ouder[size];
        }
    };
}
