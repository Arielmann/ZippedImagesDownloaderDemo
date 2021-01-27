package com.example.zippedimagesdownloaderdemo.model;

import lombok.Getter;

/**
 * Enum for representing an image asset data.
 * It is used to conveniently access correct folder names and server endpoints.
 */
public enum ImageAssetData {

    ASSET_125("test_assets", "asset_125", "https://test-assets-mobile.s3-us-west-2.amazonaws.com/125%402.zip"),
    ASSET_127("test_assets", "asset_127", "https://test-assets-mobile.s3-us-west-2.amazonaws.com/127%402.zip");

    @Getter
    private final String parentDirName;
    @Getter
    private final String extractedAssetDirName;
    @Getter
    private final String url;


    ImageAssetData(String parentDirName, String extractedAssetDirName, String url) {
        this.parentDirName = parentDirName;
        this.extractedAssetDirName = extractedAssetDirName;
        this.url = url;
    }

}
