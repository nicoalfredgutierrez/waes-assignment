package com.waes.assignment.resources;

import static org.assertj.core.api.Assertions.*;
import com.waes.assignment.model.DiffRequest;
import com.waes.assignment.model.RequestBinaryDataInsertion;
import com.waes.assignment.repositories.DiffRequestRepository;
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

    @Test
    public void theLeftSideOfADiffIsPersistedAsNew() {

        final Integer diffId = 12;
        byte[] originalData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAAAAAAAAAAAAAAAA");
        final String base64Data = getBase64EncodedBody(originalData);
        RequestBinaryDataInsertion request =  new RequestBinaryDataInsertion();
        request.setData(base64Data);

        this.webClient.post().uri("/V1/diff/12/left")
                                .body(BodyInserters.fromObject(request))
                                .exchange().expectStatus().isCreated();

        theDiffRequestHasBeenSaved(diffId, originalData);
    }

    private void theDiffRequestHasBeenSaved(Integer diffId, byte[] originalData) {

        Optional<DiffRequest> request = diffRequestRepository.findById(diffId);
        assertThat(request.isPresent()).isTrue();
        assertThat(request.get().getLeftSideData()).isEqualTo(originalData);
    }

    private String getBase64EncodedBody(byte[] data) {

        String encodedString = Base64.getEncoder().encodeToString(data);
        return encodedString;
    }

}
