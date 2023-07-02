package aleksey.vasilev.apptest;

import android.app.Application;

import androidx.room.Room;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import aleksey.vasilev.apptest.model.Balance;
import aleksey.vasilev.apptest.model.BalanceDao;
import aleksey.vasilev.apptest.model.BalanceDatabase;

public class AppTestApplication extends Application {
    private BalanceDatabase balanceDatabase;
    private BalanceDao balanceDao;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private static final String SATOSHI_BALANCE = "25";
    private static final String BTC_BALANCE = "0.00000025";
    private static final long PERCENTAGE = 25L;

    public BalanceDao getBalanceDao() {
        return balanceDao;
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void onCreate() {
        executorService.execute(() -> {
            balanceDatabase = Room.databaseBuilder(getApplicationContext(),
                    BalanceDatabase.class, getPackageName()).build();
            balanceDao = balanceDatabase.balanceDao();
            final Balance balance = balanceDao.getBalance();
            if (balance == null) {
                balanceDao.insertBalance(new Balance(
                        new BigDecimal(SATOSHI_BALANCE),
                        new BigDecimal(BTC_BALANCE),
                        PERCENTAGE,
                        System.currentTimeMillis())
                );
            }
        });
        super.onCreate();
    }
}
