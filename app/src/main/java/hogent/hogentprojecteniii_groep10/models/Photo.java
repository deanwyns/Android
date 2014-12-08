package hogent.hogentprojecteniii_groep10.models;

/**
 * Een foto object dat bij een kamp kan horen.
 * Bevat een id, de url naar de thumbnail, de url naar de volledige afbeelding, een titel en een beschrijving.
 */
public class Photo {
    private long id;
    private String thumbnail;
    private String fullsize;
    private String title;
    private String summary;

    public long getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getFullsize() {
        return fullsize;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }
}
