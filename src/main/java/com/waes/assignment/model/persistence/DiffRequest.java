package com.waes.assignment.model.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;


/**
 * Entity used to persist a diff request. AS the diff left and right data are loaded at different times, the data can be
 * incomplete.
 */
@Entity
public class DiffRequest {

    private Integer id;
    private byte[] leftSideData;
    private byte[] rightSideData;

    /**
     * Gets the id of a diff.
     * @return the id of the diff
     */
    @Id
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the diff. Not autogenerated.
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the left side of the data to be diffed
     * @return The binary data as byte array
     */
    @Lob
    public byte[] getLeftSideData() {
        return leftSideData;
    }

    /**
     * Sets the left side of the data to be diffed
     * @param leftSideData The binary data as byte array
     */
    public void setLeftSideData(byte[] leftSideData) {
        this.leftSideData = leftSideData;
    }

    /**
     * Gets the right side of the data to be diffed
     * @return The binary data as byte array
     */
    @Lob
    public byte[] getRightSideData() {
        return rightSideData;
    }

    /**
     * Sets the right side of the data to be diffed
     * @param rightSideData The binary data as byte array
     */
    public void setRightSideData(byte[] rightSideData) {
        this.rightSideData = rightSideData;
    }
}
