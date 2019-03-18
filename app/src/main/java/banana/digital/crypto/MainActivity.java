package banana.digital.crypto;




import android.os.Bundle;

import android.util.DisplayMetrics;

import com.astuetz.PagerSlidingTabStrip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.viewpager.widget.ViewPager;
import banana.digital.crypto.fragments.BalanceFragment;
import banana.digital.crypto.fragments.InfoFragment;
import banana.digital.crypto.fragments.TransactionsFragment;
import banana.digital.crypto.repository.RxTransactionRepository;


public class MainActivity extends AppCompatActivity {


//    ViewPager mViewPager;
//    Adapter adapter;
//    PagerSlidingTabStrip tabs;

    public static int SCREEN_WIDTH_PX;
    public static int SCREEN_HEIGHT_PX;
    public static float density;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxTransactionRepository.getInstance().initialize(this);
        setContentView(R.layout.activity_main);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = getResources().getDisplayMetrics().density;

        SCREEN_WIDTH_PX = dm.widthPixels;
        SCREEN_HEIGHT_PX = dm.heightPixels;


        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new TransactionsFragment()).commit();

//        mViewPager = findViewById(R.id.viewPager);
//        tabs = findViewById(R.id.tabs);
//        adapter = new Adapter(getSupportFragmentManager());
//        mViewPager.setAdapter(adapter);
//
//
//        tabs.setViewPager(mViewPager);
    }
}

//class Adapter extends androidx.fragment.app.FragmentPagerAdapter {
//
//    String[] titles = {"Transactions", "Balance", "Account info"};
//
//    public Adapter(FragmentManager fm) {
//        super(fm);
//    }
//
//
//    @Override
//    public int getCount() {
//        return titles.length;
//    }
//
//
//    @Override
//    public Fragment getItem(int i) {
//        switch (i) {
//            case 0:
//                return new TransactionsFragment();
//            case 1:
//                return new BalanceFragment();
//            case 2:
//                return new InfoFragment();
//            default:
//                return null;
//        }
//    }
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titles[position];
//    }
//}



//IVAZ67XPPXX5WFNUZMVEZKKT81BCKMYETE
//https://api.etherscan.io/api?module=account&action=balance&address=  &tag=latest&apikey=