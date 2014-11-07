package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

public class LoginToken {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public int expiresIn;

    @Override
    public String toString() {
        return String.format("%s %s", tokenType, accessToken);
    }
}
