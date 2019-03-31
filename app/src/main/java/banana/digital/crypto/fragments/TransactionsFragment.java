package banana.digital.crypto.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    viewModel viewModel;

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


        viewModel = ViewModelProviders.of(getActivity()).get(viewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeaderSize();


        viewModel.getTransactions("").observe(getActivity(), transactions -> {
            // когда получили транзакции - обновляем список
            adapter.swap(transactions);
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // когда пользователь вводит текст - запрашиваем транзакции
                viewModel.getTransactions(newText).observe(getActivity(), transactions -> {
                    // когда получили транзакции - обновляем список
                    adapter.swap(transactions);
                });
                return false;
            }
        });





        /*

            timestamp не сохраняется

         */
        if(!isNetworkAvailable()) {
            final String[] a = new String[1];
            viewModel.getTimestamp().observe(getActivity(), timestamp -> {
                a[0] = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp));
            });
            Toast.makeText(getActivity(), "No internet connection available \n loading data from " + a[0], Toast.LENGTH_SHORT).show();
            Log.e("kfipsakfgops", "no");
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    void setHeaderSize() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SCREEN_WIDTH_PX / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        hash.setLayoutParams(params);
        from.setLayoutParams(params);
        to.setLayoutParams(params);
        value.setLayoutParams(params);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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


class viewModel extends ViewModel {
    private MutableLiveData<List<Transactions.Result>> transactions = new MutableLiveData<>();
    private String query;
    private Disposable transactionsDisposable;

    private Disposable timestampDisposable;
    private MutableLiveData<Long> timestamp = new MutableLiveData<>();


    public LiveData<List<Transactions.Result>> getTransactions(String query) {
        this.query = query; // сохраняем поисковый запрос (чтобы потом отфильтровать)
        subscribeTransactions(); // подписываем на транзакции (если ещё не)
        return transactions;
    }

    private void subscribeTransactions() {
        if (transactionsDisposable != null) { // если уже подписались
            transactionsDisposable.dispose(); // отписываемся
        }
        // переподписываемся на все транзакции
        transactionsDisposable = RxTransactionRepository.getInstance().getTransactions()
                .subscribe(transactions -> {
                    // когда они приходят, фильтруем
                    final List<Transactions.Result> filteredTransactions = new ArrayList<>();
                    for (Transactions.Result transaction : transactions) {
                        if (transaction.getHash().contains(query)) {
                            filteredTransactions.add(transaction);
                        }
                    }
                    // и кладём в контейнер
                    this.transactions.setValue(filteredTransactions);
                });
    }




    public MutableLiveData<Long> getTimestamp() {
        subscribeTimestamp();
        return timestamp;
    }

    private void subscribeTimestamp() {
        if(timestampDisposable != null) {
            timestampDisposable.dispose();
        }

        timestampDisposable = RxTransactionRepository.getInstance().getTimestamp().subscribe(timestamp -> {
            this.timestamp.setValue(timestamp);
        });
    }
}