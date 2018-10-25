package com.xhinliang.birthdays.common.db.model;

import com.xhinliang.birthdays.common.db.constant.ModelStatus;
import com.xhinliang.birthdays.common.db.constant.RelationshipType;
import lombok.Data;

import javax.persistence.*;

/**
 * @author xhinliang
 */
@Entity
@Data
@Table(name = "user_relationship_model", indexes = {
    @Index(name = "idx_a_id", columnList = "aUserId"),
    @Index(name = "idx_b_id", columnList = "bUserId"),
    @Index(name = "idx_model_status", columnList = "modelStatus"),
})
public class UserRelationshipModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long aUserId;

    @Column
    private long bUserId;

    @Column
    private int relationshipType;

    @Column
    private int modelStatus;

    public UserRelationshipModel() {
    }

    public UserRelationshipModel(long aUserId, long bUserId, RelationshipType relationshipType) {
        this.aUserId = aUserId;
        this.bUserId = bUserId;
        this.relationshipType = relationshipType.getValue();
    }

    public RelationshipType getStateEnum() {
        return RelationshipType.ofValue(this.relationshipType);
    }

    public void setStateEnum(RelationshipType relationshipType) {
        this.relationshipType = relationshipType.getValue();
    }

    public ModelStatus getModelStatusEnum() {
        return ModelStatus.getFromStatusValue(this.modelStatus);
    }

    public void setModelStatusEnum(ModelStatus modelStatusEnum) {
        this.modelStatus = modelStatusEnum.getValue();
    }
}
