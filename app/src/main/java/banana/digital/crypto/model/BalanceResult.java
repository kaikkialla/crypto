package banana.digital.crypto.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceResult {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("result")
    @Expose
    public String result;






}
