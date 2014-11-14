package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fabrice on 7/11/2014.
 */
public class Ouder {

    @SerializedName("rrn_mother")
    private String rrnMoeder;
    @SerializedName("rrn_father")
    private String rrnVader;
    @SerializedName("last_name_father")
    private String naamOuder2;
    @SerializedName("first_name_father")
    private String voornaamOuder2;

/*
    public Ouder(String naam, String voornaam, String rrnMoeder, String naamOuder2, String voornaamOuder2, String rrnVader,String telNr, String emailadres, String password, String passwordConfirmed ) {
        super(naam, voornaam, telNr, emailadres, password, passwordConfirmed);
        this.rrnMoeder = rrnMoeder;
        this.rrnVader = rrnVader;
        this.naamOuder2 = naamOuder2;
        this.voornaamOuder2 = voornaamOuder2;
    }

    public Ouder(String naam, String voornaam,String rrnMoeder, String password, String passwordConfirmed, String telNr, String emailadres){
        super(naam, voornaam, telNr, emailadres, password, passwordConfirmed);
        this.rrnMoeder = rrnMoeder;
    }

    public String getRrnMoeder() {
        return rrnMoeder;
    }

    public String getRrnVader() {
        return rrnVader;
    }

    public String getNaamOuder2() {
        return naamOuder2;
    }

    public String getVoornaamOuder2() {
        return voornaamOuder2;
    }
       */
}
