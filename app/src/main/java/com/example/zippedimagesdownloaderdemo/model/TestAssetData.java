package com.example.zippedimagesdownloaderdemo.model;

import lombok.Getter;

public enum TestAssetData {

    ASSET_125("test_assets", "125@2.zip", "https://test-assets-mobile.s3-us-west-2.amazonaws.com/125%402.zip"),
    ASSET_127("test_assets", "127@2.zip", "https://test-assets-mobile.s3-us-west-2.amazonaws.com/127%402.zip");

    @Getter
    private final String parentDirName;
    @Getter
    private final String zipFileName;
    @Getter
    private final String url;


    TestAssetData(String parentDirName, String zipFileName, String url) {
        this.parentDirName = parentDirName;
        this.zipFileName = zipFileName;
        this.url = url;
    }

    public String getExtractedAssetDirName() {
        return zipFileName.replace(".zip", "");
    }
}
