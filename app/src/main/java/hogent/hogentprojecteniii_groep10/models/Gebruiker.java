package hogent.hogentprojecteniii_groep10.models;


/**
 * Created by Fabrice on 7/11/2014.
 */
public class Gebruiker {

    private String naam;
    private String voornaam;
    private String telNr;
    private String emailadres;

    public Gebruiker(String naam, String voornaam, String telNr, String emailadres) {
        this.naam = naam;
        this.voornaam = voornaam;
        this.telNr = telNr;
        this.emailadres = emailadres;
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

}
