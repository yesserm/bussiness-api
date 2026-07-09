package dev.yesserm.demosb4.userservice.repository;

import dev.yesserm.demosb4.userservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query(
            value = """
                    SELECT DISTINCT u
                    FROM User u
                    LEFT JOIN u.roles r
                    WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
                    AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
                    AND (:role IS NULL OR r.name = :role)
                    AND (:active IS NULL OR u.active = :active)
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT u)
                    FROM User u
                    LEFT JOIN u.roles r
                    WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
                    AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
                    AND (:role IS NULL OR r.name = :role)
                    AND (:active IS NULL OR u.active = :active)
                    """
    )
    Page<User> findFiltered(
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") String role,
            @Param("active") Boolean active,
            Pageable pageable
    );

    @Query(
            value = """
                    SELECT DISTINCT u
                    FROM User u
                    LEFT JOIN u.roles r
                    WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
                    AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
                    AND (:rolesEmpty = TRUE OR r.name IN :roles)
                    AND (:active IS NULL OR u.active = :active)
                    AND (:createdFrom IS NULL OR u.createdAt >= :createdFrom)
                    AND (:createdTo IS NULL OR u.createdAt <= :createdTo)
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT u)
                    FROM User u
                    LEFT JOIN u.roles r
                    WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
                    AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
                    AND (:rolesEmpty = TRUE OR r.name IN :roles)
                    AND (:active IS NULL OR u.active = :active)
                    AND (:createdFrom IS NULL OR u.createdAt >= :createdFrom)
                    AND (:createdTo IS NULL OR u.createdAt <= :createdTo)
                    """
    )
    Page<User> search(
            @Param("name") String name,
            @Param("email") String email,
            @Param("roles") Collection<String> roles,
            @Param("rolesEmpty") boolean rolesEmpty,
            @Param("active") Boolean active,
            @Param("createdFrom") Instant createdFrom,
            @Param("createdTo") Instant createdTo,
            Pageable pageable
    );
}
