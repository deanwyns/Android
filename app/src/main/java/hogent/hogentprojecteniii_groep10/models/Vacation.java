package hogent.hogentprojecteniii_groep10.models;

public class Vacation {

    private long id;
    private String title;
    private String description;
    private String promoText;
    private String location;
    private int ageFrom;
    private int ageTo;
    private String transportation;
    private int maxParticipants;
    private double baseCost;
    private double oneBmMemberCost;
    private double twoBmMemberCost;
    private boolean taxDeductable;

    public Vacation(long id, String title, String description, String promoText, String location, int ageFrom, int ageTo, String transportation, int maxParticipants, double baseCost, double oneBmMemberCost, double twoBmMemberCost, boolean taxDeductable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.promoText = promoText;
        this.location = location;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.transportation = transportation;
        this.maxParticipants = maxParticipants;
        this.baseCost = baseCost;
        this.oneBmMemberCost = oneBmMemberCost;
        this.twoBmMemberCost = twoBmMemberCost;
        this.taxDeductable = taxDeductable;
    }

    //"Basic" constructor
    public Vacation(long id, String title, String description, String promoText, String location) {
        this(id, title, description, promoText, location, 0, 0, null, 0, 0.0, 0.0, 0.0, false);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPromoText() {
        return promoText;
    }

    public String getLocation() {
        return location;
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public String getTransportation() {
        return transportation;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public double getOneBmMemberCost() {
        return oneBmMemberCost;
    }

    public double getTwoBmMemberCost() {
        return twoBmMemberCost;
    }

    public boolean isTaxDeductable() { return taxDeductable; }
}
