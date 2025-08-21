package org.unibl.etf.vetclinic.data.database;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.unibl.etf.vetclinic.data.dao.AppointmentDao;
import org.unibl.etf.vetclinic.data.dao.MedicalRecordDao;
import org.unibl.etf.vetclinic.data.dao.PaymentDao;
import org.unibl.etf.vetclinic.data.dao.PetDao;
import org.unibl.etf.vetclinic.data.dao.RoleDao;
import org.unibl.etf.vetclinic.data.dao.ServiceDao;
import org.unibl.etf.vetclinic.data.dao.UnpaidServiceDao;
import org.unibl.etf.vetclinic.data.dao.UserDao;
import org.unibl.etf.vetclinic.data.dao.UserPreferencesDao;
import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.MedicalRecord;
import org.unibl.etf.vetclinic.data.entities.Payment;
import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.data.entities.Role;
import org.unibl.etf.vetclinic.data.entities.Service;
import org.unibl.etf.vetclinic.data.entities.UnpaidService;
import org.unibl.etf.vetclinic.data.entities.User;
import org.unibl.etf.vetclinic.data.entities.UserPreferences;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public abstract RoleDao roleDao();
    public abstract UserDao userDao();
    public abstract UserPreferencesDao userPreferencesDao();
    public abstract ServiceDao serviceDao();
    public abstract PetDao petDao();
    public abstract AppointmentDao appointmentDao();
    public abstract MedicalRecordDao medicalRecordDao();
    public abstract PaymentDao paymentDao();
    public abstract UnpaidServiceDao unpaidServiceDao();

    private static volatile AppDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

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
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(() -> {
                                        AppDatabase database = INSTANCE;
                                        if (database != null) {
                                            // Kastuj kontekst u Application
                                            Application app = (Application) context.getApplicationContext();
                                            DatabaseSeeder.seed(database, app);
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
