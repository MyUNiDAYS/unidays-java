/*
The MIT License (MIT)

Copyright (c) 2018 MyUNiDAYS Ltd.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

*/

package com.myunidays;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TrackingHelperWhenGeneratingATrackingPixelUrlWithAllParamsPresent {

    private final String parameterName;
    private final String expectedParameterValue;
    private URI url;

    public TrackingHelperWhenGeneratingATrackingPixelUrlWithAllParamsPresent(String parameterName, String expectedParameterValue) {

        this.parameterName = parameterName;
        this.expectedParameterValue = expectedParameterValue;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"CustomerId", "a customer"},
                {"TransactionId", "the transaction"},
                {"MemberId", "id of student"},
                {"Currency", "GBP"},
                {"OrderTotal", "209.00"},
                {"ItemsUNiDAYSDiscount", "13.00"},
                {"Code", "a code"},
                {"ItemsTax", "34.50"},
                {"ShippingGross", "5.00"},
                {"ShippingDiscount", "3.00"},
                {"ItemsGross", "230.00"},
                {"ItemsOtherDiscount", "10.00"},
                {"UNiDAYSDiscountPercentage", "10.00"},
                {"NewCustomer", "1"},
                {"Signature", "nB3GKjq9wKf+qbQywULuVEunrGEH2nd+qRKjDoT35nsgy0yDoNvYEPZEdD4VkglhxgB8oMYZEKW9CkMFvgV/+A=="},
        });
    }

    @Before
    public void before() throws Exception {
        byte[] key = new byte[]{(byte) 0xc4, 0x26, (byte) 0xa2, 0x1a, 0x6b, 0x3a, 0x78, 0x47, 0x11, 0x60, (byte) 0xaa, (byte) 0x98, (byte) 0xee, 0x15, (byte) 0xd8, 0x3c, 0x12, (byte) 0xe2, (byte) 0xcd, (byte) 0x9c, 0x18, (byte) 0xf5, (byte) 0x9d, (byte) 0xa0, (byte) 0xfc, (byte) 0xea, (byte) 0xb2, 0x39, 0x76, (byte) 0xb4, (byte) 0xaf, (byte) 0xb9, 0x6a, 0x67, (byte) 0xc3, 0x69, 0x7f, (byte) 0xbf, 0x1e, 0x2b, (byte) 0xea, (byte) 0xdb, (byte) 0x9e, (byte) 0x8a, 0x65, 0x1f, (byte) 0xbc, 0x42, 0x1, (byte) 0xa6, (byte) 0xff, (byte) 0xd3, (byte) 0xe8, 0x75, (byte) 0xcc, (byte) 0xb6, 0x31, 0x7c, (byte) 0x99, (byte) 0xba, (byte) 0xd8, 0x66, (byte) 0xe0, 0x48, 0x77, (byte) 0xe7, (byte) 0xbe, 0x35, 0x6, 0x10, (byte) 0xeb, (byte) 0xa1, (byte) 0xfc, (byte) 0xcb, 0x47, 0x34, 0x2, (byte) 0xc2, (byte) 0xa1, 0x70, (byte) 0xfa, 0x63, 0x76, 0x16, 0x22, (byte) 0xb2, 0x67, (byte) 0x94, 0x47, (byte) 0xf2, (byte) 0x9b, 0x69, 0x26, 0x5a, 0x5e, (byte) 0xd9, 0x42, (byte) 0x81, 0x72, 0x61, (byte) 0xb6, 0x57, 0x5, 0x36, 0x6a, (byte) 0xd1, 0x2c, (byte) 0x82, 0x5c, (byte) 0x90, 0x8, (byte) 0xe7, 0x74, (byte) 0xa1, 0x28, (byte) 0x87, 0x13, 0x3c, 0x30, 0x4c, (byte) 0xde, (byte) 0xc3, 0x4b, (byte) 0x9b, (byte) 0xbd, 0x8, 0x5a, 0x7b};

        TrackingHelper trackingHelper = new TrackingHelper("a customer", key);
        url = new URI(trackingHelper.clientSideTrackingPixelUrl("the transaction", "id of student", "GBP", new BigDecimal(209.00), new BigDecimal(13.00), "a code", new BigDecimal(34.50), new BigDecimal(5.00), new BigDecimal(3.00), new BigDecimal(230.00), new BigDecimal(10.00), new BigDecimal(10.00), 1));
    }

    @Test
    public void thenTheSchemeShouldBeHttps() {
        assertThat(url.getScheme(), is(equalTo("https")));
    }

    @Test
    public void thenTheHostShouldBeCorrect() {
        assertThat(url.getHost(), is(equalTo("tracking.myunidays.com")));
    }

    @Test
    public void thenThePathShouldBeCorrect() {
        assertThat(url.getPath(), is(equalTo("/perks/redemption/v1.1.gif")));
    }

    @Test
    public void thenTheParametersShouldBeCorrect() {
        Map<String, String> params = URLEncodedUtils.parse(url, Charset.forName("UTF-8"))
                .stream()
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

        assertThat(params.get(parameterName), is(equalTo(expectedParameterValue)));
    }
}
