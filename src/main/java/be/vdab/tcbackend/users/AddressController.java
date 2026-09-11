package be.vdab.tcbackend.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("address")
class AddressController {

    private final AddressService addressService;

    AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /* DTOs */
    //NOTE: This can return user inside a { user.. }
    private record AddressDetails(
            long addressId,
            String street,
            String houseNumber,
            String bus,
            String city,
            String postalCode,
            String country,
            long userId) {
        AddressDetails(Address address) {
            this(address.getId(),
                    address.getStreet(),
                    address.getHouseNumber(),
                    address.getBus(),
                    address.getCity(),
                    address.getPostalCode(),
                    address.getCountry(),
                    address.getUser().getId()
            );
        }
    }

    // GET requests finds by id returns one address */
    // GET http://localhost:8080/addresses/{{id}}
    @GetMapping("{id}")
    AddressDetails findById(@PathVariable long id) {
        return addressService.findById(id)
                .map(AddressDetails::new)
                .orElseThrow(AddressNotFoundException::new);
    }

    // GET requests finds by userId returns one address */
    // GET http://localhost:8080/products/byUserId/{{id}}
    @GetMapping("/byuserid/{userId}")
    List<AddressDetails> findByUserId(@PathVariable long userId) {
        return addressService.findByUserId(userId)
                .stream()
                .map(AddressDetails::new)
                .toList();
    }

    /* POST request to create a new Address */
    // POST http://localhost:8080/addresses
    /* Thema 9: Toevoegen */
    @PostMapping("forUser/{userId}")
    long create(@PathVariable long userId, @RequestBody @Valid NewAddress newAddress) {
        return addressService.create(userId, newAddress);
    }

    /* Thema 10: Verwijderen */
    /* DELETE request to delete an user by Id */
    //DELETE http://localhost:8080/users/{{id}}
    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        try {
            addressService.delete(id);
        } catch (EmptyResultDataAccessException _) {
        }
    }

    @PutMapping("{id}")
    void update(@PathVariable long id,
                @RequestBody @Valid EditAddress editAddress) {
        addressService.update(id, editAddress);
    }
/*
PUT http://localhost:8080/address/2
Content-Type: application/json

{
  "street": "Peachtree Dunwoody Road",
  "houseNumber": "40",
  "bus": "2",
  "city": "Sandy Springs",
  "postalCode": "28056",
  "country": "United States"
}
 */





}
