package org.demo.huyminh.Repository;



import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, String> {

    //JPQL - JPA QUERRY LANGUAGE

    //Check PetName existed
    boolean existsByPetName(String petName);
    // Find pet_status
    List<Pet> findByPetStatus(String status);
    //Find pets by many fields
    @Query("SELECT p FROM Pet p WHERE " +
            "(:petType = 'All' OR p.petType = :petType) AND " +
            "(:petAge = 'All' OR p.petAge = :petAge) AND " +
            "(:petGender = 'All' OR p.petGender = :petGender) AND " +
            "(:petColor = 'All' OR p.petColor = :petColor) AND " +
            "(:petVaccin = 'All' OR p.petVaccin = :petVaccin) AND " +
            "(:petStatus = 'All' OR p.petStatus = :petStatus) AND " +
            "(:keyword = '' OR LOWER(p.petName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Pet> searchPets(@Param("petType") String petType,
                         @Param("petAge") String petAge,
                         @Param("petGender") String petGender,
                         @Param("petColor") String petColor,
                         @Param("petVaccin") String petVaccin,
                         @Param("petStatus") String petStatus,
                         @Param("keyword") String keyword);

    List<Pet> findAllByOrderByCreatedPetAtDesc();
}



//    //Search By Name
//    @Query("SELECT p FROM Pet p WHERE LOWER(p.petName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    List<Pet> searchByName(@Param("keyword") String keyword);
//
//    //Search By PetType(petType-petGender-petAge-petColor-petVaccin-petStatus)
//    @Query("SELECT p FROM Pet p WHERE " +
//            "(:petType = 'all' OR p.petType = :petType) AND " +
//            "(:petGender = 'all' OR p.petGender = :petGender) AND " +
//            "(:petAge = 'all' OR p.petAge = :petAge) AND " +
//            "(:petColor = 'all' OR p.petColor = :petColor) AND " +
//            "(:petStatus = 'all' OR p.petStatus = :petStatus) AND " +
//            "(:petVaccin = 'all' OR p.petVaccin = :petVaccin)")
//    List<Pet> searchPets(
//            @Param("petType") String petType,
//            @Param("petGender") String petGender,
//            @Param("petAge") String petAge,
//            @Param("petColor") String petColor,
//            @Param("petStatus") String petStatus,
//            @Param("petVaccin") String petVaccin);


//    //Sort Pets By Name (Alphabetical)
//    List<Pet> findAllByOrderByPetNameAsc();
//
//    //Sort Pets by Weight
//    List<Pet> findAllByOrderByPetWeight();
//
//    //Sort Pets by Age
//    @Query("SELECT p FROM Pet p ORDER BY " +
//            "CASE p.petAge " +
//            "WHEN 'Young' THEN 1 " +
//            "WHEN 'Full Grown' THEN 2 " +
//            "WHEN 'Old' THEN 3 " +
//            "END")
//    List<Pet> sortPetByAge();
