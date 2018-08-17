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
                testCase(h -> h.trackingScriptUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption/js?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&Code=a+code&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=True&Signature=VsP%2B%2BN2PQ7Jy%2FhH6wjkVcGRLRkqpyBFyZPCLW7u0UYuXiYvBlggi4SgCQ1GPs5mg3JswBYms8qTwRehFpWhhAg%3D%3D"),
                testCase(h -> h.trackingScriptTestUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption/js?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&Code=a+code&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=True&Signature=VsP%2B%2BN2PQ7Jy%2FhH6wjkVcGRLRkqpyBFyZPCLW7u0UYuXiYvBlggi4SgCQ1GPs5mg3JswBYms8qTwRehFpWhhAg%3D%3D&Test=True"),
                testCase(h -> h.trackingServerUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&Code=a+code&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=True&Signature=VsP%2B%2BN2PQ7Jy%2FhH6wjkVcGRLRkqpyBFyZPCLW7u0UYuXiYvBlggi4SgCQ1GPs5mg3JswBYms8qTwRehFpWhhAg%3D%3D"),
                testCase(h -> h.trackingServerTestUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&Code=a+code&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=True&Signature=VsP%2B%2BN2PQ7Jy%2FhH6wjkVcGRLRkqpyBFyZPCLW7u0UYuXiYvBlggi4SgCQ1GPs5mg3JswBYms8qTwRehFpWhhAg%3D%3D&Test=True"),
                testCase(TrackingHelper::trackingScriptUrl, "https://api.myunidays.com/tracking/v1.2/redemption/js?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&Code=a+code&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=True"),
                testCase(TrackingHelper::trackingScriptTestUrl, "https://api.myunidays.com/tracking/v1.2/redemption/js?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=209.00&ItemsUNiDAYSDiscount=13.00&Code=a+code&ItemsTax=34.50&ShippingGross=5.00&ShippingDiscount=3.00&ItemsGross=230.00&ItemsOtherDiscount=10.00&UNiDAYSDiscountPercentage=10.00&NewCustomer=True&Test=True")
        );
    }

    private static Object[] testCase(Function<TrackingHelper, URI> generateURI, String expectedURI) {
        return new Object[]{generateURI, expectedURI};
    }

    @Before
    public void before() {
        DirectTrackingDetails directTrackingDetails = new DirectTrackingDetailsBuilder("a partner Id", "GBP", "the transaction id")
                .withOrderTotal(new BigDecimal("209.00"))
                .withItemsUnidaysDiscount(new BigDecimal("13.00"))
                .withCode("a code")
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
