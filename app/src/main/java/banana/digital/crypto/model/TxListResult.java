package banana.digital.crypto.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import banana.digital.crypto.model.Transaction;

public class TxListResult {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;
/*
    @SerializedName("result")
    @Expose
    public List result;
*/
    @SerializedName("result")
    @Expose
    public List<Transaction> result;





}
