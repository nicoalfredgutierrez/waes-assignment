package com.waes.assignment.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.waes.assignment.model.Diff;
import com.waes.assignment.repositories.DiffRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

    private DiffService instance;
    private DiffRepository respository;

    @Captor
    private ArgumentCaptor<Diff> diffCaptor;

    @Before
    public void initialize() {

        instance = new DiffService();
        respository = mock(DiffRepository.class);
        instance.setDiffRepository(respository);
    }

    @Test
    public void theLeftSideOfAnUnexistingDiffIsSavedSoTheDiffIsCreated() {

        final Integer diffId = 13;
        final byte[] data = {(byte)0x80, 0x53, 0x1c,
                (byte)0x87, (byte)0xa0, 0x42, 0x69, 0x10, (byte)0xa2, (byte)0xea, 0x08,
                0x00, 0x2b, 0x30, 0x30, (byte)0x9d };

        instance.saveLeftSideOfADiff(diffId, data);

        thenTheDiffHasBeenPersistedAsNewWith(diffId, data);

    }

    @Test
    public void theLeftSideOfAnExistingDiffIsSavedSoTheExistingDiffIsUpdated() {

        final Integer diffId = 13;
        final byte[] data = {(byte)0x80, 0x53, 0x1c,
                (byte)0x87, (byte)0xa0, 0x42, 0x69, 0x10, (byte)0xa2, (byte)0xea, 0x08,
                0x00, 0x2b, 0x30, 0x30, (byte)0x9d };


        Diff exisitingDiff = givenThatTheDiffExistsWithTheRightDataLoaded(diffId);

        instance.saveLeftSideOfADiff(diffId, data);

        theLeftDataHasBeenUpdated(exisitingDiff, data);
    }

    private void theLeftDataHasBeenUpdated(Diff existingDiff, byte[] newLeftData) {

        verify(respository, times(1)).save(diffCaptor.capture());
        assertThat(diffCaptor.getValue().getId()).isEqualTo(existingDiff.getId());
        assertThat(diffCaptor.getValue().getRightSideData()).isEqualTo(existingDiff.getRightSideData());
        assertThat(diffCaptor.getValue().getLeftSideData()).isEqualTo(newLeftData);
    }

    private Diff givenThatTheDiffExistsWithTheRightDataLoaded(Integer diffId) {

        Diff diff = new Diff();
        diff.setId(diffId);
        byte[] data = new byte[10];
        diff.setRightSideData(data);
        when(respository.getOne(diffId)).thenReturn(diff);
        return diff;
    }

    private void thenTheDiffHasBeenPersistedAsNewWith(Integer diffId, byte[] data) {

        verify(respository, times(1)).save(diffCaptor.capture());
        assertThat(diffCaptor.getValue().getId()).isEqualTo(diffId);
        assertThat(diffCaptor.getValue().getLeftSideData()).isEqualTo(data);
    }

}
