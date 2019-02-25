package banana.digital.crypto.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import banana.digital.crypto.R;
import banana.digital.crypto.model.Transactions;
import banana.digital.crypto.repository.RxTransactionRepository;
import banana.digital.crypto.repository.TransactionsRepository;
import banana.digital.crypto.service.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static banana.digital.crypto.MainActivity.SCREEN_WIDTH_PX;

public class TransactionsFragment extends Fragment {

    public static RecyclerView recyclerView;
    public static Adapter adapter;

    static TextView hash;
    static TextView from;
    static TextView to;
    static TextView value;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transactions_fragment, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);

        hash = view.findViewById(R.id.hash);
        from = view.findViewById(R.id.from);
        to = view.findViewById(R.id.to);
        value = view.findViewById(R.id.value);


        adapter = new Adapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        setSizes();
    }




    public static void setSizes() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        hash.setLayoutParams(params);
        from.setLayoutParams(params);
        to.setLayoutParams(params);
        value.setLayoutParams(params);
    }


    @Override
    public void onResume() {
        super.onResume();
        /*
        TransactionsRepository.getInstance().getTransactions(new TransactionsRepository.Callback(){
            @Override
            public void onNext(List<Transactions.Result> transactions) {

            }
        }, "0x3f5CE5FBFe3E9af3971dD833D26bA9b5C936f0bE");
        */
        //RxTransactionRepository.getInstance().getTransactions().subscribe();
        //Presenter.getInstance().getTransactions("0x3f5CE5FBFe3E9af3971dD833D26bA9b5C936f0bE");
    }




    public static class Presenter {

        public static Presenter instance;
        public static Presenter getInstance() {
            if (instance == null) { instance = new Presenter(); }
            return instance;
        }
//0x3f5CE5FBFe3E9af3971dD833D26bA9b5C936f0bE
        public static void getTransactions(String address) {
            Service.getEtherscanSevices().getTransactions(address).enqueue(new Callback<Transactions>() {
                @Override
                public void onResponse(Call<Transactions> call, Response<Transactions> response) {
                    List<Transactions.Result> transactions = response.body().getResult();
                    adapter.swap(transactions);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<Transactions> call, Throwable t) {

                }
            });
        }
    }





    static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        List<Transactions.Result> mTransactions = new ArrayList<>();
        Context context;


        public Adapter(Context context) {
            this.context = context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.transactions_item   , parent, false );
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            setSizes(holder);

            Transactions.Result item = mTransactions.get(position);

            String from = item.getFrom();
            String to = item.getTo();

            BigDecimal value0 = new BigDecimal(item.getValue());
            BigDecimal  value1 = Convert.fromWei(value0, Convert.Unit.ETHER);
            String value = value1.toString();

            String hash = item.getHash();

            holder.from.setText(from);
            holder.to.setText(to);
            holder.value.setText(value);
            holder.hash.setText(hash);
        }

        @Override
        public int getItemCount() {
            return mTransactions.size();

        }

        public void swap(List<Transactions.Result> list) {
            mTransactions = list;
            notifyDataSetChanged();
        }

        public static void setSizes(ViewHolder holder) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.from.setLayoutParams(params);
            holder.to.setLayoutParams(params);
            holder.value.setLayoutParams(params);
            holder.hash.setLayoutParams(params);
        }
    }






    public static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        TextView from;
        TextView to;
        TextView value;
        TextView hash;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            this.from = itemView.findViewById(R.id.from);
            this.to = itemView.findViewById(R.id.to);
            this.value = itemView.findViewById(R.id.value);
            this.hash = itemView.findViewById(R.id.hash);
        }
    }
}
