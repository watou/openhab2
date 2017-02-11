package org.openhab.binding.nest.config;

/**
 * This has the configuration for the nest bridge, allowing it to talk to nest.
 *
 * @author David Bennett - initial contribution
 */
public class NestBridgeConfiguration {
    /** Client id from the nest product page. */
    public String clientId;
    /** Client secret from the nest product page. */
    public String clientSecret;
    /** Client secret from the auth page. */
    public String pincode;
    /** The access token to use once retrieved from nest. */
    public String accessToken;
    /** How often to refresh data from nest. */
    public int refreshInterval;
}
