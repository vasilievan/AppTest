package aleksey.vasilev.apptest.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Balance.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class BalanceDatabase extends RoomDatabase {
    public abstract BalanceDao balanceDao();
}