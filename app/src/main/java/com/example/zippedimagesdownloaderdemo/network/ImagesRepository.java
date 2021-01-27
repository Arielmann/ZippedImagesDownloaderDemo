package com.example.zippedimagesdownloaderdemo.network;

import com.example.zippedimagesdownloaderdemo.model.TestAssetData;

import java.net.URI;

public class ImagesRepository {

    private final ZipFilesDownloadService zipFilesDownloadService = new ZipFilesDownloadServiceImpl();

    public void downloadZipFile(TestAssetData currentAsset, NetworkCallback<URI> callback) {
        zipFilesDownloadService.download(currentAsset, callback);
    }
}
