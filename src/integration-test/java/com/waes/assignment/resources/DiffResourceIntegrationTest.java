package com.waes.assignment.resources;

import static org.assertj.core.api.Assertions.*;
import com.waes.assignment.model.DiffRequest;
import com.waes.assignment.model.RequestBinaryDataInsertion;
import com.waes.assignment.repositories.DiffRequestRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiffResourceIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private DiffRequestRepository diffRequestRepository;

    @After
    public void cleanData() {

        diffRequestRepository.deleteAll();
    }

    @Test
    public void theLeftSideOfADiffIsSaved() {

        final Integer diffId = 12;
        byte[] decodedData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAAAAAAAAAAAAAAAA");
        final String base64Data = getBase64EncodedBody(decodedData);
        RequestBinaryDataInsertion request =  new RequestBinaryDataInsertion();
        request.setData(base64Data);

        this.webClient.post().uri("/V1/diff/12/left")
                                .body(BodyInserters.fromObject(request))
                                .exchange().expectStatus().isCreated();

        theDiffRequestHasBeenSaved(diffId, decodedData,null);
    }

    @Test
    public void theRightSideOfDiffIsPersisted() {

        final Integer diffId = 12;
        byte[] decodedData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAAAAAAAAAAAAAAAA");
        final String base64Data = getBase64EncodedBody(decodedData);
        RequestBinaryDataInsertion request =  new RequestBinaryDataInsertion();
        request.setData(base64Data);

        this.webClient.post().uri("/V1/diff/12/right")
                .body(BodyInserters.fromObject(request))
                .exchange().expectStatus().isCreated();
        theDiffRequestHasBeenSaved(diffId, null, decodedData);
    }

    @Test
    public void theDiffOverARequestWithTheSameDataOnBothSidesReturnsThat() {

        final Integer diffId = 12;
        givenThatExistsADiffRequestWithTheSameRightAndLeftDataAndId(diffId);

        this.webClient.get().uri("/V1/diff/" + diffId)
                .exchange().expectStatus().isOk()
                .expectBody().json("{\n" +
                                    "    diffResult: \"EQUALS\"\n" +
                                    "}");
    }

    private void givenThatExistsADiffRequestWithTheSameRightAndLeftDataAndId(Integer diffId) {

        byte[] decodedData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAAAAAAAAAAAAAAAA");
        final String base64Data = getBase64EncodedBody(decodedData);

        RequestBinaryDataInsertion request =  new RequestBinaryDataInsertion();
        request.setData(base64Data);

        this.webClient.post().uri("/V1/diff/12/right")
                .body(BodyInserters.fromObject(request))
                .exchange();

        this.webClient.post().uri("/V1/diff/12/left")
                .body(BodyInserters.fromObject(request))
                .exchange();
    }

    private void theDiffRequestHasBeenSaved(Integer diffId, byte[] leftData, byte[] rightData) {

        Optional<DiffRequest> request = diffRequestRepository.findById(diffId);
        assertThat(request.isPresent()).isTrue().as("The diff request is not saved");
        assertThat(request.get().getLeftSideData()).isEqualTo(leftData).as("The left data is incorrect");
        assertThat(request.get().getRightSideData()).isEqualTo(rightData).as("The right data is incorrect");
    }

    private String getBase64EncodedBody(byte[] data) {

        String encodedString = Base64.getEncoder().encodeToString(data);
        return encodedString;
    }

}
