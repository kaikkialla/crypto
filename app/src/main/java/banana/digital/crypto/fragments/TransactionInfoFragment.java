package banana.digital.crypto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.Observable;

import androidx.fragment.app.Fragment;
import banana.digital.crypto.R;
import banana.digital.crypto.model.Transactions;

public class TransactionInfoFragment extends Fragment {

    static TextView mHash;
    static TextView mFrom;
    static TextView mTo;
    static TextView mValue;
    static TextView mTimeStamp;
    static Transactions.Result transaction;


    public static androidx.fragment.app.Fragment newInstance(Transactions.Result result) {
        TransactionInfoFragment fragment = new TransactionInfoFragment();
        transaction = result;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transaction_info_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHash = view.findViewById(R.id.hash);
        mFrom = view.findViewById(R.id.from);
        mTo = view.findViewById(R.id.to);
        mValue = view.findViewById(R.id.value);
        mTimeStamp = view.findViewById(R.id.timestamp);
    }

    @Override
    public void onResume() {
        super.onResume();
        Presenter.load(transaction);
    }

    public static void setTimeStamp(String timeStamp) {
        mTimeStamp.setText(timeStamp);
    }
    public static void setHash(String hash) {
        mHash.setText("Hash: " + hash);
    }
    public static void setFrom(String from) {
        mFrom.setText("From: " + from);
    }
    public static void setTo(String to) {
        mTo.setText("To: " + to);
    }
    public static void setValue(String value) {
        mValue.setText("Value: " + value);
    }


    public static class Presenter {

        public static void load(Transactions.Result transaction) {

            int timestamp = Integer.parseInt(transaction.getTimeStamp());
            Date time=new java.util.Date((long)timestamp*1000);

            String timeStamp = time.toString();
            String hash = transaction.getHash();
            String from = transaction.getFrom();
            String to = transaction.getTo();
            String value = transaction.getValue();

            TransactionInfoFragment.setTimeStamp(timeStamp);
            TransactionInfoFragment.setHash(hash);
            TransactionInfoFragment.setFrom(from);
            TransactionInfoFragment.setTo(to);
            TransactionInfoFragment.setValue(value);

        }
}

}


