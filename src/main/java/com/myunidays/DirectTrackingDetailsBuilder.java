package com.myunidays;

import java.math.BigDecimal;

public final class DirectTrackingDetailsBuilder {
    private final DirectTrackingDetails directTrackingDetails;

    public DirectTrackingDetailsBuilder(String partnerId, String currency, String transactionId) {
        directTrackingDetails = new DirectTrackingDetails();
        directTrackingDetails.setPartnerId(partnerId);
        directTrackingDetails.setCurrency(currency);
        directTrackingDetails.setTransactionId(transactionId);
    }

    public DirectTrackingDetails build() {
        return directTrackingDetails;
    }

    public DirectTrackingDetailsBuilder withMemberId(String memberId) {
        directTrackingDetails.setMemberId(memberId);
        return this;
    }

    public DirectTrackingDetailsBuilder withCode(String code) {
        directTrackingDetails.setCode(code);
        return this;
    }

    public DirectTrackingDetailsBuilder withOrderTotal(BigDecimal orderTotal) {
        directTrackingDetails.setOrderTotal(orderTotal);
        return this;
    }

    public DirectTrackingDetailsBuilder withItemsUnidaysDiscount(BigDecimal itemsUnidaysDiscount) {
        directTrackingDetails.setItemsUnidaysDiscount(itemsUnidaysDiscount);
        return this;
    }

    public DirectTrackingDetailsBuilder withItemsTax(BigDecimal itemsTax) {
        directTrackingDetails.setItemsTax(itemsTax);
        return this;
    }

    public DirectTrackingDetailsBuilder withShippingGross(BigDecimal shippingGross) {
        directTrackingDetails.setShippingGross(shippingGross);
        return this;
    }

    public DirectTrackingDetailsBuilder withShippingDiscount(BigDecimal shippingDiscount) {
        directTrackingDetails.setShippingDiscount(shippingDiscount);
        return this;
    }

    public DirectTrackingDetailsBuilder withItemsGross(BigDecimal itemsGross) {
        directTrackingDetails.setItemsGross(itemsGross);
        return this;
    }

    public DirectTrackingDetailsBuilder withItemsOtherDiscount(BigDecimal itemsOtherDiscount) {
        directTrackingDetails.setItemsOtherDiscount(itemsOtherDiscount);
        return this;
    }

    public DirectTrackingDetailsBuilder withUnidaysDiscountPercentage(BigDecimal discountPercentage) {
        directTrackingDetails.setUnidaysDiscountPercentage(discountPercentage);
        return this;
    }

    public DirectTrackingDetailsBuilder withNewCustomer(boolean newCustomer) {
        directTrackingDetails.setNewCustomer(newCustomer);
        return this;
    }
}
