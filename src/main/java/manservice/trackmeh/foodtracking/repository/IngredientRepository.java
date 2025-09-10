package manservice.trackmeh.foodtracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manservice.trackmeh.foodtracking.entity.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {

}
