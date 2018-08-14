package com.myunidays;

import java.net.URI;
import java.net.URISyntaxException;

final class UriGenerator {
    private static final String TrackingUrl = "https://api.myunidays.com/tracking/v1.2/redemption";
    private static final String TrackingGifUrl = TrackingUrl + "/gif";
    private final boolean generateTestUris;

    UriGenerator(boolean generateTestUris) {
        this.generateTestUris = generateTestUris;
    }

    URI generateScriptUrl(DirectTrackingDetails directTrackingDetails) {
        try {
            return new URI(new TrackingUriBuilder()
                    .appendTrackingParameters(directTrackingDetails)
                    .appendTestParameter(generateTestUris)
                    .prepend(TrackingGifUrl)
                    .toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    URI generateScriptUrl(String key, DirectTrackingDetails directTrackingDetails) {
        try {
            return new URI(new TrackingUriBuilder()
                    .appendTrackingParameters(directTrackingDetails)
                    .appendSignature(key)
                    .appendTestParameter(generateTestUris)
                    .prepend(TrackingGifUrl)
                    .toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    URI generateServerUrl(String key, DirectTrackingDetails directTrackingDetails) {
        try {
            return new URI(new TrackingUriBuilder()
                    .appendTrackingParameters(directTrackingDetails)
                    .appendSignature(key)
                    .appendTestParameter(generateTestUris)
                    .prepend(TrackingUrl)
                    .toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

