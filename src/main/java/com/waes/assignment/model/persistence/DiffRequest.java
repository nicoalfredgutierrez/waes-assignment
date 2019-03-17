package com.waes.assignment.model.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class DiffRequest {

    private Integer id;
    private byte[] leftSideData;
    private byte[] rightSideData;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Lob
    public byte[] getLeftSideData() {
        return leftSideData;
    }

    public void setLeftSideData(byte[] leftSideData) {
        this.leftSideData = leftSideData;
    }

    @Lob
    public byte[] getRightSideData() {
        return rightSideData;
    }

    public void setRightSideData(byte[] rightSideData) {
        this.rightSideData = rightSideData;
    }

    public static class DiffExecution {
    }
}
