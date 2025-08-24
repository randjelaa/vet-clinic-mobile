package org.unibl.etf.vetclinic.data.database;

import android.util.Log;

import org.unibl.etf.vetclinic.data.dao.*;
import org.unibl.etf.vetclinic.data.entities.*;

import java.util.Calendar;
import java.util.Date;

import android.app.Application;
import org.unibl.etf.vetclinic.repository.UserRepository;

public class DatabaseSeeder {

    public static void seed(AppDatabase db, Application application) {
        try {
            //TODO prosiriti

            // === ROLES ===
            RoleDao roleDao = db.roleDao();

            Role roleAdmin = new Role();
            roleAdmin.Name = "Admin";
            int adminRoleId = (int) roleDao.insert(roleAdmin);

            Role roleVet = new Role();
            roleVet.Name = "Veterinarian";
            int vetRoleId = (int) roleDao.insert(roleVet);

            Role roleClient = new Role();
            roleClient.Name = "Client";
            int clientRoleId = (int) roleDao.insert(roleClient);

            // === USERS ===
            UserRepository userRepo = new UserRepository(application);

            User admin = new User();
            admin.Email = "admin@vet.com";
            admin.Name = "Admin User";
            admin.Password = "admin123";
            admin.RoleID = adminRoleId;
            int adminId = userRepo.insertUserAndPreferencesSync(admin);

            User vet1 = new User();
            vet1.Email = "vet1@vet.com";
            vet1.Name = "Dr. Vet One";
            vet1.Password = "vet123";
            vet1.RoleID = vetRoleId;
            int vet1Id = userRepo.insertUserAndPreferencesSync(vet1);

            User vet2 = new User();
            vet2.Email = "vet2@vet.com";
            vet2.Name = "Dr. Vet Two";
            vet2.Password = "vet123";
            vet2.RoleID = vetRoleId;
            int vet2Id = userRepo.insertUserAndPreferencesSync(vet2);

            User client1 = new User();
            client1.Email = "client1@mail.com";
            client1.Name = "John Doe";
            client1.Password = "client123";
            client1.RoleID = clientRoleId;
            int client1Id = userRepo.insertUserAndPreferencesSync(client1);

            User client2 = new User();
            client2.Email = "client2@mail.com";
            client2.Name = "Jane Smith";
            client2.Password = "client123";
            client2.RoleID = clientRoleId;
            int client2Id = userRepo.insertUserAndPreferencesSync(client2);

            User client3 = new User();
            client3.Email = "client3@mail.com";
            client3.Name = "Alice Brown";
            client3.Password = "client123";
            client3.RoleID = clientRoleId;
            int client3Id = userRepo.insertUserAndPreferencesSync(client3);

            // === PETS ===
            PetDao petDao = db.petDao();

            Pet pet1 = new Pet();
            pet1.Name = "Rex";
            pet1.Species = "Dog";
            pet1.Breed = "German Shepherd";
            pet1.OwnerID = client1Id;
            int pet1Id = (int) petDao.insert(pet1);

            Pet pet2 = new Pet();
            pet2.Name = "Milo";
            pet2.Species = "Cat";
            pet2.Breed = "Siamese";
            pet2.OwnerID = client2Id;
            int pet2Id = (int) petDao.insert(pet2);

            Pet pet3 = new Pet();
            pet3.Name = "Bella";
            pet3.Species = "Dog";
            pet3.Breed = "Golden Retriever";
            pet3.OwnerID = client3Id;
            int pet3Id = (int) petDao.insert(pet3);

            // === SERVICES ===
            ServiceDao serviceDao = db.serviceDao();

            Service exam = new Service();
            exam.Name = "Pregled";
            exam.Description = "Opšti pregled životinje";
            exam.Price = 30.0;
            exam.DurationMinutes = 30;
            int examId = (int) serviceDao.insert(exam);

            Service vac = new Service();
            vac.Name = "Vakcinacija";
            vac.Description = "Vakcinacija protiv bolesti";
            vac.Price = 50.0;
            vac.DurationMinutes = 20;
            int vacId = (int) serviceDao.insert(vac);

            Service groom = new Service();
            groom.Name = "Šišanje";
            groom.Description = "Usluga šišanja pasa i mačaka";
            groom.Price = 40.0;
            groom.DurationMinutes = 60;
            int groomId = (int) serviceDao.insert(groom);

            // === APPOINTMENTS ===
            AppointmentDao appointmentDao = db.appointmentDao();

            Calendar cal = Calendar.getInstance();

            Appointment app1 = new Appointment();
            app1.PetID = pet1Id;
            app1.VetID = vet1Id;
            app1.ServiceID = examId;
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, 1);
            app1.Date = cal.getTime();
            int app1Id = (int) appointmentDao.insert(app1);

            Appointment app2 = new Appointment();
            app2.PetID = pet2Id;
            app2.VetID = vet2Id;
            app2.ServiceID = vacId;
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -2);
            app2.Date = cal.getTime();
            int app2Id = (int) appointmentDao.insert(app2);

            Appointment app3 = new Appointment();
            app3.PetID = pet3Id;
            app3.VetID = vet1Id;
            app3.ServiceID = groomId;
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -5);
            app3.Date = cal.getTime();
            int app3Id = (int) appointmentDao.insert(app3);

            // === PAYMENTS & UNPAID ===
            PaymentDao paymentDao = db.paymentDao();
            UnpaidServiceDao unpaidDao = db.unpaidServiceDao();

            Payment p1 = new Payment();
            p1.UserID = client2Id;
            p1.AppointmentID = app2Id;
            p1.Amount = 50.0;
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -2);
            p1.Date = cal.getTime();
            paymentDao.insert(p1);

            UnpaidService u1 = new UnpaidService();
            u1.UserID = client3Id;
            u1.AppointmentID = app3Id;
            u1.Amount = 40.0;
            u1.status = UnpaidService.Status.pending;
            unpaidDao.insert(u1);

            UnpaidService u2 = new UnpaidService();
            u2.UserID = client1Id;
            u2.AppointmentID = app1Id;
            u2.Amount = 30.0;
            u2.status = UnpaidService.Status.pending;
            unpaidDao.insert(u2);

            // === MEDICAL RECORDS ===
            MedicalRecordDao recordDao = db.medicalRecordDao();

            MedicalRecord r1 = new MedicalRecord();
            r1.AppointmentID = app2Id;
            r1.Diagnosis = "Vakcinacija uspješna";
            r1.Treatment = "Antirabies vaccine";
            r1.Notes = "Nema komplikacija";
            recordDao.insert(r1);

            MedicalRecord r2 = new MedicalRecord();
            r2.AppointmentID = app3Id;
            r2.Diagnosis = "Pregled završen";
            r2.Treatment = "Tablete protiv parazita";
            r2.Notes = "Potrebna kontrola za 2 sedmice";
            recordDao.insert(r2);

            Log.i("DatabaseSeeder", "Database successfully seeded!");
        } catch (Exception e) {
            Log.e("DatabaseSeeder", "Error seeding database", e);
        }
    }
}
