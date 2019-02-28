package banana.digital.crypto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import banana.digital.crypto.R;
import banana.digital.crypto.model.Transactions;

public class TransactionInfoFragment extends Fragment {

    TextView mHash;
    TextView mFrom;
    TextView mTo;
    TextView mValue;
    static Transactions.Result transaction;


    public static androidx.fragment.app.Fragment newInstance(Transactions.Result list) {
        TransactionInfoFragment fragment = new TransactionInfoFragment();
        transaction = list;
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
    }

    @Override
    public void onResume() {
        super.onResume();
        Presenter.load(transaction);
    }

    void setHash(String hash) {
        mHash.setText(hash);
    }

    void setFrom(String from) {
        mFrom.setText(from);
    }
    void setTo(String to) {
        mTo.setText(to);
    }
    void setValue(String value) {
        mValue.setText(value);
    }


    public static class Presenter {

        public static void load(Transactions.Result transaction) {
            TransactionInfoFragment fragment = new TransactionInfoFragment();

            String hash = transaction.getHash();
            String from = transaction.getFrom();
            String to = transaction.getTo();
            String value = transaction.getValue();

            fragment.setHash(hash);
            fragment.setFrom(from);
            fragment.setTo(to);
            fragment.setValue(value);

        }
    }
}


