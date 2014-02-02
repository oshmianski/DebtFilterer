package by.oshmianski.objects;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:30 AM
 */
public class DataMainItem {
    private String unid;
    private String accounts;
    private String fio;
    private BigDecimal sumDebt;
    private BigDecimal sumSalary;
    private BigDecimal sumMove;
    private BigDecimal feeRate;
    private boolean draft;
    private boolean exclude;

    public DataMainItem(String unid, String accounts, String fio, BigDecimal sumDebt, BigDecimal sumSalary, boolean draft, boolean exclude) {
        this.unid = unid;
        this.accounts = accounts;
        this.fio = fio;
        this.sumDebt = sumDebt;
        this.sumSalary = sumSalary;
        this.draft = draft;
        this.exclude = exclude;
    }

    public String getUnid() {
        return unid;
    }

    public void setUnid(String unid) {
        this.unid = unid;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public BigDecimal getSumDebt() {
        return sumDebt;
    }

    public void setSumDebt(BigDecimal sumDebt) {
        this.sumDebt = sumDebt;
    }

    public BigDecimal getSumSalary() {
        return sumSalary;
    }

    public void setSumSalary(BigDecimal sumSalary) {
        this.sumSalary = sumSalary;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public BigDecimal getSumMove() {
        return sumMove;
    }

    public void setSumMove(BigDecimal sumMove) {
        this.sumMove = sumMove;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }
}
