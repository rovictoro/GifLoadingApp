package com.rovictoro.giphy.models;

import com.giphy.sdk.core.models.Media;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Media.class})
public class GifModelTest {

    GifModel mGm;

    @Mock
    private Media gif;

    @Before
    public void setUp() throws Exception {

        gif = Mockito.mock(Media.class);


        mGm = new GifModel();
        mGm.setPosition(0);
        mGm.setTitle("Test");
        mGm.setFixedWidthDownsampledSize(1);
        mGm.setFixedWidthDownsampledUrl("testUrl");
        mGm.setGifUrl("GifUrl");
        mGm.setSize(20);


    }

    @After
    public void tearDown() throws Exception {
        mGm = null;
    }

    @Test
    public void setGifModel() {

        mGm = new GifModel(gif, 10);
        //mGm.setGifUrl("mocked URL");

        //when(gif.getImages().getOriginal().getGifUrl()).thenReturn("mocked URL");


        assertEquals("mocked URL",mGm.getGifUrl());


    }

    @Test
    public void getGifUrl() {
        assertEquals("GifUrl", mGm.getGifUrl());
    }

    @Test
    public void setGifUrl() {

        //assertThat("set GifUrl", mGm.setGifUrl("set GifUrl"));

    }

    @Test
    public void getFixedWidthDownsampledUrl() {
        assertEquals("testUrl", mGm.getFixedWidthDownsampledUrl());
    }

    @Test
    public void setFixedWidthDownsampledUrl() {
    }

    @Test
    public void getTitle() {
        assertEquals("Test", mGm.getTitle());
    }

    @Test
    public void setTitle() {
    }

    @Test
    public void getPosition() {
        assertEquals(0, mGm.getPosition());
    }

    @Test
    public void setPosition() {
    }

    @Test
    public void getSize() {
        assertEquals(20, mGm.getSize());
    }

    @Test
    public void setSize() {
    }

    @Test
    public void getFixedWidthDownsampledSize() {
        assertEquals(1, mGm.getFixedWidthDownsampledSize());

    }

    @Test
    public void setFixedWidthDownsampledSize() {
    }
}