package banana.digital.crypto.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import banana.digital.crypto.R;
import banana.digital.crypto.model.BalanceResult;
import banana.digital.crypto.service.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private static TextView balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        balance = view.findViewById(R.id.balance);
    }

    @Override
    public void onResume() {
        super.onResume();
        Presenter.getInstance().requestBalance("0x3f5CE5FBFe3E9af3971dD833D26bA9b5C936f0bE");
    }

    public static void showBalance(String i) {
        balance.setText(i);
    }




    private static class Presenter {


        DecimalFormat decimalFormat = new DecimalFormat(".##");
        public static Presenter instance;
        public static  Presenter getInstance() {
            if (instance == null) { instance = new Presenter(); }
            return instance;
        }

        public void requestBalance(String adress) {
            Service.getEtherscanSevices().getBalance(adress).enqueue(new Callback<BalanceResult>() {
                @Override
                public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                    BigDecimal value = new BigDecimal(response.body().result);
                    BigDecimal  balance = Convert.fromWei(value, Convert.Unit.ETHER);
                    //float balance = i.floatValue();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Ether: ");
                    stringBuilder.append(decimalFormat.format(balance));
                    stringBuilder.append("\nUSD: ");
                    stringBuilder.append(decimalFormat.format(balance.doubleValue() * 149.18));
                    String result = stringBuilder.toString();

                    BalanceFragment.showBalance(result);
                }

                @Override
                public void onFailure(Call<BalanceResult> call, Throwable t) {
                }
            });
        }
/*
        public int BalanceFromatter(BigDecimal i){
            String s = decimalFormat.format(i);
            String main = s.split(".")[0];
            String decimal = s.split(".")[1];
            int mainLenght = main.length();
            char[] chars = main.toCharArray();
            for(int y = 0; y <= mainLenght; y++) {
                if(y % 3 == 0) {

                }
            }

            int balance = 0;
            return balance;
        }
        */
    }
}

