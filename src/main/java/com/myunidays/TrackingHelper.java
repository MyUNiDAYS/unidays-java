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

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class TrackingHelper {
    private final String trackingUrl;
    private final String customerId;
    private final byte[] key;
    private final DecimalFormat moneyFormatter = new DecimalFormat("#0.00");



    public TrackingHelper(String customerId, byte[] key) {
        if (customerId == null || "".equals(customerId))
            throw new IllegalArgumentException("CustomerId is required");

        if (key == null)
            throw new IllegalArgumentException("Key cannot be null");

        if (key.length == 0)
            throw new IllegalArgumentException("Key cannot be empty");

        this.customerId = customerId;
        this.key = key;

        this.trackingUrl = "https://tracking.myunidays.com/perks/redemption/v1.1";
    }

    private static void signUrl(StringBuilder builder, String param, byte[] key) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String signature = stringToHmacSHA512(builder.toString(), key);

        builder
                .append('&')
                .append(param)
                .append('=')
                .append(encode(signature));

    }

    private static String stringToHmacSHA512(String s, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA512");
            sha256_HMAC.init(secret_key);

            return Base64.encodeBase64String(sha256_HMAC.doFinal(s.getBytes()));
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw e;
        }
    }

    private static String encode(String s) throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(s, "UTF-8");
        Matcher matcher = Pattern.compile("(%[A-Z0-9]{2})").matcher(encoded);
        StringBuilder sb = new StringBuilder();
        int last = 0;

        while (matcher.find()) {
            sb.append(encoded.substring(last, matcher.start()));
            sb.append(matcher.group(0).toLowerCase());
            last = matcher.end();
        }
        sb.append(encoded.substring(last));

        return sb.toString();
    }

    private static void appendToStringBuilder(StringBuilder builder, String field, Object value) throws UnsupportedEncodingException {
        builder.append(field);
        if (value != null) {
            builder.append(encode(value.toString()));
        }
    }

    private void appendToStringBuilder(StringBuilder builder, String field, BigDecimal value) throws UnsupportedEncodingException {
        builder.append(field);
        if (value != null) {
            builder.append(encode(moneyFormatter.format(value)));
        }
    }

    private void generateQuery(StringBuilder builder, String customerId, String transactionId, String memberId,
                                      String currency, BigDecimal orderTotal, BigDecimal itemsUNiDAYSDiscount, String code,
                                      BigDecimal itemsTax, BigDecimal shippingGross, BigDecimal shippingDiscount, BigDecimal itemsGross,
                                      BigDecimal itemsOtherDiscount, BigDecimal UNiDAYSDiscountPercentage, Integer newCustomer) throws UnsupportedEncodingException {

        appendToStringBuilder(builder, "?CustomerId=", customerId);
        appendToStringBuilder(builder, "&TransactionId=", transactionId);
        appendToStringBuilder(builder, "&MemberId=", memberId);
        appendToStringBuilder(builder, "&Currency=", currency);
        appendToStringBuilder(builder, "&OrderTotal=", orderTotal);
        appendToStringBuilder(builder, "&ItemsUNiDAYSDiscount=", itemsUNiDAYSDiscount);
        appendToStringBuilder(builder, "&Code=", code);
        appendToStringBuilder(builder, "&ItemsTax=", itemsTax);
        appendToStringBuilder(builder, "&ShippingGross=", shippingGross);
        appendToStringBuilder(builder, "&ShippingDiscount=", shippingDiscount);
        appendToStringBuilder(builder, "&ItemsGross=", itemsGross);
        appendToStringBuilder(builder, "&ItemsOtherDiscount=", itemsOtherDiscount);
        appendToStringBuilder(builder, "&UNiDAYSDiscountPercentage=", UNiDAYSDiscountPercentage);
        appendToStringBuilder(builder, "&NewCustomer=", newCustomer);
    }

    /**
     * Generates the Server-to-Server Redemption Tracking URL
     *
     * @param transactionId             Unique ID for the transaction
     * @param memberId
     * @param currency
     * @param orderTotal
     * @param itemsUNiDAYSDiscount
     * @param code
     * @param itemsTax
     * @param shippingGross
     * @param shippingDiscount
     * @param itemsGross
     * @param itemsOtherDiscount
     * @param UNiDAYSDiscountPercentage
     * @param newCustomer
     * @return The URL to make a server-to-server request to.
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public String serverToServerTrackingUrl(String transactionId, String memberId, String currency, BigDecimal orderTotal, BigDecimal itemsUNiDAYSDiscount, String code, BigDecimal itemsTax, BigDecimal shippingGross, BigDecimal shippingDiscount, BigDecimal itemsGross, BigDecimal itemsOtherDiscount, BigDecimal UNiDAYSDiscountPercentage, Integer newCustomer) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder builder = new StringBuilder();

        generateQuery(builder, this.customerId, transactionId, memberId, currency, orderTotal, itemsUNiDAYSDiscount, code, itemsTax,
                shippingGross, shippingDiscount, itemsGross, itemsOtherDiscount, UNiDAYSDiscountPercentage, newCustomer);
        signUrl(builder, "Signature", this.key);

        builder.insert(0, trackingUrl);
        return builder.toString();
    }

    /**
     * Generates the Redemption Tracking URL
     *
     * @param transactionId             Unique ID for the transaction
     * @param memberId
     * @param currency                  ISO 4217 Currency Code
     * @param orderTotal                Order total rounded to 2 d.p.
     * @param itemsUNiDAYSDiscount
     * @param code
     * @param itemsTax
     * @param shippingGross
     * @param shippingDiscount
     * @param itemsGross
     * @param itemsOtherDiscount
     * @param UNiDAYSDiscountPercentage
     * @param newCustomer
     * @return The URL to be placed inside an &lt;img /&gt; element in your receipt page. The image returned is a 1x1px transparent gif..
     */
    public String clientSideTrackingPixelUrl(String transactionId, String memberId, String currency, BigDecimal orderTotal, BigDecimal itemsUNiDAYSDiscount, String code, BigDecimal itemsTax, BigDecimal shippingGross, BigDecimal shippingDiscount, BigDecimal itemsGross, BigDecimal itemsOtherDiscount, BigDecimal UNiDAYSDiscountPercentage, Integer newCustomer) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder builder = new StringBuilder();

        generateQuery(builder, this.customerId, transactionId, memberId, currency, orderTotal, itemsUNiDAYSDiscount, code, itemsTax,
                shippingGross, shippingDiscount, itemsGross, itemsOtherDiscount, UNiDAYSDiscountPercentage, newCustomer);
        signUrl(builder, "Signature", this.key);

        builder.insert(0, trackingUrl);
        builder.insert(trackingUrl.length(), ".gif");

        return builder.toString();
    }
}