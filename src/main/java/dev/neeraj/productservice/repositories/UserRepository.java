package dev.neeraj.productservice.repositories;

import dev.neeraj.productservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
    User save(User user);
}
