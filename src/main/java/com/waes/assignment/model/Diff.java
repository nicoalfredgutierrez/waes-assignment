package com.waes.assignment.model;

import javax.persistence.Entity;

@Entity
public class Diff {

    private Integer id;
    private byte[] leftSideData;
    private byte[] rightSideData;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public byte[] getLeftSideData() {
        return leftSideData;
    }

    public void setLeftSideData(byte[] leftSideData) {
        this.leftSideData = leftSideData;
    }

    public byte[] getRightSideData() {
        return rightSideData;
    }

    public void setRightSideData(byte[] rightSideData) {
        this.rightSideData = rightSideData;
    }
}
