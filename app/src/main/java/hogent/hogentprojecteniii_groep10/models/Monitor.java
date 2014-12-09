package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabrice on 9/12/2014.
 */
public class Monitor extends Gebruiker {

    public Monitor(String emailadres, String password, String telNr, String naam, String voornaam) {
        super(emailadres, password, telNr, naam, voornaam);
    }

    /**
     * Constructor voor monitor die als parameter een parcel object meekrijgt
     * en die het parcel object uitleest om een monitor aan te maken
     * @param in de parcel die meegegeven wordt en die de gegevens van de monitor bevat
     */
    protected Monitor(Parcel in) {
        super(in);
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
        super.writeToParcel(dest, flags);
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
