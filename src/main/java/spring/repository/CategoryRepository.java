package spring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>, CodeRepository<Category> {

	Optional<Category> findByName(String name);

}
