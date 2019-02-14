package banana.digital.crypto.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.web3j.utils.Convert;

import java.math.BigDecimal;

import banana.digital.crypto.R;
import banana.digital.crypto.model.BalanceResult;
import banana.digital.crypto.service.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private static TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.textView);
    }

    @Override
    public void onResume() {
        super.onResume();
        Presenter.getInstance().requestBalance();
    }

    public static void showBalance(String balance) {
        textView.setText(balance);
    }
}

class Presenter {

    public static Presenter instance;
    public static Presenter getInstance() {
        if (instance == null) { instance = new Presenter(); }
        return instance;
    }

    public void requestBalance() {
        Service.getEtherscanSevices().getBalance("0x582A535CB6b1D13e078f5E7e5093B3d3D3Cc255b").enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                BigDecimal value = new BigDecimal(response.body().result);
                String displayedValue = Convert.fromWei(value, Convert.Unit.ETHER).toString();
                BalanceFragment.showBalance(displayedValue);
            }

            @Override
            public void onFailure(Call<BalanceResult> call, Throwable t) {
            }
        });
    }
}
