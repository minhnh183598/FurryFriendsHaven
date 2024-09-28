package org.demo.huyminh.Controller;


import jakarta.validation.Valid;

import org.demo.huyminh.DTO.Request.PetCreationRequest;
import org.demo.huyminh.DTO.Request.PetUpdateRequest;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@CrossOrigin("http://localhost:3000")
public class PetController {
    @Autowired
    private PetService petService;


    @PostMapping
    Pet createPets(@RequestBody @Valid PetCreationRequest request){
         return petService.createPet(request);
    }
    @GetMapping
    List<Pet> getPets(){
         return petService.getPets();
    }

    //GET PET BY ID
    @GetMapping("/{petId}")
    Pet getPet(@PathVariable("petId") String petId){
        return petService.getPet(petId);
    }

    //UPDATE PET
    @PutMapping("/{petId}")
    Pet updatePet(@PathVariable("petId") String petId ,@RequestBody PetUpdateRequest request){
        return petService.updatePet(petId, request);
    }
    //DELETE PET
    @DeleteMapping("/{petId}")
    String deletePet(@PathVariable("petId") String petId){
        petService.deletePet(petId);
        return "Pet has been deleted";
    }

    //Search Pets By Many Fields
    @GetMapping("/SearchPets")
    public List<Pet> searchPets(
            @RequestParam(defaultValue = "All") String petType,
            @RequestParam(defaultValue = "All") String petAge,
            @RequestParam(defaultValue = "All") String petGender,
            @RequestParam(defaultValue = "All") String petColor,
            @RequestParam(defaultValue = "All") String petVaccin,
            @RequestParam(defaultValue = "All") String petStatus,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) String sort) {
            return petService.searchPets(petType, petAge, petGender, petColor, petVaccin,petStatus, keyword, sort);
        }
    //Sort 6 pets
    @GetMapping("/sort6Pets")
    public List<Pet> sort6Pets(){
        return petService.sort6pets();
    }

//    // Search By Name
//        @GetMapping("/searchName")
//    public ResponseEntity<List<Pet>> searchPets(@RequestParam String keyword){
//        List<Pet> pets = petService.searchByPetName(keyword);
//        return new ResponseEntity<>(pets, HttpStatus.OK);
//        }
//    //Search v2
//    @GetMapping("/search")
//    public ResponseEntity<List<Pet>> searchPets(
//            @RequestParam(required = false) String petType,
//            @RequestParam(required = false) String petGender,
//            @RequestParam(required = false) String petAge,
//            @RequestParam(required = false) String petColor,
//            @RequestParam(required = false) String petStatus,
//            @RequestParam(required = false) String petVaccin) {

//        List<Pet> pets = petService.searchPets(petType, petGender, petAge, petColor, petStatus, petVaccin);
//        return new ResponseEntity<>(pets, HttpStatus.OK);
//    }
//    //Sort Pets by Name
//    @GetMapping("/sortByName")
//    public List<Pet> sortByName(){
//        return petService.sortPetByName();
//    }
//    //Sort Pets by Weight
//    @GetMapping("/sortByWeight")
//    public List<Pet> sortByWeight(){
//        return petService.sortPetByWeight();
//    }
//    //Sort Pets by Age
//    @GetMapping("/sortByAge")
//    public List<Pet> sortByAge(){
//        return petService.sortPetByAge();
//    }
//    @GetMapping("/adoptedPets")
//    public List<Pet> adoptedPets(){
//        return petService.adoptedPet();
//    }

//     //availablePets
//     @GetMapping("/availablePets")
//     public List<Pet> availablePets(){
//        return petService.availablePet();
//}

}
