package com.myunidays;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

final class TrackingUriBuilder {
    private static final DecimalFormat moneyFormatter = new DecimalFormat("#0.00");

    private StringBuilder builder;

    TrackingUriBuilder() {
        builder = new StringBuilder();
    }

    private static String encode(String s) {
        try {
           return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
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
        builder.append("?PartnerId=")
                .append(encode(directTrackingDetails.getPartnerId()))
                .append("&TransactionId=")
                .append(encode(directTrackingDetails.getTransactionId()));

        builder.append("&MemberId=");
        if (directTrackingDetails.getMemberId() != null && !directTrackingDetails.getMemberId().isEmpty())
            builder.append(encode(directTrackingDetails.getMemberId()));

        builder.append("&Currency=");
        if(directTrackingDetails.getCurrency()!= null && !directTrackingDetails.getCurrency().isEmpty())
            builder.append(encode(directTrackingDetails.getCurrency()));

        builder.append("&OrderTotal=");
        if (directTrackingDetails.getOrderTotal() != null)
            appendDecimal(directTrackingDetails.getOrderTotal());

        builder.append("&ItemsUNiDAYSDiscount=");
        if (directTrackingDetails.getItemsUnidaysDiscount() != null)
            appendDecimal(directTrackingDetails.getItemsUnidaysDiscount());

        builder.append("&Code=");
        if (directTrackingDetails.getCode() != null)
            builder.append(encode(directTrackingDetails.getCode()));

        builder.append("&ItemsTax=");
        if (directTrackingDetails.getItemsTax() != null)
            appendDecimal(directTrackingDetails.getItemsTax());

        builder.append("&ShippingGross=");
        if (directTrackingDetails.getShippingGross() != null)
            appendDecimal(directTrackingDetails.getShippingGross());

        builder.append("&ShippingDiscount=");
        if (directTrackingDetails.getShippingDiscount() != null)
            appendDecimal(directTrackingDetails.getShippingDiscount());

        builder.append("&ItemsGross=");
        if (directTrackingDetails.getItemsGross() != null)
            appendDecimal(directTrackingDetails.getItemsGross());

        builder.append("&ItemsOtherDiscount=");
        if (directTrackingDetails.getItemsOtherDiscount() != null)
            appendDecimal(directTrackingDetails.getItemsOtherDiscount());

        builder.append("&UNiDAYSDiscountPercentage=");
        if (directTrackingDetails.getUnidaysDiscountPercentage() != null)
            appendDecimal(directTrackingDetails.getUnidaysDiscountPercentage());

        builder.append("&NewCustomer=");
        if (directTrackingDetails.getNewCustomer() != null && directTrackingDetails.getNewCustomer())
           builder.append("True");

        return this;
    }

    TrackingUriBuilder appendSignature(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (key.isEmpty()) throw new IllegalArgumentException("Key cannot be empty");

        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(Base64.decodeBase64(key), "HmacSHA512");
            mac.init(secret_key);

            byte[] buffer = builder.toString().getBytes(StandardCharsets.US_ASCII);
            byte[] signatureBytes = mac.doFinal(buffer);

            builder.append("&Signature=");
            builder.append(encode(Base64.encodeBase64String(signatureBytes)));
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    private void appendDecimal(BigDecimal decimal) {
        if (decimal == null) return;
        builder.append(encode(moneyFormatter.format(decimal)));
    }

    TrackingUriBuilder appendTestParameter(boolean isTestUri) {
        if (isTestUri)
            builder.append("&Test=True");
        return this;
    }
}
