package com.waes.assignment.resources;


import com.waes.assignment.model.RequestBinaryDataInsertion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Base64;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiffResourceTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void theLeftSideOfADiffIsPersistedAsNew() {

        final Integer diffId = 12;
        final String base64Data = getBase64EncodedBody();
        RequestBinaryDataInsertion request =  new RequestBinaryDataInsertion();
        request.setData(base64Data);

        this.webClient.post().uri("/V1/diff/12/left")
                                .body(BodyInserters.fromObject(request))
                                .exchange().expectStatus().isCreated();


    }

    private String getBase64EncodedBody() {

        String originalInput = "test input";
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        return encodedString;
    }

}
