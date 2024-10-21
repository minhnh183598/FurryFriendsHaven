package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long>{

//    @Query("SELECT SUM(d.amount) FROM Donation d")
//    BigDecimal calculateTotalDonations();

//    // Phương thức để tìm các donations của user theo username
//    List<Donation> findByUser_Username(String username);
}
