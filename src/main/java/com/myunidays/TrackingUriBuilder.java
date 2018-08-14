package com.myunidays;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class TrackingUriBuilder {
    private static final Pattern percentEncodingPattern = Pattern.compile("(%[A-Z0-9]{2})");
    private static final DecimalFormat moneyFormatter = new DecimalFormat("#0.00");

    private StringBuilder builder;

    TrackingUriBuilder() {
        builder = new StringBuilder();
    }

    private static String encode(String s) {
        String encoded;
        try {
            encoded = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Matcher matcher = percentEncodingPattern.matcher(encoded);
        StringBuilder sb = new StringBuilder();
        int last = 0;

        while (matcher.find()) {
            sb.append(encoded, last, matcher.start());
            sb.append(matcher.group(0).toLowerCase());
            last = matcher.end();
        }
        sb.append(encoded.substring(last));

        return sb.toString();
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    TrackingUriBuilder prepend(String string) {
        builder.insert(0, string);
        return this;
    }

    TrackingUriBuilder appendTrackingParameters(DirectTrackingDetails directTrackingDetails) {
        builder.append("?CustomerId=")
                .append(encode(directTrackingDetails.getPartnerId()))
                .append("&TransactionId=")
                .append(encode(directTrackingDetails.getTransactionId()))
                .append("&Currency=")
                .append(encode(directTrackingDetails.getCurrency()));

        if (directTrackingDetails.getMemberId() != null && !directTrackingDetails.getMemberId().isEmpty())
            builder.append("&MemberId=").append(encode(directTrackingDetails.getMemberId()));

        if (directTrackingDetails.getCode() != null && !directTrackingDetails.getCode().isEmpty())
            builder.append("&Code=").append(encode(directTrackingDetails.getCode()));

        appendDecimalParameter("OrderTotal", directTrackingDetails.getOrderTotal());
        appendDecimalParameter("ItemsUNiDAYSDiscount", directTrackingDetails.getItemsUnidaysDiscount());
        appendDecimalParameter("ItemsTax", directTrackingDetails.getItemsTax());
        appendDecimalParameter("ShippingGross", directTrackingDetails.getShippingGross());
        appendDecimalParameter("ShippingDiscount", directTrackingDetails.getShippingDiscount());
        appendDecimalParameter("ItemsGross", directTrackingDetails.getItemsGross());
        appendDecimalParameter("ItemsOtherDiscount", directTrackingDetails.getItemsOtherDiscount());
        appendDecimalParameter("UNiDAYSDiscountPercentage", directTrackingDetails.getUnidaysDiscountPercentage());

        if (directTrackingDetails.getNewCustomer() != null)
            builder.append("&NewCustomer=").append(directTrackingDetails.getNewCustomer());

        return this;
    }

    TrackingUriBuilder appendSignature(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (key.isEmpty()) throw new IllegalArgumentException("Key cannot be empty");

        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(Base64.decodeBase64(key), "HmacSHA512");
            mac.init(secret_key);

            byte[] buffer = builder.toString().getBytes(Charset.forName("ascii"));
            byte[] signatureBytes = mac.doFinal(buffer);

            builder.append("&Signature=");
            builder.append(encode(Base64.encodeBase64String(signatureBytes)));
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    TrackingUriBuilder appendTestParameter(boolean isTestUri) {
        if (isTestUri)
            builder.append("&Test=true");
        return this;
    }

    private void appendDecimalParameter(String parameterName, BigDecimal decimal) {
        if (decimal == null) return;

        builder.append(String.format("&%s=", parameterName));
        builder.append(encode(moneyFormatter.format(decimal)));
    }
}