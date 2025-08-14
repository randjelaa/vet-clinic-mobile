package org.unibl.etf.vetclinic.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.MedicalRecord;
import org.unibl.etf.vetclinic.data.entities.Payment;
import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.data.entities.Role;
import org.unibl.etf.vetclinic.data.entities.Service;
import org.unibl.etf.vetclinic.data.entities.UnpaidService;
import org.unibl.etf.vetclinic.data.entities.User;
import org.unibl.etf.vetclinic.data.entities.UserPreferences;

@Database(
        entities = {
                Role.class,
                User.class,
                UserPreferences.class,
                Service.class,
                Pet.class,
                Appointment.class,
                MedicalRecord.class,
                Payment.class,
                UnpaidService.class
        },
        version = 1,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    // DAO interfejsi (definišemo kad ih napravimo)
//    public abstract RoleDao roleDao();
//    public abstract UserDao userDao();
//    public abstract UserPreferencesDao userPreferencesDao();
//    public abstract ServiceDao serviceDao();
//    public abstract PetDao petDao();
//    public abstract AppointmentDao appointmentDao();
//    public abstract MedicalRecordDao medicalRecordDao();
//    public abstract PaymentDao paymentDao();
//    public abstract UnpaidServiceDao unpaidServiceDao();

    // Singleton instance
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "vetclinic_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

