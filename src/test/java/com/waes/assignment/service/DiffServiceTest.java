package com.waes.assignment.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.waes.assignment.model.DiffRequest;
import com.waes.assignment.repositories.DiffRequestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

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

        thenTheDiffRequestHasBeenPersistedAsNewWith(diffId, data);

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

    private void theLeftDataHasBeenUpdated(DiffRequest existingDiff, byte[] newLeftData) {

        verify(respository, times(1)).save(diffRequestCaptor.capture());
        assertThat(diffRequestCaptor.getValue().getId()).isEqualTo(existingDiff.getId());
        assertThat(diffRequestCaptor.getValue().getRightSideData()).isEqualTo(existingDiff.getRightSideData());
        assertThat(diffRequestCaptor.getValue().getLeftSideData()).isEqualTo(newLeftData);
    }

    private DiffRequest givenThatTheDiffRequestExistsWithTheRightDataLoaded(Integer diffId) {

        DiffRequest diffRequest = new DiffRequest();
        diffRequest.setId(diffId);
        byte[] data = new byte[10];
        diffRequest.setRightSideData(data);
        when(respository.getOne(diffId)).thenReturn(diffRequest);
        return diffRequest;
    }

    private void thenTheDiffRequestHasBeenPersistedAsNewWith(Integer diffId, byte[] data) {

        verify(respository, times(1)).save(diffRequestCaptor.capture());
        assertThat(diffRequestCaptor.getValue().getId()).isEqualTo(diffId);
        assertThat(diffRequestCaptor.getValue().getLeftSideData()).isEqualTo(data);
    }

}
