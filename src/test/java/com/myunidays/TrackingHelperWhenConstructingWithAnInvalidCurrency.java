package com.myunidays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TrackingHelperWhenConstructingWithAnInvalidCurrency {
    private final String currency;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public TrackingHelperWhenConstructingWithAnInvalidCurrency(String currency) {
        this.currency = currency;
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
        thrown.expectMessage("Currency is required");

        new TrackingHelper(new DirectTrackingDetailsBuilder("partnerid", currency, "transactionid").build());
    }
}
