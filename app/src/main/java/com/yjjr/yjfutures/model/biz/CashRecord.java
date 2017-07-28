package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/28.
 */

public class CashRecord {

    /**
     * money : 500
     * statusCn : 已提交,待审核
     * id : 1
     * account : lj
     * cashTime : 1499925285000
     * status : 0
     */

    private double money;
    private String statusCn;
    private int id;
    private String account;
    private long cashTime;
    /**
     * -1:已驳回 1:已提交,待审核  2:已审核,待转账 3:已提现
     */
    private int status;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getStatusCn() {
        return statusCn;
    }

    public void setStatusCn(String statusCn) {
        this.statusCn = statusCn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getCashTime() {
        return cashTime;
    }

    public void setCashTime(long cashTime) {
        this.cashTime = cashTime;
    }


    /**
     * -1:已驳回 1:已提交,待审核  2:已审核,待转账 3:已提现
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CashRecord{" +
                "money=" + money +
                ", statusCn='" + statusCn + '\'' +
                ", id=" + id +
                ", account='" + account + '\'' +
                ", cashTime=" + cashTime +
                ", status='" + status + '\'' +
                '}';
    }
}
