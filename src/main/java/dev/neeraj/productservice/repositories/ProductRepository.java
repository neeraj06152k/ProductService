package dev.neeraj.productservice.repositories;

import dev.neeraj.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product save(Product product);
    Product findById(long id);
    List<Product> findAll();

    @Query(value = "select p.* from Product p limit ?1", nativeQuery = true)
    List<Product> findLimitedProducts(int limit);
    void deleteAll();
}
