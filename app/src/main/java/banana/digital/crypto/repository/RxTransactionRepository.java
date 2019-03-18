package banana.digital.crypto.repository;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import androidx.room.Room;
import banana.digital.crypto.MainDatabase;
import banana.digital.crypto.TransactionDao;
import banana.digital.crypto.model.Transactions;
import banana.digital.crypto.service.Service;
import io.reactivex.Observable;
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


    public void loadTransactions() {
        new Thread(){
            @Override
            public void run() {
                Log.e("hdphdfa", "loaded");
                transactions.onNext(mTransactionDao.getAll());
            }
        }.start();
    }

    public void saveTransactions(List<Transactions.Result> transactions) {
        new Thread(){
            @Override
            public void run() {
                mTransactionDao.deleteAll();
                mTransactionDao.insert(transactions);
            }
        }.start();
    }
}
