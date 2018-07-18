package com.professor.traficinspiration.model.messages;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteOrderRequestMessage extends RequestMessage{

    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("review_MAC")
    @Expose
    private String reviewMAC;

    @SerializedName("idOrder")
    @Expose
    private String idOrder;
    @SerializedName("idOrder_MAC")
    @Expose
    private String idOrderMAC;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewMAC() {
        return reviewMAC;
    }

    public void setReviewMAC(String reviewMAC) {
        this.reviewMAC = reviewMAC;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdOrderMAC() {
        return idOrderMAC;
    }

    public void setIdOrderMAC(String idOrderMAC) {
        this.idOrderMAC = idOrderMAC;
    }
}
