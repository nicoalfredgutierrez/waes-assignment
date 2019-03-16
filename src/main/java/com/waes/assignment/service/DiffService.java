package com.waes.assignment.service;

import com.waes.assignment.model.Diff;
import com.waes.assignment.repositories.DiffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiffService {

    private DiffRepository diffRespository;

    @Autowired
    public void setDiffRepository(DiffRepository diffRespository) {
        this.diffRespository = diffRespository;
    }

    public void saveLeftSideOfADiff(Integer diffId, byte[] data) {

        Diff diff = diffRespository.getOne(diffId);
        if(diff == null) {

            diff = new Diff();
        }
        diff.setId(diffId);
        diff.setLeftSideData(data);
        diffRespository.save(diff);
    }
}
