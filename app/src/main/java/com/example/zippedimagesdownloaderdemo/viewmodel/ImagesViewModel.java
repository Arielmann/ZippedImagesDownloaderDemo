package com.example.zippedimagesdownloaderdemo.viewmodel;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.zippedimagesdownloaderdemo.R;
import com.example.zippedimagesdownloaderdemo.app.ZippedImagesDownloadApp;
import com.example.zippedimagesdownloaderdemo.model.TestAssetData;
import com.example.zippedimagesdownloaderdemo.network.ImagesRepository;
import com.example.zippedimagesdownloaderdemo.network.NetworkCallback;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import lombok.Getter;

public class ImagesViewModel extends ViewModel {

    private static final String TAG = ImagesViewModel.class.getName();

    @Getter private final MutableLiveData<Uri> imageUriLiveData = new MutableLiveData<>();
    private final ImagesRepository repository = new ImagesRepository(); //todo: DI
    private final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ZippedImagesDownloadApp.APPLICATION_CONTEXT); //todo: DI
    private final Resources resources = ZippedImagesDownloadApp.APPLICATION_CONTEXT.getResources(); //todo: DI
    private final Map<TestAssetData, TestAssetData> nextAssetForDownloadMap = setupNextAssetToDownloadMap();
    private final TestAssetData defaultAssetData = TestAssetData.ASSET_125;
    private TestAssetData currentAsset = defaultAssetData;

    private Map<TestAssetData, TestAssetData> setupNextAssetToDownloadMap() {
        Map<TestAssetData, TestAssetData> result = new HashMap<>();
        result.put(TestAssetData.ASSET_125, TestAssetData.ASSET_127);
        result.put(TestAssetData.ASSET_127, TestAssetData.ASSET_125);
        return result;
    }

    public void requestNewImage() {
        String currentTestAssetString = sharedPreferences.getString(resources.getString(R.string.key_current_asset), defaultAssetData.name());
        this.currentAsset = TestAssetData.valueOf(currentTestAssetString);
        repository.downloadZipFile(currentAsset, new NetworkCallback<URI>() {
            @Override
            public void onSuccess(Uri imageUri) {
                Log.d(TAG, "Image request success");
                ImagesViewModel.this.imageUriLiveData.setValue(imageUri);
                TestAssetData nextAssetData = nextAssetForDownloadMap.get(ImagesViewModel.this.currentAsset); //Getting the next asset data is derived from the previous asset data
                if(nextAssetData == null){
                    nextAssetData = defaultAssetData;
                }
                sharedPreferences.edit().putString(resources.getString(R.string.key_current_asset), nextAssetData.name()).apply();
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "Image request failure: " + message);
            }
        });
    }
}
