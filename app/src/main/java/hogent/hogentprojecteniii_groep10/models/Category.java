package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

/**
 * Een vakantie behoort tot een categorie.
 * Elke categorie heeft een afbeelding.
 * Deze afbeelding is een Base64 String
 */
public class Category {
    private int id;
    private String name;
    @SerializedName("photo_url")
    private String photoUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
