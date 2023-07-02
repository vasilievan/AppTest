package aleksey.vasilev.apptest.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.math.BigDecimal;

@Entity
public class Balance {
    public Balance(BigDecimal valueSatoshi, BigDecimal valueBTC, long percent, long dateTime) {
        this.valueSatoshi = valueSatoshi;
        this.valueBTC = valueBTC;
        this.dateTime = dateTime;
        this.percent = percent;
    }

    @PrimaryKey(autoGenerate = true)
    public Long uid;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "value_satoshi")
    public BigDecimal valueSatoshi;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "value_btc")
    public BigDecimal valueBTC;

    @ColumnInfo(name = "value_percent")
    public long percent;

    @ColumnInfo(name = "date_time")
    public Long dateTime;
}