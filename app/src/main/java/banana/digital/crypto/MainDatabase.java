package banana.digital.crypto;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;
import banana.digital.crypto.model.Transactions;

@Database(entities = Transactions.Result.class, version = 1, exportSchema = false)
public abstract class MainDatabase extends RoomDatabase{
    public abstract TransactionDao getTransactionDao();
}
