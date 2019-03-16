package com.waes.assignment.resources;

import com.waes.assignment.model.RequestBinaryDataInsertion;
import com.waes.assignment.service.DiffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Base64;


@RestController
public class DiffResource {

    private DiffService diffService;

    @PostMapping("/V1/diff/{id}/left")
    public ResponseEntity<Integer> createLeftSideOfADiff(@PathVariable Integer id,
                                                     @RequestBody RequestBinaryDataInsertion request) {

        byte[] decodeData = Base64.getDecoder().decode(request.getData());
        diffService.saveLeftSideOfADiff(id, decodeData);
        return ResponseEntity.created(getDiffUrl(id)).build();
    }

    @PostMapping("/V1/diff/{id}/right")
    public ResponseEntity<Integer> createRightSideOfADiff(@PathVariable Integer id,
                                                     @RequestBody RequestBinaryDataInsertion request) {

        byte[] decodeData = Base64.getDecoder().decode(request.getData());
        return ResponseEntity.created(getDiffUrl(id)).build();
    }

    private URI getDiffUrl(Integer id) {

        return new UriTemplate("/V1.0/diff").expand(id);
    }

    @Autowired
    public void setDiffService(DiffService diffService) {
        this.diffService = diffService;
    }
}
