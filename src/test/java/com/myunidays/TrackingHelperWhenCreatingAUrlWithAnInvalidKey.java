package com.myunidays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;

@RunWith(Parameterized.class)
public class TrackingHelperWhenCreatingAUrlWithAnInvalidKey {
    private final String key;
    private final String expectedMessage;
    private final BiFunction<TrackingHelper, String, URI> uriGenerator;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public TrackingHelperWhenCreatingAUrlWithAnInvalidKey(String key, BiFunction<TrackingHelper, String, URI> uriGenerator, String expectedMessage) {
        this.key = key;
        this.expectedMessage = expectedMessage;
        this.uriGenerator = uriGenerator;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                testCase("", TrackingHelper::trackingServerUrl, "Key cannot be empty"),
                testCase(null, TrackingHelper::trackingServerUrl, "Key cannot be null"),
                testCase("", TrackingHelper::trackingServerTestUrl, "Key cannot be empty"),
                testCase(null, TrackingHelper::trackingServerTestUrl, "Key cannot be null"),
                testCase("", TrackingHelper::trackingServerUrl, "Key cannot be empty"),
                testCase(null, TrackingHelper::trackingServerUrl, "Key cannot be null"),
                testCase("", TrackingHelper::trackingScriptTestUrl, "Key cannot be empty"),
                testCase(null, TrackingHelper::trackingScriptTestUrl, "Key cannot be null"));
    }

    private static Object[] testCase(String key, BiFunction<TrackingHelper, String, URI> uriGenerator, String expectedMessage) {
        return new Object[]{key, uriGenerator, expectedMessage};
    }

    @Test
    public void ThenAnArgumentExceptionIsThrown() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(this.expectedMessage);

        TrackingHelper helper =
                new TrackingHelper(new DirectTrackingDetailsBuilder("a partner id", "GBP", "transactionId").build());

        uriGenerator.apply(helper, key);
    }
}
