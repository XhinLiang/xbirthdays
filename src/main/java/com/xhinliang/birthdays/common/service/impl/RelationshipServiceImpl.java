package com.xhinliang.birthdays.common.service.impl;

import com.xhinliang.birthdays.common.db.constant.RelationshipType;
import com.xhinliang.birthdays.common.db.model.UserRelationshipModel;
import com.xhinliang.birthdays.common.db.repo.UserRelationshipModelRepo;
import com.xhinliang.birthdays.common.dto.PageItem;
import com.xhinliang.birthdays.common.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xhinliang
 */
@Service
@Lazy
public class RelationshipServiceImpl implements RelationshipService {

    private final UserRelationshipModelRepo userRelationshipModelRepo;

    @Autowired
    public RelationshipServiceImpl(UserRelationshipModelRepo userRelationshipModelRepo) {
        this.userRelationshipModelRepo = userRelationshipModelRepo;
    }

    public void follow(long userId, long friendId) {
        UserRelationshipModel relationship = userRelationshipModelRepo.findOne(userId, friendId);
        if (relationship == null) {
            relationship = new UserRelationshipModel(userId, friendId, RelationshipType.A_TO_B);
        }
        if (relationship.getStateEnum() == RelationshipType.B_TO_A) {
            relationship.setStateEnum(RelationshipType.DUPLEX);
        } else {
            relationship.setStateEnum(RelationshipType.A_TO_B);
        }
        userRelationshipModelRepo.save(relationship);
    }

    public void unfollow(long userId, long friendId) {
        UserRelationshipModel relationship = userRelationshipModelRepo.findOne(userId, friendId);
        if (relationship == null) {
            relationship = new UserRelationshipModel(userId, friendId, RelationshipType.NONE);
        }
        if (relationship.getStateEnum() == RelationshipType.A_TO_B) {
            relationship.setStateEnum(RelationshipType.NONE);
        }
        if (relationship.getStateEnum() == RelationshipType.DUPLEX) {
            relationship.setStateEnum(RelationshipType.B_TO_A);
        }
        userRelationshipModelRepo.save(relationship);
    }

    public PageItem<Long> listFollowing(long userId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserRelationshipModel> userRelationships = userRelationshipModelRepo.findUserRelationshipsByAUserIdOrBUserId(userId, userId, pageable);
        int currentPage = userRelationships.getNumber();
        boolean hasNextPage = userRelationships.hasNext();
        List<Long> items = userRelationships //
            .stream() //
            .filter(f -> f.getStateEnum() != RelationshipType.DUPLEX)
            .filter(f -> (f.getAUserId() == userId && f.getStateEnum() == RelationshipType.A_TO_B)
                || (f.getBUserId() == userId && f.getStateEnum() == RelationshipType.B_TO_A))
            .map(f -> f.getAUserId() == userId ? f.getBUserId() : f.getAUserId())
            .collect(Collectors.toList());
        return PageItem.of(currentPage, hasNextPage, items);
    }
}
