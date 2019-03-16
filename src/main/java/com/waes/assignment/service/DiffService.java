package com.waes.assignment.service;

import com.waes.assignment.model.DiffRequest;
import com.waes.assignment.repositories.DiffRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiffService {

    private DiffRequestRepository diffRequestRespository;

    @Autowired
    public void setDiffRepository(DiffRequestRepository diffRespository) {
        this.diffRequestRespository = diffRespository;
    }

    public void saveLeftSideOfADiff(Integer diffId, byte[] data) {

        DiffRequest diff = diffRequestRespository.getOne(diffId);
        if(diff == null) {

            diff = new DiffRequest();
        }
        diff.setId(diffId);
        diff.setLeftSideData(data);
        diffRequestRespository.save(diff);
    }
}
