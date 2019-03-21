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
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import banana.digital.crypto.R;
import banana.digital.crypto.model.Transactions;
import banana.digital.crypto.repository.RxTransactionRepository;
import io.reactivex.disposables.Disposable;

import static banana.digital.crypto.MainApplication.SCREEN_WIDTH_PX;
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

    ViewModel viewModel;

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


        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeaderSize();

        viewModel.getTransactions("0").observe(getActivity(), results -> {
            adapter.swap(results);
            recyclerView.setAdapter(adapter);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.getTransactions(newText).observe(getActivity(), results -> {
                    adapter.swap(results);
                    recyclerView.setAdapter(adapter);
                });
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mDisposable.dispose();
    }

    void setHeaderSize() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        hash.setLayoutParams(params);
        from.setLayoutParams(params);
        to.setLayoutParams(params);
        value.setLayoutParams(params);
    }







    public static class ViewModel extends androidx.lifecycle.ViewModel {

        public MutableLiveData<List<Transactions.Result>> transactions = new MutableLiveData<>();
        String query;
        public Disposable transactionsDisposable;

        public ViewModel() {
        }

        public LiveData<List<Transactions.Result>> getTransactions(String query) {
            this.query = query;
            subscribeTransactions();
            return transactions;
        }

        public void subscribeTransactions() {
            if (transactionsDisposable != null) {
                transactionsDisposable.dispose();
            }

            transactionsDisposable = RxTransactionRepository.getInstance().getTransactions().subscribe(transactions -> {
                final List<Transactions.Result> filteredTransactions = new ArrayList<>();
                for (Transactions.Result transaction : transactions) {
                    if (transaction.getHash().contains(query)) {
                        filteredTransactions.add(transaction);
                    }
                }
                this.transactions.setValue(filteredTransactions);
            });
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
            setSizes(holder);
            initHolder(holder, mTransactions.get(position));
            holder.itemView.setOnClickListener(view -> {
                Fragment fragment = TransactionInfoFragment.newInstance(mTransactions.get(position));
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            });
        }

        @Override
        public int getItemCount() {
            return mTransactions.size();
        }

        void setSizes(ViewHolder holder) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.from.setLayoutParams(params);
            holder.to.setLayoutParams(params);
            holder.value.setLayoutParams(params);
            holder.hash.setLayoutParams(params);
        }

        void initHolder(ViewHolder holder, Transactions.Result result) {

            String from = result.getFrom();
            String to = result.getTo();

            BigDecimal value0 = new BigDecimal(result.getValue());
            BigDecimal  value1 = Convert.fromWei(value0, Convert.Unit.ETHER);
            String value = value1.toString();

            String hash = result.getHash();

            result.setFrom(from);
            result.setTo(to);
            result.setHash(hash);
            result.setValue(value);

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