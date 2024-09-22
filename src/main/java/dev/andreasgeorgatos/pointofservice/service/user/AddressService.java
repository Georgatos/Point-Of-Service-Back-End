package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.model.address.Address;
import dev.andreasgeorgatos.pointofservice.repository.users.AddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();

        if (!addresses.isEmpty()) {
            return ResponseEntity.ok(addresses);
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<Address> getAddressById(long id) {
        Optional<Address> address = addressRepository.findById(id);

        return address.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Address> createAddress(Address address) {
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @Transactional
    public ResponseEntity<Address> editAddressById(long id, Address address) {
        Optional<Address> optionalAddress = addressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            Address editedAddress = optionalAddress.get();

            editedAddress.setStreet(address.getStreet());
            editedAddress.setNumber(address.getNumber());
            editedAddress.setCity(address.getCity());
            editedAddress.setPostalCode(address.getPostalCode());
            editedAddress.setDoorRingBellName(address.getDoorRingBellName());
            editedAddress.setCountry(address.getCountry());
            editedAddress.setStoryLevel(address.getStoryLevel());

            addressRepository.save(editedAddress);

            return ResponseEntity.ok(editedAddress);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Address> deleteAddressById(long id) {
        Optional<Address> address = addressRepository.findById(id);

        if (address.isPresent()) {
            addressRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
