package banana.digital.crypto.repository;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import androidx.room.Room;
import banana.digital.crypto.Executor;
import banana.digital.crypto.MainDatabase;
import banana.digital.crypto.TransactionDao;
import banana.digital.crypto.model.Transactions;
import banana.digital.crypto.service.Service;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RxTransactionRepository {
    public static RxTransactionRepository instance;

    BehaviorSubject<List<Transactions.Result>> transactions = BehaviorSubject.create();
    TransactionDao mTransactionDao;


    private RxTransactionRepository(){}


    public static RxTransactionRepository getInstance() {
        if (instance == null) {
            instance = new RxTransactionRepository();
        }
        return instance;
    }



    public void updateTransactions() {
        Service.getEtherscanSevices().getTransactions("0xA4Eb0f8D1DAa48D6e6675022a208006fDC89606B").enqueue(new Callback<Transactions>() {
            @Override
            public void onResponse(Call<Transactions> call, Response<Transactions> response) {
                if(response.isSuccessful()) {
                    transactions.onNext(response.body().getResult());
                    saveTransactions(response.body().getResult());
                }
            }
            @Override
            public void onFailure(Call<Transactions> call, Throwable t) {

            }
        });
    }


    public Observable<List<Transactions.Result>> getTransactions() {
        updateTransactions();
        return transactions;
    }


    public void initialize(Context context) {
        mTransactionDao = Room.databaseBuilder(context.getApplicationContext(), MainDatabase.class, "database").build().getTransactionDao();
        loadTransactions();
    }


    @SuppressLint("StaticFieldLeak")
    public void loadTransactions() {
        new AsyncTask<String, Integer, Void>() {

            @Override
            protected Void doInBackground(String... strings) {
                transactions.onNext(mTransactionDao.getAll());
                return null;
            }
        };

    }


    @SuppressLint({"StaticFieldLeak", "CheckResult"})
    public void saveTransactions(List<Transactions.Result> transactions) {
        Single.fromCallable(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                mTransactionDao.deleteAll();
                mTransactionDao.insert(transactions);
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(value -> {
                    //код будет выполнен в главном потоке
                    return value;
                }).observeOn(Schedulers.io())
                .map(value -> {
                    //код будет выполнен в рабочем потоке
                    return value;
        });

//        new AsyncTask<String, Integer, Void>() {
//            @Override
//            protected Void doInBackground(String... strings) {
//                mTransactionDao.deleteAll();
//                mTransactionDao.insert(transactions);
//                return null;
//            }
//        };

    }
}
