package com.waes.assignment.service;

import com.waes.assignment.model.Diff;
import com.waes.assignment.model.DiffExecutionResult;
import com.waes.assignment.model.persistence.DiffRequest;
import com.waes.assignment.model.DifferenceDetail;
import com.waes.assignment.model.exceptions.ApiException;
import com.waes.assignment.model.exceptions.ResourceNotFoundException;
import com.waes.assignment.repositories.DiffRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Contains the logic to save and execute a dif of binary data.
 */
@Component
public class DiffService {

    private DiffRequestRepository diffRespository;

    @Autowired
    public void setDiffRepository(DiffRequestRepository diffRespository) {
        this.diffRespository = diffRespository;
    }


    /**
     * Saves the left side ot the diff request with the given id. If the diff request exists then it is updated,
     * otherwise it is created.
     * @param diffId The id to track the diff
     * @param data the binary data to be saved in the left side
     */
    public void saveLeftSideOfADiff(Integer diffId, byte[] data) {

        DiffRequest diffRequest = prepareDiffRequestForPersistence(diffId);
        diffRequest.setLeftSideData(data);
        diffRespository.save(diffRequest);
    }

    /**
     * Saves the right side ot the diff request with the given id. If the diff request exists then it is updated,
     * otherwise it is created.
     * @param diffId The id to track the diff
     * @param data the binary data to be saved in the right side
     */
    public void saveRightSideOfADiff(Integer diffId, byte[] data) {

        DiffRequest diffRequest = prepareDiffRequestForPersistence(diffId);
        diffRequest.setRightSideData(data);
        diffRespository.save(diffRequest);
    }

    /**
     * Executes a diff previously stored with a given id.
     * @param diffId The id of the request to be stored
     * @return The diff result.
     * @throws ResourceNotFoundException When the diff execution is attempted on an non existent diff.
     */
    public Diff executeDiff(Integer diffId) throws ResourceNotFoundException  {

        Optional<DiffRequest> diffRequest = diffRespository.findById(diffId);

        if(diffRequest.isPresent()) {

            checkIfDataIsFullyLoaded(diffRequest.get());
            return executeDiff(diffRequest.get());
        } else {

            throw new ResourceNotFoundException("the diff with id " + diffId + " could not be found to execute");
        }
    }

    private void checkIfDataIsFullyLoaded(DiffRequest diffRequest) {

        if(diffRequest.getLeftSideData() == null) {

            throw new ApiException("the diff with id " + diffRequest.getId() + " is missing the left data");
        }

        if(diffRequest.getRightSideData() == null) {

            throw new ApiException("the diff with id " + diffRequest.getId() + " is missing the right data");
        }
    }

    private Diff executeDiff(DiffRequest diffRequest) {

        Diff diff = new Diff();

        if(Arrays.equals(diffRequest.getLeftSideData(), diffRequest.getRightSideData())) {

            diff.setDiffResult(DiffExecutionResult.EQUALS);
        } else if (diffRequest.getLeftSideData().length != diffRequest.getRightSideData().length){

            diff.setDiffResult(DiffExecutionResult.DIFFERENT_SIZE);
        } else {

            diff.setDiffResult(DiffExecutionResult.DIFFERENT_CONTENT);
            List<DifferenceDetail> differences = detectDifferences(diffRequest);
            diff.setDifferences(differences);
        }
        return diff;
    }

    private List<DifferenceDetail> detectDifferences(DiffRequest diffRequest) {

        byte[] leftSideData = diffRequest.getLeftSideData();
        byte[] rightSideData = diffRequest.getRightSideData();
        DifferenceDetail currentDetail = null;
        List<DifferenceDetail> differences = new ArrayList<>();

        for(int i=0; i< leftSideData.length; i++) {

            if(leftSideData[i] != rightSideData[i]) {

                if(currentDetail == null) {

                    currentDetail = new DifferenceDetail();
                    currentDetail.setOffset(i);
                    differences.add(currentDetail);
                }
                currentDetail.addDifference();
            } else if (currentDetail != null) {

                currentDetail = null;
            }
        }

        return differences;
    }

    private DiffRequest prepareDiffRequestForPersistence(Integer diffId) {

        Optional<DiffRequest> dbDiffRequest = diffRespository.findById(diffId);
        DiffRequest diffRequest;
        if(dbDiffRequest.isPresent()) {

            diffRequest = dbDiffRequest.get();
        } else {

            diffRequest = new DiffRequest();
            diffRequest.setId(diffId);
        }

        return diffRequest;
    }

}
