package com.example.zippedimagesdownloaderdemo.network;

import com.example.zippedimagesdownloaderdemo.model.TestAssetData;

import java.net.URI;

public interface ZipFilesDownloadService {

    void download(TestAssetData asset, NetworkCallback<URI> callback);
}
