package banana.digital.crypto;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import banana.digital.crypto.model.Transactions;

@Dao
public interface TransactionDao {


    @Insert
    void insert(List<Transactions.Result> transactions);

    @Query("SELECT * FROM tx")
    List<Transactions.Result> getAll();

    @Query("SELECT * FROM tx WHERE hash = :hash")
    List<Transactions.Result> getByHash(String hash);

    @Delete
    void delete(Transactions.Result transaction);

    @Query("DELETE FROM tx")
    void deleteAll();

}
