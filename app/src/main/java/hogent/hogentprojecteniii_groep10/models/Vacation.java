package hogent.hogentprojecteniii_groep10.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * De klasse die een vakantie voorstelt in de applicatie.
 * Is Parcelable om doorgave mogelijk te maken tussen activities.
 */
public class Vacation implements Parcelable {

    private long id;
    private String title;
    private String description;
    private String promoText;
    private String location;
    private Date beginDate;
    private Date endDate;
    private int ageFrom;
    private int ageTo;
    private String transportation;
    private int maxParticipants;
    private double baseCost;
    private double oneBmMemberCost;
    private double twoBmMemberCost;
    private int taxDeductable;
    private int currentParticipants;
    private int category_id;
    private int likes;



    public Vacation(long id, String title, String description, String promoText, String location, Date beginDate, Date endDate, int ageFrom, int ageTo, String transportation, int maxParticipants, double baseCost, double oneBmMemberCost, double twoBmMemberCost, int taxDeductable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.promoText = promoText;
        this.location = location;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.transportation = transportation;
        this.maxParticipants = maxParticipants;
        this.baseCost = baseCost;
        this.oneBmMemberCost = oneBmMemberCost;
        this.twoBmMemberCost = twoBmMemberCost;
        this.taxDeductable = taxDeductable;
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

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
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

    public int isTaxDeductable() {
        return taxDeductable;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getLikes() {
        return likes;
    }
    /**
     * Maakt het mogelijk om vacation in een parcel te steken.
     * @param dest is de parcel waarin vacation terecht komt
     * @param flags
     */
    //Parcellable is sneller dan serializable wanneer we objecten doorgeven in intents.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(promoText);
        dest.writeString(location);
        dest.writeSerializable(beginDate);
        dest.writeSerializable(endDate);
        dest.writeInt(ageFrom);
        dest.writeInt(ageTo);
        dest.writeString(transportation);
        dest.writeInt(maxParticipants);
        dest.writeDouble(baseCost);
        dest.writeDouble(oneBmMemberCost);
        dest.writeDouble(twoBmMemberCost);
        //Er is geen writeBoolean, dus een omweg maken.
        //Lezen is dan via: boolean = in.readByte() != 0;
        // boolean == true if byte != 0
        dest.writeByte((byte) (taxDeductable==1 ? 1 : 0));
        dest.writeInt(currentParticipants);
        dest.writeInt(category_id);
        dest.writeInt(likes);
    }

    /**
     * Lege private constructor is nodig bij het maken van parcellables
     */
    private Vacation() {
    }

    /**
     * Maakt het lezen van een vacation uit een parcel mogelijk
     */
    public static final Parcelable.Creator<Vacation> CREATOR = new Creator<Vacation>() {
        public Vacation createFromParcel(Parcel source) {
            Vacation vacation = new Vacation();
            vacation.id = source.readLong();
            vacation.title = source.readString();
            vacation.description = source.readString();
            vacation.promoText = source.readString();
            vacation.location = source.readString();
            vacation.beginDate = (Date) source.readSerializable();
            vacation.endDate = (Date) source.readSerializable();
            vacation.ageFrom = source.readInt();
            vacation.ageTo = source.readInt();
            vacation.transportation = source.readString();
            vacation.maxParticipants = source.readInt();
            vacation.baseCost = source.readDouble();
            vacation.oneBmMemberCost = source.readDouble();
            vacation.twoBmMemberCost = source.readDouble();
            vacation.taxDeductable = source.readByte() != 0 ? 1:0;
            vacation.currentParticipants = source.readInt();
            vacation.category_id = source.readInt();
            vacation.likes = source.readInt();
            return vacation;
        }

        public Vacation[] newArray(int size) {
            return new Vacation[size];
        }
    };

    /**
     * Nodig voor elke parcellable. In 99% van de gevallen is return 0 goed.
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }


}
