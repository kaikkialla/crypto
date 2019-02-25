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
        Service.getEtherscanSevices().getTransactions("0x3f5CE5FBFe3E9af3971dD833D26bA9b5C936f0bE").enqueue(new Callback<Transactions>() {
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
