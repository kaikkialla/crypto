package banana.digital.crypto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import banana.digital.crypto.R;
import banana.digital.crypto.model.Transactions;
import banana.digital.crypto.repository.RxTransactionRepository;
import io.reactivex.disposables.Disposable;

import static banana.digital.crypto.MainActivity.SCREEN_WIDTH_PX;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class TransactionsFragment extends Fragment {

    public static RecyclerView recyclerView;
    public static Adapter adapter;

    static TextView hash;
    static TextView from;
    static TextView to;
    static TextView value;
    SearchView searchView;

    Disposable mDisposable;
    static LinearLayout header;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transactions_fragment, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);

        header = view.findViewById(R.id.header);

        hash = view.findViewById(R.id.hash);
        from = view.findViewById(R.id.from);
        to = view.findViewById(R.id.to);
        value = view.findViewById(R.id.value);

        searchView = (SearchView) view.findViewById(R.id.searchView);

        adapter = new Adapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

    }

    @Override
    public void onResume() {
        super.onResume();
        Presenter.setHeaderSize();

        mDisposable = io.reactivex.Observable.combineLatest(
                RxSearchView.queryTextChanges(searchView).debounce(500, TimeUnit.MILLISECONDS),
                RxTransactionRepository.getInstance().getTransactions(),
                (CharSequence query,  List<Transactions.Result> transactions) -> {
                    final List<Transactions.Result> filteredTransactions = new ArrayList<>();
                    for (Transactions.Result transaction : transactions) {
                        if(transaction.getHash().contains(query)) {
                            filteredTransactions.add(transaction);
                        }
                    }
                    return filteredTransactions;
                }).observeOn(mainThread()).subscribe(transactions -> {
                    adapter.swap(transactions);
                });




    }

    @Override
    public void onPause() {
        super.onPause();
        mDisposable.dispose();
    }

    public static void setHeaderSize(LinearLayout.LayoutParams params) {
        hash.setLayoutParams(params);
        from.setLayoutParams(params);
        to.setLayoutParams(params);
        value.setLayoutParams(params);
    }





    public static class Presenter {

        public static Presenter instance;

        public static Presenter getInstance() {
            if (instance == null) {
                instance = new Presenter();
            }
            return instance;
        }

        public static void setHeaderSize() {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            TransactionsFragment.setHeaderSize(params);
        }

        public static void setSizes(ViewHolder holder) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            Adapter.setSizes(holder, params);
        }

        public static void initHolder(List<Transactions.Result> mTransactions, int pos, ViewHolder holder) {
            Transactions.Result item = mTransactions.get(pos);

            String from = item.getFrom();
            String to = item.getTo();

            BigDecimal value0 = new BigDecimal(item.getValue());
            BigDecimal  value1 = Convert.fromWei(value0, Convert.Unit.ETHER);
            String value = value1.toString();

            String hash = item.getHash();

            item.setFrom(from);
            item.setTo(to);
            item.setHash(hash);
            item.setValue(value);

            Adapter.initHolder(holder, mTransactions.get(pos));
        }
    }





    static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        List<Transactions.Result> mTransactions = new ArrayList<>();
        FragmentActivity activity;

        public Adapter(FragmentActivity activity) {
            this.activity = activity;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View v = inflater.inflate(R.layout.transactions_item   , parent, false );
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Presenter.setSizes(holder);
            Presenter.initHolder(mTransactions, position, holder);


            holder.itemView.setOnClickListener(view -> {
                Fragment fragment = TransactionInfoFragment.newInstance(mTransactions.get(position));
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            });



        }

        @Override
        public int getItemCount() {
            return mTransactions.size();

        }

        public static void setSizes(ViewHolder holder, LinearLayout.LayoutParams params) {
            holder.from.setLayoutParams(params);
            holder.to.setLayoutParams(params);
            holder.value.setLayoutParams(params);
            holder.hash.setLayoutParams(params);
        }

        public static void initHolder(ViewHolder holder, Transactions.Result result) {
            holder.from.setText(result.getFrom());
            holder.to.setText(result.getTo());
            holder.value.setText(result.getValue());
            holder.hash.setText(result.getHash());
        }

        public void swap(List<Transactions.Result> list) {
            mTransactions = list;
            notifyDataSetChanged();
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