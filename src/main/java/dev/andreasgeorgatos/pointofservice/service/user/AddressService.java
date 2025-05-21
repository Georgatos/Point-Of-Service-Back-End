package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.model.address.Address;
import dev.andreasgeorgatos.pointofservice.repository.users.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class that handles operations related to addresses.
 */
@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    /**
     * Constructs an AddressService with the specified repository.
     *
     * @param addressRepository The repository used for address data operations
     */
    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * Retrieves all addresses from the database.
     *
     * @return ResponseEntity containing a list of all addresses or NOT_FOUND if none exist
     */
    @Transactional(readOnly = true)
    public ResponseEntity<List<Address>> getAllAddresses() {
        logger.debug("Retrieving all addresses");
        List<Address> addresses = addressRepository.findAll();

        if (addresses.isEmpty()) {
            logger.info("No addresses found");
            return ResponseEntity.notFound().build();
        }

        logger.debug("Found {} addresses", addresses.size());
        return ResponseEntity.ok(addresses);
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param id The unique identifier of the address
     * @return ResponseEntity containing the address if found, or NOT_FOUND if none exists with the given ID
     */
    @Transactional(readOnly = true)
    public ResponseEntity<Address> getAddressById(long id) {
        logger.debug("Retrieving address with ID: {}", id);

        Optional<Address> address = addressRepository.findById(id);

        if (address.isEmpty()) {
            logger.info("Address not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        logger.debug("Address found: {}", address.get());
        return ResponseEntity.ok(address.get());
    }

    /**
     * Creates a new address.
     *
     * @param address The address to create
     * @return ResponseEntity containing the created address
     */
    @Transactional
    public ResponseEntity<Address> createAddress(Address address) {
        logger.debug("Creating new address: {}", address);

        Address savedAddress = addressRepository.save(address);

        logger.info("Address created with ID: {}", savedAddress.getId());
        return ResponseEntity.ok(savedAddress);
    }

    /**
     * Updates an existing address by its ID.
     *
     * @param id The unique identifier of the address to update
     * @param address The updated address data
     * @return ResponseEntity containing the updated address or NOT_FOUND if none exists with the given ID
     */
    @Transactional
    public ResponseEntity<Address> editAddressById(long id, Address address) {
        logger.debug("Updating address with ID: {}", id);

        Optional<Address> optionalAddress = addressRepository.findById(id);

        if (optionalAddress.isEmpty()) {
            logger.info("Address not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        Address existingAddress = optionalAddress.get();

        existingAddress.setStreet(address.getStreet());
        existingAddress.setNumber(address.getNumber());
        existingAddress.setCity(address.getCity());
        existingAddress.setPostalCode(address.getPostalCode());
        existingAddress.setDoorRingBellName(address.getDoorRingBellName());
        existingAddress.setCountry(address.getCountry());
        existingAddress.setStoryLevel(address.getStoryLevel());

        Address updatedAddress = addressRepository.save(existingAddress);

        logger.info("Address updated with ID: {}", updatedAddress.getId());
        return ResponseEntity.ok(updatedAddress);
    }

    /**
     * Deletes an address by its ID.
     *
     * @param id The unique identifier of the address to delete
     * @return ResponseEntity with no content if deletion was successful, or NOT_FOUND if no address exists with the given ID
     */
    @Transactional
    public ResponseEntity<Void> deleteAddressById(long id) { // Changed return type to ResponseEntity<Void>
        logger.debug("Deleting address with ID: {}", id);

        Optional<Address> address = addressRepository.findById(id);

        if (address.isEmpty()) {
            logger.info("Address not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        addressRepository.deleteById(id);

        logger.info("Address deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}