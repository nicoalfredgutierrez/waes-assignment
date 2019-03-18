package com.waes.assignment.model.exceptions;

/**
 * Class employed to communicate an error to an API consumer
 */
public class ApiError {

    private String detail;

    /**
     * Obtains the detail of the error
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Sets the detail of an error
     * @param detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
