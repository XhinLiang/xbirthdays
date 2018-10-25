package com.xhinliang.birthdays.common.db.repo;

import com.xhinliang.birthdays.common.db.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xhinliang
 */
public interface UserModelRepo extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findOneByEmail(String email);

    List<UserModel> findXInternalUsersByIdIn(Collection<Long> ids);

    @Modifying // 标识这个 SQL 会修改数据
    @Transactional // 标识这个 SQL 需要事务支持
    @Query(value = "update user_model set birth_time = :birthTime, birthday_type = :birthdayType where id = :userId", nativeQuery = true)
    void updateBirthday(@Param("userId") long userId, @Param("birthTime") long birthTime, @Param("birthdayType") int birthdayType);
}
