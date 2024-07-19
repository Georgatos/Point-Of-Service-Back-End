package dev.andreasgeorgatos.pointofservice.repository.users;

import dev.andreasgeorgatos.pointofservice.model.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
