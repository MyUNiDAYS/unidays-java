package com.myunidays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TrackingHelperWhenGeneratingAUrlWithAllParamsPresent {
    private final static String key = "xCaiGms6eEcRYKqY7hXYPBLizZwY9Z2g/OqyOXa0r7lqZ8Npf78eK+rbnoplH7xCAab/0+h1zLYxfJm62GbgSHfnvjUGEOuh/MtHNALCoXD6Y3YWIrJnlEfym2kmWl7ZQoFyYbZXBTZq0SyCXJAI53ShKIcTPDBM3sNLm70IWns=";
    private final Function<TrackingHelper, URI> generateUri;
    private final String expectedUri;
    private URI uri;

    public TrackingHelperWhenGeneratingAUrlWithAllParamsPresent(Function<TrackingHelper, URI> generateUri, String expectedUri) {
        this.generateUri = generateUri;
        this.expectedUri = expectedUri;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                testCase(h -> h.trackingScriptUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption/gif?CustomerId=a+customer+Id&TransactionId=the+transaction&Currency=GBP&MemberId=a+member+id&Code=a+code&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=true&Signature=K0kambXKdZpXa99ER%2fH%2f3qhPjWw6E1ax7PDcmpBUbhb2DtxXulMgW0tDE3ZRiEg%2fWVnsOlXsHipwNkBaz3IpTA%3d%3d"),
                testCase(h -> h.trackingScriptTestUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption/gif?CustomerId=a+customer+Id&TransactionId=the+transaction&Currency=GBP&MemberId=a+member+id&Code=a+code&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=true&Signature=K0kambXKdZpXa99ER%2fH%2f3qhPjWw6E1ax7PDcmpBUbhb2DtxXulMgW0tDE3ZRiEg%2fWVnsOlXsHipwNkBaz3IpTA%3d%3d&Test=true"),
                testCase(h -> h.trackingServerUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption?CustomerId=a+customer+Id&TransactionId=the+transaction&Currency=GBP&MemberId=a+member+id&Code=a+code&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=true&Signature=K0kambXKdZpXa99ER%2fH%2f3qhPjWw6E1ax7PDcmpBUbhb2DtxXulMgW0tDE3ZRiEg%2fWVnsOlXsHipwNkBaz3IpTA%3d%3d"),
                testCase(h -> h.trackingServerTestUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption?CustomerId=a+customer+Id&TransactionId=the+transaction&Currency=GBP&MemberId=a+member+id&Code=a+code&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=true&Signature=K0kambXKdZpXa99ER%2fH%2f3qhPjWw6E1ax7PDcmpBUbhb2DtxXulMgW0tDE3ZRiEg%2fWVnsOlXsHipwNkBaz3IpTA%3d%3d&Test=true"),
                testCase(TrackingHelper::trackingScriptUrl, "https://api.myunidays.com/tracking/v1.2/redemption/gif?CustomerId=a+customer+Id&TransactionId=the+transaction&Currency=GBP&MemberId=a+member+id&Code=a+code&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=true"),
                testCase(TrackingHelper::trackingScriptTestUrl, "https://api.myunidays.com/tracking/v1.2/redemption/gif?CustomerId=a+customer+Id&TransactionId=the+transaction&Currency=GBP&MemberId=a+member+id&Code=a+code&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=true&Test=true")
        );
    }

    private static Object[] testCase(Function<TrackingHelper, URI> generateURI, String expectedURI) {
        return new Object[]{generateURI, expectedURI};
    }

    @Before
    public void before() {
        DirectTrackingDetails directTrackingDetails = new DirectTrackingDetailsBuilder("a customer Id", "GBP", "the transaction")
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

        TrackingHelper trackingHelper = new TrackingHelper(directTrackingDetails);

        uri = generateUri.apply(trackingHelper);
    }

    @Test
    public void thenTheUrlShouldBeCorrect() {
        assertThat(uri.toString(), is(equalTo(expectedUri)));
    }
}
