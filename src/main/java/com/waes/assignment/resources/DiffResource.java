package com.waes.assignment.resources;

import com.waes.assignment.model.Diff;
import com.waes.assignment.model.RequestBinaryDataInsertion;
import com.waes.assignment.service.DiffService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Base64;


@RestController
public class DiffResource {

    private DiffService diffService;

    @PostMapping("/V1/diff/{id}/left")
    @ApiOperation(value = "Allows to save the left side of a diff. If the diff is new, it will be created." +
            "If the left side has already been stored then it will be overwritten.")

    public ResponseEntity<Integer> createLeftSideOfADiff(@PathVariable @ApiParam("The id of the diff") Integer id,
                                                         @RequestBody @ApiParam("The binary data to be stored as a side of a diff. It has to be a valid base64 encoded data")
                                                                 RequestBinaryDataInsertion request) {

        byte[] decodeData = Base64.getDecoder().decode(request.getData());
        diffService.saveLeftSideOfADiff(id, decodeData);
        return ResponseEntity.created(getDiffUrl(id)).build();
    }

    @PostMapping("/V1/diff/{id}/right")
    @ApiOperation(value = "Allows to save the right side of a diff. If the diff is new, it will be created." +
            "If the rights side has already been stored before then it will be overwritten.")
    public ResponseEntity<Integer> createRightSideOfADiff(@PathVariable @ApiParam("The id of the diff") Integer id,
                                                          @RequestBody @ApiParam("The binary data to be stored as a side of a diff. It has to be a valid base64 encoded data")
                                                                RequestBinaryDataInsertion request) {

        byte[] decodeData = Base64.getDecoder().decode(request.getData());
        diffService.saveRightSideOfADiff(id, decodeData);
        return ResponseEntity.created(getDiffUrl(id)).build();
    }

    @GetMapping(value = "/V1/diff/{id}", produces = MediaType. APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Executes a diff and returns the result of it.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The diff has been succesfully executed."),
                            @ApiResponse(code = 404, message = "The execution was attempted on an non existing diff."),
                            @ApiResponse(code = 500, message = "The execution was attempted on an incomplete diff request.")})
    public ResponseEntity<Diff> getDiff(@PathVariable Integer id) {

        Diff diff = diffService.executeDiff(id);
        return ResponseEntity.ok(diff);
    }

    private URI getDiffUrl(Integer id) {

        return new UriTemplate("/V1.0/diff").expand(id);
    }

    /**
     * Used only for dependecy injection
     * @param diffService
     */
    @Autowired
    public void setDiffService(DiffService diffService) {
        this.diffService = diffService;
    }
}
