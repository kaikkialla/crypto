package banana.digital.crypto.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {
    static EtherscanService etherscanService;

    public static EtherscanService getEtherscanSevices() {
        EtherscanService service = null;
        if (etherscanService == null) {
            final Gson gson = new GsonBuilder().setLenient().create();
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://etherscan.io/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            service = retrofit.create(EtherscanService.class);
        }
        return service;
    }



}
