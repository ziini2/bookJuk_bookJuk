package com.itwillbs.bookjuk.dto;

import java.util.Objects;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;

public class PaymentDTO {
	
	private String merchantMid;
    private String paymentId;
    private int paidAmount;
    private PaymentStatus status;
    
 // Constructor
    public PaymentDTO(String merchantMid, String paymentId, int paidAmount, PaymentStatus status) {
        this.merchantMid = merchantMid;
        this.paymentId = paymentId;
        this.paidAmount = paidAmount;
        this.status = status;
    }

	public String getMerchantMid() {
		return merchantMid;
	}

	public void setMerchantMid(String merchantMid) {
		this.merchantMid = merchantMid;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public int getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(int paidAmount) {
		this.paidAmount = paidAmount;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}
    
	  // toString method
    @Override
    public String toString() {
        return "PaymentDTO{" +
                "merchantMid='" + merchantMid + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", paidAmount=" + paidAmount +
                ", status=" + status +
                '}';
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDTO that = (PaymentDTO) o;
        return paidAmount == that.paidAmount &&
                Objects.equals(merchantMid, that.merchantMid) &&
                Objects.equals(paymentId, that.paymentId) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(merchantMid, paymentId, paidAmount, status);
    }

}
