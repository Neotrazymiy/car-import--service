package spring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import spring.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID>, CodeRepository<Car>, JpaSpecificationExecutor<Car> {

	Optional<Car> findByModel(String model);

	Page<Car> findAll(Pageable pageable);

}
