package com.waes.assignment.model;

public class DifferenceDetail {

    private Integer offset;
    private Integer length = 0;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void addDifference() {

        this.length++;
    }
}
