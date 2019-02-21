package banana.digital.crypto.repository;

import android.provider.Settings;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Convert;

import java.math.BigInteger;

import banana.digital.crypto.BuildConfig;
import banana.digital.crypto.model.SendTransactionResult;
import banana.digital.crypto.service.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletRepository {
    private static final String key = BuildConfig.WALLET_PRIVATE_KEY;
    //private static final String key = "0xc6381433ee0afbd6b5ad261fa3f5bc27e6d91011069e129459ab58586f9b8e35";
    public static Credentials mCredentials;
    public static WalletRepository instance;





    private WalletRepository(){
        mCredentials = Credentials.create(key);
    }


    public static WalletRepository getInstance() {
        if (instance == null) {
            instance = new WalletRepository();
        }
        return instance;
    }


    public String getAdress() {
        return mCredentials.getAddress();
    }

    public void sendTransaction() {
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(BigInteger.ZERO,
                Convert.toWei("10", Convert.Unit.GWEI).toBigInteger(),
                BigInteger.valueOf(31000), "",
                BigInteger.valueOf(0));
        byte[] message = TransactionEncoder.signMessage(rawTransaction, mCredentials);
        String hex = Hex.toHexString(message);
        Service.getEtherscanSevices().sendTransaction(hex).enqueue(new Callback<SendTransactionResult>() {
            @Override
            public void onResponse(Call<SendTransactionResult> call, Response<SendTransactionResult> response) {

            }

            @Override
            public void onFailure(Call<SendTransactionResult> call, Throwable t) {

            }
        });
    }

}



//mnemonic - dinner weekend tank ridge toilet verb flock disorder cool indicate clown icon
//private - 0xc6381433ee0afbd6b5ad261fa3f5bc27e6d91011069e129459ab58586f9b8e35
