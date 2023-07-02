package aleksey.vasilev.apptest.model;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

public class Converters {
    @TypeConverter
    public BigDecimal stringToBigDecimal(String value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(value);
    }

    @TypeConverter
    public String bigDecimalToString(BigDecimal value) {
        return  value == null ? "" : value.toPlainString();
    }
}