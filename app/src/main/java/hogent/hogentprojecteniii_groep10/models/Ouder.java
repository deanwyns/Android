package hogent.hogentprojecteniii_groep10.models;

/**
 * Created by Fabrice on 7/11/2014.
 */
public class Ouder extends Gebruiker {

    private String rrn_moeder;
    private String rrn_vader;
    private String naam_ouder2;
    private String voornaam_ouder2;

    public Ouder(String naam, String voornaam, String rrn_moeder, String naam_ouder2, String voornaam_ouder2, String rrn_vader,String telNr, String emailadres ) {
        super(naam, voornaam, telNr, emailadres);
        this.rrn_moeder = rrn_moeder;
        this.rrn_vader = rrn_vader;
        this.naam_ouder2 = naam_ouder2;
        this.voornaam_ouder2 = voornaam_ouder2;
    }
}
