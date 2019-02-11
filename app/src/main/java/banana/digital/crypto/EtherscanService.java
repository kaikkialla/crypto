package banana.digital.crypto;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EtherscanService {


    String API_KEY = "IVAZ67XPPXX5WFNUZMVEZKKT81BCKMYETE";

    @GET("https://api.etherscan.io/api?module=account&action=balance&tag=latest&apikey=" + API_KEY)
    Call<BalanceResult> getBalance(@Query("address") String address);

    @GET("http://api.etherscan.io/api?module=account&action=txlist&startblock=0&endblock=99999999&sort=asc&apikey=" + API_KEY)
    Call<BalanceResult> getTransactions(@Query("address") String address);

}
