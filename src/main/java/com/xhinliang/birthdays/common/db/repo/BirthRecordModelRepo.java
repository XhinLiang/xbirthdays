package com.xhinliang.birthdays.common.db.repo;

import com.xhinliang.birthdays.common.db.model.BirthRecordModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author xhinliang
 */
public interface BirthRecordModelRepo extends JpaRepository<BirthRecordModel, Long> {

    @Nonnull
    List<BirthRecordModel> findAllById(@Nonnull Iterable<Long> iterable);

    @Nonnull
    Page<BirthRecordModel> findAllByCreatorUserId(Long userId, Pageable pageable);

    @Nonnull
    List<BirthRecordModel> findAllByCreatorUserId(Long userId);
}
