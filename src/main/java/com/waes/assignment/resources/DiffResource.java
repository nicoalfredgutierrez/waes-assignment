package com.waes.assignment.resources;

import com.waes.assignment.model.RequestBinaryDataInsertion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import java.net.URI;


@RestController
public class DiffResource {

    @PostMapping("/V1/diff/{id}/left")
    public ResponseEntity<Integer> createSideOfADiff(@PathVariable Integer id,
                                                     @RequestBody RequestBinaryDataInsertion request) {
        String algo = request.getData();
        return ResponseEntity.created(getDiffUrl(id)).build();
    }

    private URI getDiffUrl(Integer id) {

        return new UriTemplate("/V1.0/diff").expand(id);
    }
}
