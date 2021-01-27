package com.example.zippedimagesdownloaderdemo.viewmodel;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.example.zippedimagesdownloaderdemo.R;
import com.example.zippedimagesdownloaderdemo.model.ImageAssetData;
import com.example.zippedimagesdownloaderdemo.network.ImagesRepository;
import com.example.zippedimagesdownloaderdemo.network.NetworkCallback;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import lombok.Getter;
import servicelocator.ServiceLocator;


/**
 * View model for managing the logic required to obtain the correct images to display
 */
public class ImagesViewModel extends ViewModel {

    private static final String TAG = ImagesViewModel.class.getName();

    @Getter private final MutableLiveData<Uri> imageUriLiveData = new MutableLiveData<>();
    private final ImagesRepository repository = ServiceLocator.getInstance().newImageRepository();
    private final SharedPreferences sharedPreferences = ServiceLocator.getInstance().getSharedPreference();
    private final Resources resources = ServiceLocator.getInstance().getResources();
    private final Map<ImageAssetData, ImageAssetData> nextAssetForDownloadMap = newNextAssetToDownloadMap();
    private final ImageAssetData defaultAssetData = ImageAssetData.ASSET_125;
    private ImageAssetData currentAsset = defaultAssetData;

    /**
     * Creates a map used to acquire the next image asset to be download.
     * @return Map containing the next assets to download
     */
    private Map<ImageAssetData, ImageAssetData> newNextAssetToDownloadMap() {
        Map<ImageAssetData, ImageAssetData> result = new HashMap<>();
        result.put(ImageAssetData.ASSET_125, ImageAssetData.ASSET_127);
        result.put(ImageAssetData.ASSET_127, ImageAssetData.ASSET_125);
        return result;
    }

    /**
     * Opening a request for a new image
     */
    public void requestNewImage() {
        String currentTestAssetString = sharedPreferences.getString(resources.getString(R.string.key_current_asset), defaultAssetData.name());
        this.currentAsset = ImageAssetData.valueOf(currentTestAssetString);
        repository.downloadImageFile(currentAsset, new NetworkCallback<URI>() {
            @Override
            public void onSuccess(Uri imageUri) {
                Log.d(TAG, "Image download success");
                ImagesViewModel.this.imageUriLiveData.setValue(imageUri);
                setNextAssetsForDownload();
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "Image request failure: " + message);
                ImagesViewModel.this.imageUriLiveData.setValue(null);
            }
        });
    }

    /**
     * Setting the next asset to be download by the app.
     * The method puts the next asset's key in the app's shared preferences.
     * The key is later used in the corresponding assets map to get the correct asset value.
     */
    private void setNextAssetsForDownload() {
        ImageAssetData nextAssetData = nextAssetForDownloadMap.get(ImagesViewModel.this.currentAsset); //Getting the next asset data is derived from the previous asset data
        if(nextAssetData == null){
            nextAssetData = defaultAssetData;
        }
        sharedPreferences.edit().putString(resources.getString(R.string.key_current_asset), nextAssetData.name()).apply();
    }
}
