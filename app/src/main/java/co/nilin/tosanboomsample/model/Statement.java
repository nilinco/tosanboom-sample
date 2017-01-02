package co.nilin.tosanboomsample.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by data on 1/2/2017.
 */

public class Statement {
    private final String serialNumber;
    private final String date;
    private final String description;
    private final long balance;
    private final long statementSerial;
    private final String branchCode;
    private final String branchName;
    private final String agentBranchCode;
    private final String agentBranchName;
    private final long transferAmount;
    private final String referenceNumber;
    private final long serial;

    public Statement(String serialNumber, String date, String description, long balance, long statementSerial, String branchCode, String branchName, String agentBranchCode, String agentBranchName, long transferAmount, String referenceNumber, long serial) {
        this.serialNumber = serialNumber;
        this.date = date;
        this.description = description;
        this.balance = balance;
        this.statementSerial = statementSerial;
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.agentBranchCode = agentBranchCode;
        this.agentBranchName = agentBranchName;
        this.transferAmount = transferAmount;
        this.referenceNumber = referenceNumber;
        this.serial = serial;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public long getBalance() {
        return balance;
    }

    public long getStatementSerial() {
        return statementSerial;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getAgentBranchCode() {
        return agentBranchCode;
    }

    public String getAgentBranchName() {
        return agentBranchName;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public long getSerial() {
        return serial;
    }

    public static Statement toStatement(JsonObject json) {
        JsonElement serialNumber = json.get("serial_number");
        JsonElement date = json.get("date");
        JsonElement description = json.get("description");
        JsonElement balance = json.get("balance");
        JsonElement statementSerial = json.get("statement_serial");
        JsonElement branchCode = json.get("branch_code");
        JsonElement branchName = json.get("branch_name");
        JsonElement agentBranchCode = json.get("agent_branch_code");
        JsonElement agentBranchName = json.get("agent_branch_name");
        JsonElement transferAmount = json.get("transfer_amount");
        JsonElement referenceNumber = json.get("reference_number");
        JsonElement serial = json.get("serial");

        return new Statement(serialNumber == null || serialNumber.isJsonNull() ? "" : serialNumber.getAsString(),
                date == null || date.isJsonNull() ? "" : date.getAsString(),
                description == null || description.isJsonNull() ? "-" : description.getAsString(),
                balance == null || balance.isJsonNull() ? 0 : balance.getAsLong(),
                statementSerial == null || statementSerial.isJsonNull() ? 0 : statementSerial.getAsLong(),
                branchCode == null || branchCode.isJsonNull() ? "" : branchCode.getAsString(),
                branchName == null || branchName.isJsonNull() ? "" : branchName.getAsString(),
                agentBranchCode == null || agentBranchCode.isJsonNull() ? "" : agentBranchCode.getAsString(),
                agentBranchName == null || agentBranchName.isJsonNull() ? "" : agentBranchName.getAsString(),
                transferAmount == null || transferAmount.isJsonNull() ? 0 : transferAmount.getAsLong(),
                referenceNumber == null || referenceNumber.isJsonNull() ? "-" : referenceNumber.getAsString(),
                serial == null || serial.isJsonNull() ? 0 : serial.getAsLong());
    }
}
