package com.xhinliang.birthdays.common.db.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.xhinliang.birthdays.common.db.model.UserRelationshipModel;

/**
 * @author xhinliang
 */
public interface UserRelationshipModelRepo extends JpaRepository<UserRelationshipModel, Long> {

    UserRelationshipModel findUserRelationshipByAUserIdAndBUserId(Long aUserId, Long bUserId);

    List<UserRelationshipModel> findUserRelationshipsByAUserId(Long aUserId);

    Page<UserRelationshipModel> findUserRelationshipsByAUserIdOrBUserId(Long aUserId, Long bUserId, Pageable pageable);

    default UserRelationshipModel findOne(long userId, long anotherUserId) {
        if (userId < anotherUserId) {
            return this.findUserRelationshipByAUserIdAndBUserId(userId, anotherUserId);
        }
        return this.findUserRelationshipByAUserIdAndBUserId(anotherUserId, userId);
    }
}
