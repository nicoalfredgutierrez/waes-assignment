package com.waes.assignment.model;

import java.util.List;

public class Diff {

    private DiffExecutionResult diffResult;
    private List<DifferenceDetail> differences;

    public DiffExecutionResult getDiffResult() {
        return diffResult;
    }

    public void setDiffResult(DiffExecutionResult diffResult) {
        this.diffResult = diffResult;
    }

    public List<DifferenceDetail> getDifferences() {
        return differences;
    }

    public void setDifferences(List<DifferenceDetail> differences) {
        this.differences = differences;
    }
}
