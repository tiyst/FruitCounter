package tech.tiyst.fruitcounter.Database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DatabaseTypeConverters {

    @TypeConverter
    public static Date fromTimestamp(Long timeEpoch) {
        return timeEpoch == null ? null : new Date(timeEpoch);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
