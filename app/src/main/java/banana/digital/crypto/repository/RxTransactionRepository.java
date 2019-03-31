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

import static banana.digital.crypto.Executor.EXECUTOR;

public class RxTransactionRepository {


    private static RxTransactionRepository sInstance;

    private TransactionDao mTransactionDao;
    private BehaviorSubject<List<Transactions.Result>> transactions = BehaviorSubject.create();
    private BehaviorSubject<Long> timestamps=  BehaviorSubject.create();

    private static class InstanceHolder {
        static final RxTransactionRepository sInstance = new RxTransactionRepository();
    }

    private RxTransactionRepository() {
        // stub
    }

    public static RxTransactionRepository getInstance() {
        return InstanceHolder.sInstance;
    }



    public Observable<List<Transactions.Result>> getTransactions() {
        updateTransactions();
        return transactions;
    }

    public Observable<Long> getTimestamp() {
        return timestamps;
    }


    public void initialize(Context context) {
        mTransactionDao = Room.databaseBuilder(context.getApplicationContext(), MainDatabase.class, "database").build().getTransactionDao();
        loadTransactions();
    }


    private void updateTransactions() {
        Service.getEtherscanSevices().getTransactions("0xA4Eb0f8D1DAa48D6e6675022a208006fDC89606B").enqueue(new Callback<Transactions>() {
            @Override
            public void onResponse(Call<Transactions> call, Response<Transactions> response) {
                if(response.isSuccessful()) {
                    transactions.onNext(response.body().getResult());
                    saveTransactions(response.body().getResult());
                    updateTimestamp();
                }
            }
            @Override
            public void onFailure(Call<Transactions> call, Throwable t) {

            }
        });
    }


    private void updateTimestamp() {
        long timestamp = System.currentTimeMillis();
        timestamps.onNext(timestamp);
    }


    private void loadTransactions() {
        EXECUTOR.execute(() -> transactions.onNext(mTransactionDao.getAll()));
//        new AsyncTask<String, Integer, Void>() {
//
//            @Override
//            protected Void doInBackground(String... strings) {
//                transactions.onNext(mTransactionDao.getAll());
//                return null;
//            }
//        };


    }


    private void saveTransactions(List<Transactions.Result> transactions) {
        Single.fromCallable(() -> {
            mTransactionDao.deleteAll();
            mTransactionDao.insert(transactions);
            return true;
        }).subscribeOn(Schedulers.io()).subscribe();

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
