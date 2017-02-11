package org.openhab.binding.nest.internal.data;

import com.google.gson.annotations.SerializedName;

/**
 * Deals with the access token data that comes back from nest when it is requested.
 *
 * @author David Bennett - Initial Contribution
 */
public class AccessTokenData {
    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private Long expiresIn;
}
