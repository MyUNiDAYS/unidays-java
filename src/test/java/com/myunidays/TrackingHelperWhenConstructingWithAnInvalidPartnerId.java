package com.myunidays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TrackingHelperWhenConstructingWithAnInvalidPartnerId {

    private final String partnerId;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public TrackingHelperWhenConstructingWithAnInvalidPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {""}, {null}
        });
    }

    @Test
    public void ThenAnArgumentExceptionIsThrown() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("PartnerId is required");

        new TrackingHelper(new DirectTrackingDetailsBuilder(partnerId, "GBP", "transactionid").build());
    }

}
