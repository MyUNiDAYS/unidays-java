package com.myunidays;

import java.math.BigDecimal;

public final class DirectTrackingDetails {
    private String partnerId;
    private String transactionId;
    private String currency;
    private String memberId;
    private String code;
    private BigDecimal orderTotal;
    private BigDecimal itemsUnidaysDiscount;
    private BigDecimal itemsTax;
    private BigDecimal shippingGross;
    private BigDecimal shippingDiscount;
    private BigDecimal itemsGross;
    private BigDecimal itemsOtherDiscount;
    private BigDecimal unidaysDiscountPercentage;
    private Boolean newCustomer;

    public String getPartnerId() {
        return partnerId;
    }

    void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCurrency() {
        return currency;
    }

    void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMemberId() {
        return memberId;
    }

    void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public BigDecimal getItemsUnidaysDiscount() {
        return itemsUnidaysDiscount;
    }

    void setItemsUnidaysDiscount(BigDecimal itemsUnidaysDiscount) {
        this.itemsUnidaysDiscount = itemsUnidaysDiscount;
    }

    public BigDecimal getItemsTax() {
        return itemsTax;
    }

    void setItemsTax(BigDecimal itemsTax) {
        this.itemsTax = itemsTax;
    }

    public BigDecimal getShippingGross() {
        return shippingGross;
    }

    void setShippingGross(BigDecimal shippingGross) {
        this.shippingGross = shippingGross;
    }

    public BigDecimal getShippingDiscount() {
        return shippingDiscount;
    }

    void setShippingDiscount(BigDecimal shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
    }

    public BigDecimal getItemsGross() {
        return itemsGross;
    }

    void setItemsGross(BigDecimal itemsGross) {
        this.itemsGross = itemsGross;
    }

    public BigDecimal getItemsOtherDiscount() {
        return itemsOtherDiscount;
    }

    void setItemsOtherDiscount(BigDecimal itemsOtherDiscount) {
        this.itemsOtherDiscount = itemsOtherDiscount;
    }

    public BigDecimal getUnidaysDiscountPercentage() {
        return unidaysDiscountPercentage;
    }

    void setUnidaysDiscountPercentage(BigDecimal unidaysDiscountPercentage) {
        this.unidaysDiscountPercentage = unidaysDiscountPercentage;
    }

    public Boolean getNewCustomer() {
        return newCustomer;
    }

    void setNewCustomer(Boolean newCustomer) {
        this.newCustomer = newCustomer;
    }
}
