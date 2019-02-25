package banana.digital.crypto.service;


import banana.digital.crypto.model.BalanceResult;
import banana.digital.crypto.model.SendTransactionResult;
import banana.digital.crypto.model.Transactions;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EtherscanService {


    String API_KEY = "IVAZ67XPPXX5WFNUZMVEZKKT81BCKMYETE";

    @GET("https://api.etherscan.io/api?module=account&action=balance&tag=latest&apikey=" + API_KEY)
    Call<BalanceResult> getBalance(@Query("address") String address);

    @GET("https://api.etherscan.io/api?module=account&action=txlist&startblock=0&endblock=99999999&sort=asc&apikey=" + API_KEY)
    Call<Transactions> getTransactions(@Query("address") String address);


    @GET("https://api.etherscan.io/api?module=proxy&action=eth_sendRawTransaction&apikey=" + API_KEY)
    Call<SendTransactionResult> sendTransaction(@Query("hex") String hex);
}
