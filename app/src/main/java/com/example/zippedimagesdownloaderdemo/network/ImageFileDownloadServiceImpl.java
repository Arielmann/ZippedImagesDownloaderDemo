package com.example.zippedimagesdownloaderdemo.network;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.example.zippedimagesdownloaderdemo.R;
import com.example.zippedimagesdownloaderdemo.app.ZippedImagesDownloadApp;
import com.example.zippedimagesdownloaderdemo.model.ImageAssetData;
import com.example.zippedimagesdownloaderdemo.utils.Utils;

import java.io.File;
import java.net.URI;

import servicelocator.ServiceLocator;

/**
 * Implementation class for image downloading service.
 * The class supports downloading images contained within .zip files
 */
public class ImageFileDownloadServiceImpl implements ImageFileDownloadService {

    private static final String TAG = ImageFileDownloadServiceImpl.class.getName();

    private final Resources resources = ServiceLocator.getInstance().getResources();

    /**
     * Download service implementation method.
     * The method downloads the zip file within the {@link ImageAssetData}
     * @param assetData Image asset's data
     * @param callback A callback to for notifying the operation's status
     */
    @Override
    public void download(ImageAssetData assetData, NetworkCallback<URI> callback) {
        Log.d(TAG, "Starting image download process");
        String mUrl = assetData.getUrl();
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl,
                response -> {
                    try {
                        if (response != null) {
                            Log.d(TAG, "Image download response received from server");
                            String parentDir = ZippedImagesDownloadApp.APPLICATION_CONTEXT.getFilesDir() + "/" + assetData.getParentDirName() + "/";
                            File targetExtractionFile = new File(parentDir, assetData.getExtractedAssetDirName());
                            Utils.unzipFile(response, targetExtractionFile);
                            Log.d(TAG, "File unzipped successfully");
                            File imageFile = getFirstFileFromDir(targetExtractionFile);
                            if (imageFile != null) {
                                Uri imageUri = Uri.fromFile(imageFile);
                                Log.d(TAG, "Obtained downloaded file's Uri");
                                callback.onSuccess(imageUri);
                            } else {
                                callback.onFailure(resources.getString(R.string.error_no_file_found));
                            }
                        } else {
                            callback.onFailure(resources.getString(R.string.error_null_server_response));
                        }
                    } catch (Exception e) {
                        callback.onFailure(resources.getString(R.string.error_bad_response_parsing));

                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            callback.onFailure(resources.getString(R.string.error_bad_network_request));
        });
        RequestQueue mRequestQueue;
        mRequestQueue = Volley.newRequestQueue(ZippedImagesDownloadApp.APPLICATION_CONTEXT, new HurlStack());
        mRequestQueue.add(request);
    }

    /**
     * Returns the first file found within the directory. Used with the task's assumption that every asset's directory contains only one file - the image file.
     * @param dir The directory containing the file
     * @return The first file found within the directory
     */
    private File getFirstFileFromDir(File dir) {
        File[] allFilesInFolder = dir.listFiles();
        if (allFilesInFolder != null && allFilesInFolder[0] != null) {
            String completeImagePath = dir + "/" + allFilesInFolder[0].getName();
            return new File(completeImagePath);
        } else {
            return null;
        }
    }
}
