package com.myunidays;

import java.net.URI;
import java.util.Objects;

/***
 * UNiDAYS Java Library for the Tracking API.
 */
public class TrackingHelper {
    private final DirectTrackingDetails directTrackingDetails;

    /***
     * Creates a TrackingHelper using the specified details.
     * @param directTrackingDetails the tracking details.
     */
    public TrackingHelper(DirectTrackingDetails directTrackingDetails) {
        Objects.requireNonNull(directTrackingDetails, "directTrackingDetails must not be null");

        if (directTrackingDetails.getPartnerId() == null || directTrackingDetails.getPartnerId().isEmpty())
            throw new IllegalArgumentException("PartnerId is required");
        if (directTrackingDetails.getCurrency() == null || directTrackingDetails.getCurrency().isEmpty())
            throw new IllegalArgumentException("Currency is required");
        if (directTrackingDetails.getTransactionId() == null || directTrackingDetails.getTransactionId().isEmpty())
            throw new IllegalArgumentException("TransactionId is required");

        this.directTrackingDetails = directTrackingDetails;
    }

    /***
     * Generates the Server-to-Server Redemption Tracking URL.
     * @param key The key for the signature.
     * @return The URL to make a server-to-server request to.
     */
    public URI trackingServerUrl(String key) {
        return new UriGenerator(false).generateServerUrl(key, directTrackingDetails);
    }

    /***
     * Generates the Server-to-Server Redemption Tracking URL in Test Mode.
     * @param key The key for the signature.
     * @return The test URL to make a server-to-server request to.
     */
    public URI trackingServerTestUrl(String key) {
        return new UriGenerator(true).generateServerUrl(key, directTrackingDetails);
    }

    /***
     * Generates the Redemption Tracking URL.
     * @return The URL to be placed inside an &lt;img /&gt; element in your receipt page. The image returned is a 1x1px transparent gif.
     */
    public URI trackingScriptUrl() {
        return new UriGenerator(false).generateScriptUrl(directTrackingDetails);
    }

    /***
     * Generates the Redemption Tracking URL.
     * @param key The key for the signature.
     * @return The URL to be placed inside an &lt;img /&gt; element in your receipt page. The image returned is a 1x1px transparent gif
     */
    public URI trackingScriptUrl(String key) {
        return new UriGenerator(false).generateScriptUrl(key, directTrackingDetails);
    }

    /***
     * Generates the Redemption Tracking URL in Test Mode.
     * @return The Test URL to be placed inside an &lt;img /&gt; element in your receipt page. The image returned is a 1x1px transparent gif.
     */
    public URI trackingScriptTestUrl() {
        return new UriGenerator(true).generateScriptUrl(directTrackingDetails);
    }

    /***
     * Generates the Redemption Tracking URL in Test Mode.
     * @param key The key for the signature.
     * @return The test URL to be placed inside an &lt;img /&gt; element in your receipt page. The image returned is a 1x1px transparent gif.
     */
    public URI trackingScriptTestUrl(String key) {
        return new UriGenerator(true).generateScriptUrl(key, directTrackingDetails);
    }
}
