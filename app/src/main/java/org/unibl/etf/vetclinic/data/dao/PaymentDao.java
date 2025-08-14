package org.unibl.etf.vetclinic.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.Payment;

import java.util.Date;
import java.util.List;

@Dao
public interface PaymentDao {

    @Insert
    long insert(Payment payment);

    @Update
    void update(Payment payment);

    @Delete
    void delete(Payment payment);

    @Query("SELECT * FROM Payments WHERE UserID = :userId ORDER BY Date DESC")
    List<Payment> getPaymentsForUser(int userId);

    @Query("SELECT * FROM Payments WHERE Date >= :from AND Date <= :to ORDER BY Date DESC")
    List<Payment> getPaymentsInRange(Date from, Date to);

    @Query("SELECT * FROM Payments WHERE ID = :id LIMIT 1")
    Payment getPaymentById(int id);
}

