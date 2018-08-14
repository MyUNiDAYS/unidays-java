package com.myunidays;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;

public class TrackingClient implements Closeable {

    private final DirectTrackingDetails directTrackingDetails;
    private final String key;
    private final HttpAsyncClient httpClient;
    private final boolean ownsClient;

    public TrackingClient(DirectTrackingDetails directTrackingDetails, String key) {
        this(directTrackingDetails, key, createHttpClient(), true);
    }

    public TrackingClient(DirectTrackingDetails directTrackingDetails, String key, HttpAsyncClient client) {
        this(directTrackingDetails, key, client, false);
    }

    private TrackingClient(DirectTrackingDetails directTrackingDetails, String key, HttpAsyncClient httpClient, boolean ownsClient) {
        this.directTrackingDetails = directTrackingDetails;
        this.key = key;
        this.httpClient = httpClient;
        this.ownsClient = ownsClient;
    }

    private static HttpAsyncClient createHttpClient() {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        return client;
    }

    /**
     * ends a Server-to-Server Redemption Tracking Request.
     * @return HttpResponse of the resulting call.
     */
    public Future<HttpResponse> send() {
        return send(false);
    }

    /***
     * Sends a Server-to-Server Redemption Tracking Request.
     * @param sendTestParameter Set to true to enable test mode.
     * @return HttpResponse of the resulting call.
     */
    public Future<HttpResponse> send(boolean sendTestParameter) {
        URI uri = new UriGenerator(sendTestParameter).generateServerUrl(key, directTrackingDetails);
        HttpGet getRequest = new HttpGet(uri.toString());

        return httpClient.execute(getRequest, null);
    }

    @Override
    public void close() throws IOException {
        if (ownsClient && httpClient instanceof Closeable)
            ((Closeable) httpClient).close();
    }
}
