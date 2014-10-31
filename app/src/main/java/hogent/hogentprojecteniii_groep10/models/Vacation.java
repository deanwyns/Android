package hogent.hogentprojecteniii_groep10.models;

public class Vacation {
    private String title;
    private String description;
    private int signups;

    public Vacation(String title, String description, int signups) {
        this.title = title;
        this.description = description;
        this.signups = signups;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getSignups() {
        return signups;
    }
}
