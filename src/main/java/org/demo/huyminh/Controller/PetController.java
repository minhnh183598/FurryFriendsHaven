package org.demo.huyminh.Controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Request.PetCreationRequest;
import org.demo.huyminh.DTO.Request.PetUpdateRequest;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/pets")
@CrossOrigin("http://localhost:3000")
public class PetController {
    @Autowired
    private PetService petService;

    @PostMapping
    Pet createPets(@RequestBody @Valid PetCreationRequest request) {
        return petService.createPet(request);
    }

    @GetMapping
    List<Pet> getPets() {
        return petService.getPets();
    }

    //GET PET BY ID
    @GetMapping("/{petId}")
    Pet getPet(@PathVariable("petId") String petId) {
        return petService.getPet(petId);
    }

    //UPDATE PET
    @PutMapping("/{petId}")
    Pet updatePet(@PathVariable("petId") String petId, @RequestBody PetUpdateRequest request) {
        return petService.updatePet(petId, request);
    }


    //UPDATE PET STATUS
    @PutMapping("status/{petId}")
    Pet updatePetStatus(@PathVariable("petId") String petId, @RequestBody PetUpdateRequest request) {
        return petService.updatePetStatus(petId, request);
    }

    //DELETE PET
    @DeleteMapping("/{petId}")
    String deletePet(@PathVariable("petId") String petId) {
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
            @RequestParam(defaultValue = "sortByDate") String sort) {
        return petService.searchPets(petType, petAge, petGender, petColor, petVaccin, petStatus, keyword, sort);
    }

    //Sort 6 pets
    @GetMapping("/sort6Pets")
    public List<Pet> sort6Pets() {
        return petService.sort6pets();
    }
}


//    // Search By Name
//        @GetMapping("/searchName")
//    public ResponseEntity<List<Pet>> searchPets(@RequestParam String keyword){
//        List<Pet> pets = petService.searchByPetName(keyword);
//        return new ResponseEntity<>(pets, HttpStatus.OK);
//        }
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

