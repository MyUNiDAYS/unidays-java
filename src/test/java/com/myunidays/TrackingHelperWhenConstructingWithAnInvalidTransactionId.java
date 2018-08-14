package com.myunidays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TrackingHelperWhenConstructingWithAnInvalidTransactionId {
    private final String transactionId;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public TrackingHelperWhenConstructingWithAnInvalidTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
        thrown.expectMessage("TransactionId is required");

        new TrackingHelper(new DirectTrackingDetailsBuilder("partnerid", "GBP", transactionId).build());
    }
}
