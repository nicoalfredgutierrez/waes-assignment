package com.waes.assignment.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.waes.assignment.model.Diff;
import com.waes.assignment.model.DiffExecutionResult;
import com.waes.assignment.model.DiffRequest;
import com.waes.assignment.repositories.DiffRequestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import javax.xml.bind.DatatypeConverter;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

    private DiffService instance;
    private DiffRequestRepository respository;

    @Captor
    private ArgumentCaptor<DiffRequest> diffRequestCaptor;

    @Before
    public void initialize() {

        instance = new DiffService();
        respository = mock(DiffRequestRepository.class);
        instance.setDiffRepository(respository);
    }

    @Test
    public void theLeftSideOfAnUnexistingDiffIsSavedSoTheDiffRequestIsCreated() {

        final Integer diffId = 13;
        final byte[] data = {(byte)0x80, 0x53, 0x1c,
                (byte)0x87, (byte)0xa0, 0x42, 0x69, 0x10, (byte)0xa2, (byte)0xea, 0x08,
                0x00, 0x2b, 0x30, 0x30, (byte)0x9d };

        instance.saveLeftSideOfADiff(diffId, data);

        thenTheDiffRequestHasBeenPersistedFromScratchWith(diffId, data, null);
    }

    @Test
    public void theRightSideOfAnUnexistingDiffIsSavedSoTheDiffRequestIsCreated() {

        final Integer diffId = 13;
        final byte[] data = {(byte)0x80, 0x53, 0x1c,
                (byte)0x87, (byte)0xa0, 0x42, 0x69, 0x10, (byte)0xa2, (byte)0xea, 0x08,
                0x00, 0x2b, 0x30, 0x30, (byte)0x9d };

        instance.saveRightSideOfADiff(diffId, data);

        thenTheDiffRequestHasBeenPersistedFromScratchWith(diffId, null ,data);

    }

    @Test
    public void theLeftSideOfAnExistingDiffRequestIsSavedSoTheExistingDiffRequestIsUpdated() {

        final Integer diffId = 13;
        final byte[] data = {(byte)0x80, 0x53, 0x1c,
                (byte)0x87, (byte)0xa0, 0x42, 0x69, 0x10, (byte)0xa2, (byte)0xea, 0x08,
                0x00, 0x2b, 0x30, 0x30, (byte)0x9d };


        DiffRequest exisitingDiffRequest = givenThatTheDiffRequestExistsWithTheRightDataLoaded(diffId);

        instance.saveLeftSideOfADiff(diffId, data);

        theLeftDataHasBeenUpdated(exisitingDiffRequest, data);
    }

    @Test
    public void theRightSideOfAnExistingDiffRequestIsSavedSoTheExistingDiffRequestIsUpdated() {

        final Integer diffId = 13;
        final byte[] data = {(byte)0x80, 0x53, 0x1c,
                (byte)0x87, (byte)0xa0, 0x42, 0x69, 0x10, (byte)0xa2, (byte)0xea, 0x08,
                0x00, 0x2b, 0x30, 0x30, (byte)0x9d };


        DiffRequest exisitingDiffRequest = givenThatTheDiffRequestExistsWithTheLeftDataLoaded(diffId);

        instance.saveRightSideOfADiff(diffId, data);

        theRightDataHasBeenUpdated(exisitingDiffRequest, data);
    }

    @Test
    public void aDiffIsExecutedOverARequestWithTheSameLeftAndRighrDataThenItReturnsEqualsResult() {

        final Integer diffId = 13;
        givenThatExistsARequestHasTheSameData(diffId);

        Diff diff = instance.executeDiff(diffId);

        assertThat(diff).isNotNull();
        assertThat(diff.getDiffResult()).isEqualTo(DiffExecutionResult.EQUALS);
    }

    @Test
    public void aDiffIsExecutedOverARequestWithDifferentDataSizes() {

        final Integer diffId = 13;
        givenThatExistsARequestHasDifferentDataSize(diffId);

        Diff diff = instance.executeDiff(diffId);

        assertThat(diff).isNotNull();
        assertThat(diff.getDiffResult()).as("The diff result is invalid")
                .isEqualTo(DiffExecutionResult.DIFFERENT_SIZE);
    }

    @Test
    public void aDiffIsExecutedOverARequestWithSAmeDataSizesbutOneDifferenceInContentAtTheBeginning() {

        final Integer diffId = 13;
        givenThatExistsARequestHasSameDataSizeButOneDifferenceInContentAtTheBeginning(diffId);

        Diff diff = instance.executeDiff(diffId);

        thenTheDifferenceHasBeenDetected(diff);
    }

    @Test
    public void aDiffIsExecutedOverARequestWithSAmeDataSizesButTwoDifferences() {

        final Integer diffId = 13;
        givenThatExistsARequestHasSameDataSizeButTwoDifferences(diffId);

        Diff diff = instance.executeDiff(diffId);

        thenBothDifferenceaHaveBeenDetected(diff);
    }

    private void givenThatExistsARequestHasSameDataSizeButTwoDifferences(Integer diffId) {

        byte[] leftData = DatatypeConverter.parseHexBinary( "FFFFAAAAAAAAAAAA");
        byte[] rightData = DatatypeConverter.parseHexBinary("AAAAAAFFFFFFAAAA");

        DiffRequest diffRequest = new DiffRequest();
        diffRequest.setId(diffId);
        diffRequest.setLeftSideData(leftData);
        diffRequest.setRightSideData(rightData);
        when(respository.findById(diffId)).thenReturn(Optional.of(diffRequest));
    }

    private void givenThatExistsARequestHasSameDataSizeButOneDifferenceInContentAtTheBeginning(Integer diffId) {

        byte[] leftData = DatatypeConverter.parseHexBinary( "FFFFAAAAAAAA");
        byte[] rightData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAA");
        DiffRequest diffRequest = new DiffRequest();
        diffRequest.setId(diffId);
        diffRequest.setLeftSideData(leftData);
        diffRequest.setRightSideData(rightData);
        when(respository.findById(diffId)).thenReturn(Optional.of(diffRequest));
    }

    private void givenThatExistsARequestHasDifferentDataSize(Integer diffId) {

        byte[] leftData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAA");
        byte[] rightData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAADD");

        DiffRequest diffRequest = new DiffRequest();
        diffRequest.setId(diffId);
        diffRequest.setLeftSideData(leftData);
        diffRequest.setRightSideData(rightData);
        when(respository.findById(diffId)).thenReturn(Optional.of(diffRequest));
    }

    private void givenThatExistsARequestHasTheSameData(Integer diffId) {

        byte[] leftData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAA");
        byte[] rightData = DatatypeConverter.parseHexBinary("AAAAAAAAAAAA");

        DiffRequest diffRequest = new DiffRequest();
        diffRequest.setId(diffId);
        diffRequest.setLeftSideData(leftData);
        diffRequest.setRightSideData(rightData);
        when(respository.findById(diffId)).thenReturn(Optional.of(diffRequest));
    }

    private DiffRequest givenThatTheDiffRequestExistsWithTheLeftDataLoaded(Integer diffId) {

        DiffRequest diffRequest = new DiffRequest();
        diffRequest.setId(diffId);
        byte[] data = new byte[10];
        diffRequest.setLeftSideData(data);
        when(respository.findById(diffId)).thenReturn(Optional.of(diffRequest));
        return diffRequest;
    }

    private void theRightDataHasBeenUpdated(DiffRequest existingDiff, byte[] newData) {

        verify(respository, times(1)).save(diffRequestCaptor.capture());
        assertThat(diffRequestCaptor.getValue().getId()).as("The diff id is not present")
                .isEqualTo(existingDiff.getId());
        assertThat(diffRequestCaptor.getValue().getLeftSideData()).as("The existing data is not present")
                .isEqualTo(existingDiff.getLeftSideData());
        assertThat(diffRequestCaptor.getValue().getRightSideData()).as("The new data is not present")
                .isEqualTo(newData);
    }

    private void theLeftDataHasBeenUpdated(DiffRequest existingDiff, byte[] newData) {

        verify(respository, times(1)).save(diffRequestCaptor.capture());
        assertThat(diffRequestCaptor.getValue().getId()).as("The diff id is not present")
                .isEqualTo(existingDiff.getId());
        assertThat(diffRequestCaptor.getValue().getLeftSideData()).as("The new data is not present")
                .isEqualTo(newData);
        assertThat(diffRequestCaptor.getValue().getRightSideData()).as("The existing data is not present")
                .isEqualTo(existingDiff.getRightSideData());
    }

    private DiffRequest givenThatTheDiffRequestExistsWithTheRightDataLoaded(Integer diffId) {

        DiffRequest diffRequest = new DiffRequest();
        diffRequest.setId(diffId);
        byte[] data = new byte[10];
        diffRequest.setRightSideData(data);
        when(respository.findById(diffId)).thenReturn(Optional.of(diffRequest));
        return diffRequest;
    }

    private void thenTheDiffRequestHasBeenPersistedFromScratchWith(Integer diffId, byte[] leftData, byte[] rightData) {

        verify(respository, times(1)).save(diffRequestCaptor.capture());
        assertThat(diffRequestCaptor.getValue().getId()).isEqualTo(diffId);
        assertThat(diffRequestCaptor.getValue().getLeftSideData()).isEqualTo(leftData);
        assertThat(diffRequestCaptor.getValue().getRightSideData()).isEqualTo(rightData);
    }

    private void thenTheDifferenceHasBeenDetected(Diff diff) {

        assertThat(diff).isNotNull();
        assertThat(diff.getDiffResult()).as("The diff result is invalid")
                .isEqualTo(DiffExecutionResult.DIFFERENT_CONTENT);
        assertThat(diff.getDifferences()).as("No difference has been specified")
                .hasSize(1);
        assertThat(diff.getDifferences().get(0).getOffset()).as("The Offset of the difference is incorrect")
                .isEqualTo(0);
        assertThat(diff.getDifferences().get(0).getLength()).as("The Length of the difference is incorrect")
                .isEqualTo(2);
    }

    private void thenBothDifferenceaHaveBeenDetected(Diff diff) {

        assertThat(diff).isNotNull();
        assertThat(diff.getDiffResult()).as("The diff result is invalid")
                .isEqualTo(DiffExecutionResult.DIFFERENT_CONTENT);
        assertThat(diff.getDifferences()).as("Some differences hasn't been detected")
                .hasSize(2);
        assertThat(diff.getDifferences().get(0).getOffset()).isEqualTo(0).as("The first offset is incorrect");
        assertThat(diff.getDifferences().get(0).getLength()).isEqualTo(2).as("The first length is incorrect");

        assertThat(diff.getDifferences().get(1).getOffset()).isEqualTo(3).as("The second offset is incorrect");
        assertThat(diff.getDifferences().get(1).getLength()).isEqualTo(3).as("The second length is incorrect");
    }

}
