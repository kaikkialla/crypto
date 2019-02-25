package banana.digital.crypto.repository;


import java.util.ArrayList;
import java.util.List;

import banana.digital.crypto.model.Transactions;
import banana.digital.crypto.service.Service;
import retrofit2.Call;
import retrofit2.Response;

public class TransactionsRepository {
    public static TransactionsRepository instance;

    List<Transactions.Result> transactions = new ArrayList<>();

    private TransactionsRepository(){}


    public static TransactionsRepository getInstance() {
        if (instance == null) {
            instance = new TransactionsRepository();
        }
        return instance;
    }



    public void getTransactions(Callback callback, String address) {


        callback.onNext(transactions);
        Service.getEtherscanSevices().getTransactions(address).enqueue(new retrofit2.Callback<Transactions>() {
            @Override
            public void onResponse(Call<Transactions> call, Response<Transactions> response) {
                if(response.body().getResult() != null) {
                    transactions = response.body().getResult();
                    callback.onNext(transactions);
                }

            }

            @Override
            public void onFailure(Call<Transactions> call, Throwable t) {

            }
        });
    }

    public interface Callback{
        void onNext(List<Transactions.Result> transactions);
    }
}
