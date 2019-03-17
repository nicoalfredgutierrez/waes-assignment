package com.waes.assignment;

import static org.assertj.core.api.Assertions.*;
import com.waes.assignment.model.exceptions.ApiError;
import com.waes.assignment.model.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiExceptionHandlerTest {

    private ApiExceptionHandler instance;

    @Before
    public void initialize() {

        instance = new ApiExceptionHandler();
    }

    @Test
    public void aResourceNotFoundExceptionIsHandled() {

        final String message = "REsource x not found";
        ResourceNotFoundException resourceNotFound  = new ResourceNotFoundException(message);

        ResponseEntity<ApiError> response = instance.handleResourceNotFoundException(resourceNotFound);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getDetail()).isEqualTo(message);
    }

}
