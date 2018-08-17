package com.myunidays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TrackingHelperWhenGeneratingAUrlWithMinimumParamsPresent {

    private final static String key = "xCaiGms6eEcRYKqY7hXYPBLizZwY9Z2g/OqyOXa0r7lqZ8Npf78eK+rbnoplH7xCAab/0+h1zLYxfJm62GbgSHfnvjUGEOuh/MtHNALCoXD6Y3YWIrJnlEfym2kmWl7ZQoFyYbZXBTZq0SyCXJAI53ShKIcTPDBM3sNLm70IWns=";
    private final Function<TrackingHelper, URI> generateUri;
    private final String expectedUri;
    private URI uri;

    public TrackingHelperWhenGeneratingAUrlWithMinimumParamsPresent(Function<TrackingHelper, URI> generateUri, String expectedUri) {
        this.generateUri = generateUri;
        this.expectedUri = expectedUri;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                testCase(h -> h.trackingScriptUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption/js?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=&ItemsUNiDAYSDiscount=&Code=&ItemsTax=&ShippingGross=&ShippingDiscount=&ItemsGross=&ItemsOtherDiscount=&UNiDAYSDiscountPercentage=&NewCustomer=&Signature=yvBrLWFbtzmtswjTrPev4pA68tvWlANFHBBVN1g4Ei1rZnZnOooI3%2FyFABpN%2FcsgOTAYvzyRDylgC1X4ciIZmg%3D%3D"),
                testCase(h -> h.trackingScriptTestUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption/js?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=&ItemsUNiDAYSDiscount=&Code=&ItemsTax=&ShippingGross=&ShippingDiscount=&ItemsGross=&ItemsOtherDiscount=&UNiDAYSDiscountPercentage=&NewCustomer=&Signature=yvBrLWFbtzmtswjTrPev4pA68tvWlANFHBBVN1g4Ei1rZnZnOooI3%2FyFABpN%2FcsgOTAYvzyRDylgC1X4ciIZmg%3D%3D&Test=True"),
                testCase(h -> h.trackingServerUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=&ItemsUNiDAYSDiscount=&Code=&ItemsTax=&ShippingGross=&ShippingDiscount=&ItemsGross=&ItemsOtherDiscount=&UNiDAYSDiscountPercentage=&NewCustomer=&Signature=yvBrLWFbtzmtswjTrPev4pA68tvWlANFHBBVN1g4Ei1rZnZnOooI3%2FyFABpN%2FcsgOTAYvzyRDylgC1X4ciIZmg%3D%3D"),
                testCase(h -> h.trackingServerTestUrl(key), "https://api.myunidays.com/tracking/v1.2/redemption?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=&ItemsUNiDAYSDiscount=&Code=&ItemsTax=&ShippingGross=&ShippingDiscount=&ItemsGross=&ItemsOtherDiscount=&UNiDAYSDiscountPercentage=&NewCustomer=&Signature=yvBrLWFbtzmtswjTrPev4pA68tvWlANFHBBVN1g4Ei1rZnZnOooI3%2FyFABpN%2FcsgOTAYvzyRDylgC1X4ciIZmg%3D%3D&Test=True"),
                testCase(TrackingHelper::trackingScriptUrl, "https://api.myunidays.com/tracking/v1.2/redemption/js?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=&ItemsUNiDAYSDiscount=&Code=&ItemsTax=&ShippingGross=&ShippingDiscount=&ItemsGross=&ItemsOtherDiscount=&UNiDAYSDiscountPercentage=&NewCustomer="),
                testCase(TrackingHelper::trackingScriptTestUrl, "https://api.myunidays.com/tracking/v1.2/redemption/js?PartnerId=a+partner+Id&TransactionId=the+transaction+id&MemberId=&Currency=GBP&OrderTotal=&ItemsUNiDAYSDiscount=&Code=&ItemsTax=&ShippingGross=&ShippingDiscount=&ItemsGross=&ItemsOtherDiscount=&UNiDAYSDiscountPercentage=&NewCustomer=&Test=True")
        );
    }

    private static Object[] testCase(Function<TrackingHelper, URI> generateURI, String expectedURI) {
        return new Object[]{generateURI, expectedURI};
    }

    @Before
    public void before() {
        DirectTrackingDetails directTrackingDetails =
                new DirectTrackingDetailsBuilder("a partner Id", "GBP", "the transaction id").build();

        TrackingHelper trackingHelper = new TrackingHelper(directTrackingDetails);

        uri = generateUri.apply(trackingHelper);
    }

    @Test
    public void thenTheUrlShouldBeCorrect() {
        assertThat(uri.toString(), is(equalTo(expectedUri)));
    }
}
