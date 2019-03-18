package com.waes.assignment.model;


/**
 * Used as a container of the binary data to be saved for a diff
 */
public class RequestBinaryDataInsertion {

    private String data;


    /**
     * Gets the base 64 representation of binary data
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the base 64 representation of binary data
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }
}
