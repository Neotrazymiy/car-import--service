package spring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.model.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, UUID>, CodeRepository<Make> {

	Optional<Make> findByName(String name);

	Page<Make> findAll(Pageable pageable);

}
