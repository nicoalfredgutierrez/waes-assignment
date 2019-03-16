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

        Optional<DiffRequest> dbDiffRequest = diffRespository.findById(diffId);
        DiffRequest diffRequest;
        if(dbDiffRequest.isPresent()) {

            diffRequest = dbDiffRequest.get();
        } else {

            diffRequest = new DiffRequest();
        }
        diffRequest.setId(diffId);
        diffRequest.setLeftSideData(data);
        diffRespository.save(diffRequest);
    }
}
