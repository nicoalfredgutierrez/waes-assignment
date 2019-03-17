package com.waes.assignment.service;

import com.waes.assignment.model.Diff;
import com.waes.assignment.model.DiffExecutionResult;
import com.waes.assignment.model.DiffRequest;
import com.waes.assignment.repositories.DiffRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class DiffService {

    private DiffRequestRepository diffRespository;

    @Autowired
    public void setDiffRepository(DiffRequestRepository diffRespository) {
        this.diffRespository = diffRespository;
    }

    public void saveLeftSideOfADiff(Integer diffId, byte[] data) {

        DiffRequest diffRequest = prepareDiffRequestForPersistence(diffId);
        diffRequest.setLeftSideData(data);
        diffRespository.save(diffRequest);
    }

    public void saveRightSideOfADiff(Integer diffId, byte[] data) {

        DiffRequest diffRequest = prepareDiffRequestForPersistence(diffId);
        diffRequest.setRightSideData(data);
        diffRespository.save(diffRequest);
    }

    public Diff executeDiff(Integer diffId) {

        DiffRequest diffRequest = diffRespository.findById(diffId).get();
        Diff diff = new Diff();

        if(Arrays.equals(diffRequest.getLeftSideData(),diffRequest.getRightSideData())) {

            diff.setDiffResult(DiffExecutionResult.EQUALS);
        } else {

            diff.setDiffResult(DiffExecutionResult.DIFFERENT_SIZE);
        }
        return diff;
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
