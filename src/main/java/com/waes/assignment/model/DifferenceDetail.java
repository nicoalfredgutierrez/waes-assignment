package com.waes.assignment.model;

/**
 * Represents the detail of a single difference detected in a diff
 */
public class DifferenceDetail {

    private Integer offset;
    private Integer length = 0;

    /**
     * Obtains The offset where the difference starts in the array
     * @return The offset
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * Sets The offset where the difference starts in the array
     * @param offset
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * Obtains the length in positions of the difference
     * @return The Length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Sets The Length where the difference starts in the array
     * @param length
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * Increments the length of the difference
     */
    public void addDifference() {

        this.length++;
    }
}
