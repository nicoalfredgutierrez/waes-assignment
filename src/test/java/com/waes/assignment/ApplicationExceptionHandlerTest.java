package com.waes.assignment;

import static org.assertj.core.api.Assertions.*;
import com.waes.assignment.model.exceptions.ApiError;
import com.waes.assignment.model.exceptions.ApiException;
import com.waes.assignment.model.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler instance;

    @Before
    public void initialize() {

        instance = new ApplicationExceptionHandler();
    }

    @Test
    public void aResourceNotFoundExceptionIsHandled() {

        final String message = "REsource x not found";
        ResourceNotFoundException resourceNotFound  = new ResourceNotFoundException(message);

        ResponseEntity<ApiError> response = instance.handleResourceNotFoundException(resourceNotFound);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getDetail()).isEqualTo(message);
    }

    @Test
    public void aApiExceptionIsHandled() {

        final String message = "DAta is missing";
        ApiException apiException  = new ApiException(message);

        ResponseEntity<ApiError> response = instance.handleApiException(apiException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getDetail()).isEqualTo(message);
    }

}
