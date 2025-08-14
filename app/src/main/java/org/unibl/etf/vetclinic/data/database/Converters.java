package org.unibl.etf.vetclinic.data.database;

import androidx.room.TypeConverter;

import org.unibl.etf.vetclinic.data.entities.UnpaidService;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromStatus(UnpaidService.Status status) {
        return status == null ? null : status.name();
    }

    @TypeConverter
    public static UnpaidService.Status toStatus(String value) {
        return value == null ? null : UnpaidService.Status.valueOf(value);
    }

}

