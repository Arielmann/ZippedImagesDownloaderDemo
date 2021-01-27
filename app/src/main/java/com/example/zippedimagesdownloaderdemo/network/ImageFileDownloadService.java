package com.example.zippedimagesdownloaderdemo.network;

import com.example.zippedimagesdownloaderdemo.model.ImageAssetData;

import java.net.URI;

/**
 * Implemented by classes that provide an image download functionality
 */
public interface ImageFileDownloadService {

    void download(ImageAssetData asset, NetworkCallback<URI> callback);
}
