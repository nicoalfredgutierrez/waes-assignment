package com.waes.assignment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Represents the diff execution result. In case that both sides have the same size but different content, the differences will be listed
 * in differences, otherwise differences will be null
 */
@ApiModel("The diff execution result.")
public class Diff {

    private DiffExecutionResult diffResult;
    private List<DifferenceDetail> differences;

    @ApiModelProperty("The result obtained")
    public DiffExecutionResult getDiffResult() {
        return diffResult;
    }

    public void setDiffResult(DiffExecutionResult diffResult) {
        this.diffResult = diffResult;
    }

    @ApiModelProperty("The detail of the differences detected in the diff. Only present when both sides have the same size" +
            " but different content")
    public List<DifferenceDetail> getDifferences() {
        return differences;
    }

    public void setDifferences(List<DifferenceDetail> differences) {
        this.differences = differences;
    }
}
