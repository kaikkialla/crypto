package banana.digital.crypto;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import banana.digital.crypto.fragments.BalanceFragment;
import banana.digital.crypto.fragments.InfoFragment;
import banana.digital.crypto.fragments.TransactionsFragment;
import banana.digital.crypto.model.BalanceResult;
import banana.digital.crypto.model.TxListResult;
import banana.digital.crypto.service.EtherscanService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    ViewPager mViewPager;
    Adapter adapter;
    PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final TextView textView = findViewById(R.id.text_view);

        mViewPager = findViewById(R.id.viewPager);
        tabs = findViewById(R.id.tabs);
        adapter = new Adapter(this.getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        tabs.setViewPager(mViewPager);






   /*
        service.getBalance("0x0D0707963952f2fBA59dD06f2b425ace40b492Fe").enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                //textView.setText(response.body().result);
            }

            @Override
            public void onFailure(Call<BalanceResult> call, Throwable t) {

            }
        });
*/
   /*
        service.getTransactions("0x0D0707963952f2fBA59dD06f2b425ace40b492Fe").enqueue(new Callback<TxListResult>() {

            @Override
            public void onResponse(Call<TxListResult> call, Response<TxListResult> response) {
                Toast.makeText(MainActivity.this, "Response", Toast.LENGTH_SHORT).show();
                for(int i = 0; i <= response.body().result.size() - 1; i++) {
                    Log.e("fpkfpsfs", response.body().result.get(i).hash);
                }
            }

            @Override
            public void onFailure(Call<TxListResult> call, Throwable t) {         }
        });
    }
    */
    }
}

class Adapter extends FragmentStatePagerAdapter {

    String[] titles = {"Balance", "Transactions", "Account info"};

    public Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new BalanceFragment();
            case 1:
                return new TransactionsFragment();
            case 2:
                return new InfoFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}


//IVAZ67XPPXX5WFNUZMVEZKKT81BCKMYETE
//https://api.etherscan.io/api?module=account&action=balance&address=  &tag=latest&apikey=