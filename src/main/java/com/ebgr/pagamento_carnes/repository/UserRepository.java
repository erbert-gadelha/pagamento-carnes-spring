package com.ebgr.pagamento_carnes.repository;

import com.ebgr.pagamento_carnes.model.UserModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserModel, Integer> {
    Optional<UserModel> findUserByLogin(String login);
    Optional<UserModel> findUserById(Integer id);
    List<UserModel> findAllByDeletedAtBefore(LocalDateTime dateTime);
    List<UserModel> findAll();

    @Query("SELECT u FROM UserModel u WHERE u.login = :login AND u.deletedAt IS NULL")
    Optional<UserModel> findActiveUserByLogin(@Param("login") String login);
    @Query("SELECT u FROM UserModel u WHERE u.deletedAt IS NULL")
    List<UserModel> findAllActiveUsers();
}