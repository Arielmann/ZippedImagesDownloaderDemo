package com.example.zippedimagesdownloaderdemo.network;

import android.util.Log;

import com.example.zippedimagesdownloaderdemo.R;
import com.example.zippedimagesdownloaderdemo.app.ZippedImagesDownloadApp;
import com.example.zippedimagesdownloaderdemo.model.ImageAssetData;
import com.example.zippedimagesdownloaderdemo.utils.Utils;

import java.net.URI;

import servicelocator.ServiceLocator;

/**
 * Class for mediating between viewmodel's image requests and the components required to obtain them
 */
public class ImagesRepository {

    private static final String TAG = ImagesRepository.class.getName();

    private final ImageFileDownloadService imageFileDownloadService = ServiceLocator.getInstance().newImageFileDownloadService();

    /**
     * Starting the image download using an {@link ImageFileDownloadService}
     * @param assetData Data regarding the required asset
     * @param callback A callback to for notifying the operation status
     */
    public void downloadImageFile(ImageAssetData assetData, NetworkCallback<URI> callback) {
        String noNetworkError = ZippedImagesDownloadApp.APPLICATION_CONTEXT.getString(R.string.error_no_network_connection);
        if (Utils.isNetworkAvailable()) {
            imageFileDownloadService.download(assetData, callback);
        } else {
            Log.w(TAG, "No network connection");
            callback.onFailure(noNetworkError);
        }
    }
}
