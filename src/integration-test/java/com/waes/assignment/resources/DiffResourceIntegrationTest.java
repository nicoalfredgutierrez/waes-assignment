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
        final String hexData = "AAAAAAAAAAAAAAAAAAAAAAAAAA";
        RequestBinaryDataInsertion request =  createDataInsertionRequestFromHexString("AAAAAAAAAAAAAAAAAAAAAAAAAA");

        this.webClient.post().uri("/V1/diff/12/left")
                                .body(BodyInserters.fromObject(request))
                                .exchange().expectStatus().isCreated();

        theDiffRequestHasBeenSaved(diffId, DatatypeConverter.parseHexBinary(hexData),null);
    }

    @Test
    public void theRightSideOfDiffIsPersisted() {

        final Integer diffId = 12;
        final String hexData = "AAAAAAAAAAAAAAAAAAAAAAAAAA";
        RequestBinaryDataInsertion request =  createDataInsertionRequestFromHexString(hexData);

        this.webClient.post().uri("/V1/diff/12/right")
                .body(BodyInserters.fromObject(request))
                .exchange().expectStatus().isCreated();
        theDiffRequestHasBeenSaved(diffId, null, DatatypeConverter.parseHexBinary(hexData));
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

    @Test
    public void theDiffOverARequestWithDifferentSizesReturnsThat() {

        final Integer diffId = 12;
        givenThatExistsADiffRequestWithDataWithDifferentSizes(diffId);

        this.webClient.get().uri("/V1/diff/" + diffId)
                .exchange().expectStatus().isOk()
                .expectBody().json("{\n" +
                "    diffResult: \"DIFFERENT_SIZE\"\n" +
                "}");
    }

    @Test
    public void theDiffOverARequestWithSameSizeButDifferentDataReturnTheDiffPositions() {

        final Integer diffId = 12;
        givenThatExistsADiffRequestWithSameSizeButDifferentContent(diffId);

        this.webClient.get().uri("/V1/diff/" + diffId)
                .exchange().expectStatus().isOk()
                .expectBody().json("{\n" +
                                "    diffResult: \"DIFFERENT_CONTENT\",\n" +
                                "    differences:  [{\n" +
                                "                     offset: 0,\n" +
                                "                     length: 1\n" +
                                "                   },{\n" +
                                "                     offset: 6,\n" +
                                "                     length: 2\n" +
                                "                   },{\n" +
                                "                     offset: 11,\n" +
                                "                     length: 3\n" +
                                "                   }]\n" +
                                "}");
    }

    @Test
    public void theDiffOverAnUnexistingDiffRequestInformsResourceNotFound() {

        final Integer diffId = 12;

        this.webClient.get().uri("/V1/diff/" + diffId)
                .exchange().expectStatus().isNotFound()
                .expectBody().json("{\n" +
                "    detail: \"the diff with id " + diffId + " could not be found to execute\"\n" +
                "}");
    }

    @Test
    public void theDiffWithOnlyItsRightSideLoadedInformsInternalError() {

        final Integer diffId = 12;
        givenThatExistsADiffRequestWithTheRightSide(diffId);

        this.webClient.get().uri("/V1/diff/" + diffId)
                .exchange().expectStatus().is5xxServerError()
                .expectBody().json("{\n" +
                "    detail: \"the diff with id " + diffId + " is missing the left data\"\n" +
                "}");
    }


    private void givenThatExistsADiffRequestWithTheRightSide(Integer diffId) {

        RequestBinaryDataInsertion requestRightData = createDataInsertionRequestFromHexString("AAAAAAAAAAAAFFFFAAAAAAAAAADD");

        this.webClient.post().uri("/V1/diff/" + diffId + "/right")
                .body(BodyInserters.fromObject(requestRightData))
                .exchange();
    }

    private void givenThatExistsADiffRequestWithSameSizeButDifferentContent(Integer diffId) {

        RequestBinaryDataInsertion requestLeftData = createDataInsertionRequestFromHexString( "FFAAAAAAAAAAAAAAAAAAAAFFFFFF");
        RequestBinaryDataInsertion requestRightData = createDataInsertionRequestFromHexString("AAAAAAAAAAAAFFFFAAAAAAAAAADD");

        this.webClient.post().uri("/V1/diff/" + diffId + "/right")
                .body(BodyInserters.fromObject(requestLeftData))
                .exchange();

        this.webClient.post().uri("/V1/diff/" + diffId + "/left")
                .body(BodyInserters.fromObject(requestRightData))
                .exchange();
    }

    private void givenThatExistsADiffRequestWithDataWithDifferentSizes(Integer diffId) {

        RequestBinaryDataInsertion requestLeftData = createDataInsertionRequestFromHexString("AAAAAAAAAAAAAAAAAAAAAAAAAA");
        RequestBinaryDataInsertion requestRightData = createDataInsertionRequestFromHexString("AAAAAAAAAAAAAAAAAAAAAAAAAADD");

        this.webClient.post().uri("/V1/diff/" + diffId + "/right")
                .body(BodyInserters.fromObject(requestLeftData))
                .exchange();

        this.webClient.post().uri("/V1/diff/" + diffId + "/left")
                .body(BodyInserters.fromObject(requestRightData))
                .exchange();
    }

    private void givenThatExistsADiffRequestWithTheSameRightAndLeftDataAndId(Integer diffId) {

        RequestBinaryDataInsertion request =  createDataInsertionRequestFromHexString("AAAAAAAAAAAAAAAAAAAAAAAAAA");
        this.webClient.post().uri("/V1/diff/" + diffId + "/right")
                .body(BodyInserters.fromObject(request))
                .exchange();

        this.webClient.post().uri("/V1/diff/" + diffId + "/left")
                .body(BodyInserters.fromObject(request))
                .exchange();
    }

    private void theDiffRequestHasBeenSaved(Integer diffId, byte[] leftData, byte[] rightData) {

        Optional<DiffRequest> request = diffRequestRepository.findById(diffId);
        assertThat(request.isPresent()).isTrue().as("The diff request is not saved");
        assertThat(request.get().getLeftSideData()).as("The left data is incorrect").isEqualTo(leftData);
        assertThat(request.get().getRightSideData()).as("The right data is incorrect").isEqualTo(rightData);
    }

    private RequestBinaryDataInsertion createDataInsertionRequestFromHexString(String hexData) {

        byte[] decodedData = DatatypeConverter.parseHexBinary(hexData);
        final String base64Data = getBase64EncodedBody(decodedData);
        RequestBinaryDataInsertion requestLeftData =  new RequestBinaryDataInsertion();
        requestLeftData.setData(base64Data);
        return requestLeftData;
    }

    private String getBase64EncodedBody(byte[] data) {

        return Base64.getEncoder().encodeToString(data);
    }

}
