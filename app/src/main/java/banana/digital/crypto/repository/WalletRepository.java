package banana.digital.crypto.repository;

import org.web3j.crypto.Credentials;

public class WalletRepository {
    private static final String key = "0xc6381433ee0afbd6b5ad261fa3f5bc27e6d91011069e129459ab58586f9b8e35";
    private Credentials mCredentials;
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
}


//mnemonic - dinner weekend tank ridge toilet verb flock disorder cool indicate clown icon
//private - 0xc6381433ee0afbd6b5ad261fa3f5bc27e6d91011069e129459ab58586f9b8e35
