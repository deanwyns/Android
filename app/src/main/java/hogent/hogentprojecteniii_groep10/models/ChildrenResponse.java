package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Hulpklasse om de query op te vangen die kinderen teruggeeft.
 */
public class ChildrenResponse {
    @SerializedName("data")
    private List<Kind> children;

    public List<Kind> getChildren() {
        return children;
    }
}
