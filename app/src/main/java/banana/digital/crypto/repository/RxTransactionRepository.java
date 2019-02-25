package banana.digital.crypto.repository;


import java.util.List;

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

}
