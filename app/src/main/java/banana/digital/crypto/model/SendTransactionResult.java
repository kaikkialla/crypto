package banana.digital.crypto.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendTransactionResult {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    class Error {

        @SerializedName("jsonrpc")
        @Expose
        private String jsonrpc;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("error")
        @Expose
        private Error error;

        public String getJsonrpc() {
            return jsonrpc;
        }

        public void setJsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Error getError() {
            return error;
        }

        public void setError(Error error) {
            this.error = error;
        }

    }

}


