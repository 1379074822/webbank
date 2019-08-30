package cn.edu.buct.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Trans {
    private String amount;
    private String attachInfo;
    private String channel;
    private String currency;
    private String payeeAccountName;
    private String payeeAccountNo;
    private String payerAccountNo;
    private String status;
    private String transName;
    private Timestamp transTime;
    private String usage;

    @Override
    public String toString() {
        return "Trans{" +
                "amount='" + amount + '\'' +
                ", attachInfo='" + attachInfo + '\'' +
                ", channel='" + channel + '\'' +
                ", currency='" + currency + '\'' +
                ", payeeAccountName='" + payeeAccountName + '\'' +
                ", payeeAccountNo='" + payeeAccountNo + '\'' +
                ", payerAccountNo='" + payerAccountNo + '\'' +
                ", status='" + status + '\'' +
                ", transName='" + transName + '\'' +
                ", transTime='" + transTime + '\'' +
                ", usage='" + usage + '\'' +
                '}';
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(String attachInfo) {
        this.attachInfo = attachInfo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
    }

    public String getPayeeAccountNo() {
        return payeeAccountNo;
    }

    public void setPayeeAccountNo(String payeeAccountNo) {
        this.payeeAccountNo = payeeAccountNo;
    }

    public String getPayerAccountNo() {
        return payerAccountNo;
    }

    public void setPayerAccountNo(String payerAccountNo) {
        this.payerAccountNo = payerAccountNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getTransTime() {
        return transTime;
    }

    public void setTransTime(Timestamp transTime) {
        this.transTime = transTime;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
