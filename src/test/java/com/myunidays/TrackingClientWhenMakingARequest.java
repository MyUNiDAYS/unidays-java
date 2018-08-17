package com.myunidays;

import org.apache.http.nio.client.HttpAsyncClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.net.URI;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TrackingClientWhenMakingARequest {

    private HttpAsyncClient httpClient;
    private URI expectedUri;

    @Before
    public void before() throws Exception {
        String key = "xCaiGms6eEcRYKqY7hXYPBLizZwY9Z2g/OqyOXa0r7lqZ8Npf78eK+rbnoplH7xCAab/0+h1zLYxfJm62GbgSHfnvjUGEOuh/MtHNALCoXD6Y3YWIrJnlEfym2kmWl7ZQoFyYbZXBTZq0SyCXJAI53ShKIcTPDBM3sNLm70IWns=";
        DirectTrackingDetails directTrackingDetails =
                new DirectTrackingDetailsBuilder("a partner Id", "GBP", "the transaction id")
                        .withOrderTotal(new BigDecimal("209.00"))
                        .withItemsUnidaysDiscount(new BigDecimal("13.00"))
                        .withCode("a code")
                        .withMemberId("a member id")
                        .withItemsTax(new BigDecimal("34.50"))
                        .withShippingGross(new BigDecimal("5.00"))
                        .withShippingDiscount(new BigDecimal("3.00"))
                        .withItemsGross(new BigDecimal("230.00"))
                        .withItemsOtherDiscount(new BigDecimal("10.00"))
                        .withUnidaysDiscountPercentage(new BigDecimal("10.00"))
                        .withNewCustomer(true)
                        .build();

        expectedUri = new URI("https://api.myunidays.com/tracking/v1.2/redemption?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=a+member+id&Currency=GBP&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&Code=a+code&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=True&Signature=OWgjrpnHU3I6jSjYb7XiuPBq9rhrMc31GUqtKRvLIA6%2FKv1mslm54Xca%2BtXNE3uhaudnOuFm7V6J6704nJQDUA%3D%3D");

        httpClient = mock(HttpAsyncClient.class, Mockito.RETURNS_MOCKS);
        TrackingClient client = new TrackingClient(directTrackingDetails, key, httpClient);

        client.send().get();
    }

    @Test
    public void thenTheRequestShouldBeMade() {
        verify(httpClient).execute(argThat(request -> request.getURI().equals(expectedUri) && request.getMethod().equals("GET")), isNull());
    }
}
