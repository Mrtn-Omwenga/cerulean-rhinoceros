package org.zew.donations.dto;

import java.math.BigDecimal;

public class SupportedMissionResponse {
    private String missionId;
    private BigDecimal myDonationAmount;
    private String myDonationCurrency;

    public SupportedMissionResponse(String missionId, BigDecimal myDonationAmount, String myDonationCurrency) {
        this.missionId = missionId;
        this.myDonationAmount = myDonationAmount;
        this.myDonationCurrency = myDonationCurrency;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public BigDecimal getMyDonationAmount() {
        return myDonationAmount;
    }

    public void setMyDonationAmount(BigDecimal myDonationAmount) {
        this.myDonationAmount = myDonationAmount;
    }

    public String getMyDonationCurrency() {
        return myDonationCurrency;
    }

    public void setMyDonationCurrency(String myDonationCurrency) {
        this.myDonationCurrency = myDonationCurrency;
    }
}
