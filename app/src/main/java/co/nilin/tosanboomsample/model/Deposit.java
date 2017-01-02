package co.nilin.tosanboomsample.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by data on 1/2/2017.
 */

public class Deposit {
    private final String number;
    private final String status;
    private final String inaugurationDate;
    private final String expireDate;
    private final long balance;
    private final long blockedAmount;
    private final String currency;
    private final String branchCode;
    private final String withdrawalOption;
    private final long availableBalance;
    private final String iban;

    public Deposit(String number, String status, String inaugurationDate, String expireDate, long balance, long blockedAmount, String currency, String branchCode, String withdrawalOption, long availableBalance, String iban) {
        this.number = number;
        this.status = status;
        this.inaugurationDate = inaugurationDate;
        this.expireDate = expireDate;
        this.balance = balance;
        this.blockedAmount = blockedAmount;
        this.currency = currency;
        this.branchCode = branchCode;
        this.withdrawalOption = withdrawalOption;
        this.availableBalance = availableBalance;
        this.iban = iban;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public String getInaugurationDate() {
        return inaugurationDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public long getBalance() {
        return balance;
    }

    public long getBlockedAmount() {
        return blockedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getWithdrawalOption() {
        return withdrawalOption;
    }

    public long getAvailableBalance() {
        return availableBalance;
    }

    public String getIBAN() {
        return iban;
    }

    public static Deposit toDeposit(JsonObject json) {
        JsonElement depositNumber = json.get("deposit_number");
        JsonElement depositStatus = json.get("deposit_status");
        JsonElement inaugurationDate = json.get("inauguration_date");
        JsonElement expireDate = json.get("expire_date");
        JsonElement balance = json.get("balance");
        JsonElement blockedAmount = json.get("blocked_amount");
        JsonElement currency = json.get("currency");
        JsonElement branchCode = json.get("branch_code");
        JsonElement withdrawalOption = json.get("withdrawal_option");
        JsonElement availableBalance = json.get("available_balance");
        JsonElement iban = json.get("iban");

        return new Deposit(depositNumber == null || depositNumber.isJsonNull() ? "-" : depositNumber.getAsString(),
                depositStatus == null || depositStatus.isJsonNull() ? "" : depositStatus.getAsString(),
                inaugurationDate == null || inaugurationDate.isJsonNull() ? "" : inaugurationDate.getAsString(),
                expireDate == null || expireDate.isJsonNull() ? "" : expireDate.getAsString(),
                balance == null || balance.isJsonNull() ? 0 : balance.getAsLong(),
                blockedAmount == null || blockedAmount.isJsonNull() ? 0 : blockedAmount.getAsLong(),
                currency == null || currency.isJsonNull() ? "" : currency.getAsString(),
                branchCode == null || branchCode.isJsonNull() ? "" : branchCode.getAsString(),
                withdrawalOption == null || withdrawalOption.isJsonNull() ? "" : withdrawalOption.getAsString(),
                availableBalance == null || availableBalance.isJsonNull() ? 0 : availableBalance.getAsLong(),
                iban == null || iban.isJsonNull() ? "-" : iban.getAsString());
    }
}
