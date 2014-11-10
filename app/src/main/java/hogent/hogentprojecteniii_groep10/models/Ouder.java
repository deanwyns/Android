package hogent.hogentprojecteniii_groep10.models;

/**
 * Created by Fabrice on 7/11/2014.
 */
public class Ouder extends Gebruiker {

    private String rrnMoeder;
    private String rrnVader;
    private String naamOuder2;
    private String voornaamOuder2;

    public Ouder(String naam, String voornaam, String rrnMoeder, String naamOuder2, String voornaamOuder2, String rrnVader,String telNr, String emailadres ) {
        super(naam, voornaam, telNr, emailadres);
        this.rrnMoeder = rrnMoeder;
        this.rrnVader = rrnVader;
        this.naamOuder2 = naamOuder2;
        this.voornaamOuder2 = voornaamOuder2;
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
}
