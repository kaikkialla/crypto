package banana.digital.crypto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.text_view);

        final Gson gson = new GsonBuilder().setLenient().create();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://etherscan.io/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final EtherscanService service = retrofit.create(EtherscanService.class);

        service.getBalance("0x0D0707963952f2fBA59dD06f2b425ace40b492Fe").enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                textView.setText(response.body().result);
            }

            @Override
            public void onFailure(Call<BalanceResult> call, Throwable t) {

            }
        });
    }
}


//IVAZ67XPPXX5WFNUZMVEZKKT81BCKMYETE
//https://api.etherscan.io/api?module=account&action=balance&address=  &tag=latest&apikey=