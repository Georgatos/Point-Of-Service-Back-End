package dev.andreasgeorgatos.pointofservice.controller.user;

import dev.andreasgeorgatos.pointofservice.model.address.Address;
import dev.andreasgeorgatos.pointofservice.service.user.AddressService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createAddress(@Valid @RequestBody Address address, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }

        return addressService.createAddress(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editAddressById(@Valid @PathVariable Long id, @RequestBody Address address, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        return addressService.editAddressById(id, address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Address> deleteAddressById(@PathVariable Long id) {
        return addressService.deleteAddressById(id);
    }
}
