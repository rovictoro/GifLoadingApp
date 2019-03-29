package com.rovictoro.giphy.models;

import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.network.response.ListMediaResponse;

import java.util.ArrayList;
import java.util.List;

public class GifModel {

    private String gifUrl;
    private String gifFixedWidthDownsampledUrl;
    private String title;
    private int position;
    private int size;
    private int fixedWidthDownsampledSize;

    public GifModel(){};
    public GifModel(Media gif, int position) {

                this.gifUrl = gif.getImages().getOriginal().getGifUrl();
                this.gifFixedWidthDownsampledUrl = gif.getImages().getFixedWidthDownsampled()/*.getDownsized()*/.getGifUrl();
                this.title = gif.getTitle();
                this.size = gif.getImages().getOriginal().getGifSize();
                this.fixedWidthDownsampledSize = gif.getImages().getFixedWidthDownsampled().getGifSize();
                this.position = position;

    }

    public String getGifUrl() {
        return gifUrl;
    }
    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getFixedWidthDownsampledUrl() {
        return gifFixedWidthDownsampledUrl;
    }
    public void setFixedWidthDownsampledUrl(String gifFixedWidthDownsampledUrl) {
        this.gifFixedWidthDownsampledUrl = gifFixedWidthDownsampledUrl;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public int getFixedWidthDownsampledSize() {
        return fixedWidthDownsampledSize;
    }
    public void setFixedWidthDownsampledSize(int fixedWidthDownsampledSize) {
        this.fixedWidthDownsampledSize = fixedWidthDownsampledSize;
    }



}
