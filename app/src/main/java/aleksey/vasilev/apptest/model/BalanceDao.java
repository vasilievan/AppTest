package aleksey.vasilev.apptest.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BalanceDao {
    @Query("SELECT * FROM Balance WHERE uid=(SELECT max(uid) FROM Balance)")
    Balance getBalance();

    @Insert
    void insertBalance(Balance balance);
}