package com.waes.assignment.service;

import com.waes.assignment.model.DiffRequest;
import com.waes.assignment.repositories.DiffRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
