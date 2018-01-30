package ru.intabia.sso.demo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.intabia.sso.demo.backend.model.UserEntity;

/**
 * Dao to manage {@link UserEntity} entities.
 */
@Repository
public interface ClientRepository extends JpaRepository<UserEntity, Long> {
}
