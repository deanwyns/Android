package hogent.hogentprojecteniii_groep10.models;

import com.google.gson.annotations.SerializedName;

/**
 * De token die het inloggen voorstelt. Verkregen van de server.
 */
public class LoginToken {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return String.format("%s %s", tokenType, accessToken);
    }
}
