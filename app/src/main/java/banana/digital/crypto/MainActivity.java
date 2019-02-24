package banana.digital.crypto;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

    public static int SCREEN_WIDTH_PX;
    public static int SCREEN_HEIGHT_PX;
    public static float density;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = getResources().getDisplayMetrics().density;

        SCREEN_WIDTH_PX = dm.widthPixels;
        SCREEN_HEIGHT_PX = dm.heightPixels;


        mViewPager = findViewById(R.id.viewPager);
        tabs = findViewById(R.id.tabs);
        adapter = new Adapter(this.getSupportFragmentManager());
        mViewPager.setAdapter(adapter);


        tabs.setViewPager(mViewPager);

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
                return new TransactionsFragment();
            case 1:
                return new BalanceFragment();
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