package com.example.zippedimagesdownloaderdemo.network;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.example.zippedimagesdownloaderdemo.R;
import com.example.zippedimagesdownloaderdemo.app.ZippedImagesDownloadApp;
import com.example.zippedimagesdownloaderdemo.model.TestAssetData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFilesDownloadServiceImpl implements ZipFilesDownloadService {

    private static final String TAG = ZipFilesDownloadServiceImpl.class.getName();
    private final Resources resources = ZippedImagesDownloadApp.APPLICATION_CONTEXT.getResources(); //todo: DI

    @Override
    public void download(TestAssetData asset, NetworkCallback<URI> callback) {
        String mUrl = asset.getUrl();
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl,
                response -> {
                    try {
                        if (response != null) {

                            //Todo: remove
//                        Log.d(TAG, "Download response: " + Arrays.toString(response));
//                            FileOutputStream fileOutputStream;
//                            fileOutputStream = ZippedImagesDownloadApp.APPLICATION_CONTEXT.openFileOutput(asset.getZipDirName(), Context.MODE_PRIVATE);
//                            fileOutputStream.write(response);
//                            fileOutputStream.close();
//                            String root = ZippedImagesDownloadApp.APPLICATION_CONTEXT.getFilesDir().getAbsolutePath();
                            String parentDir = Environment.getExternalStorageDirectory().toString() + "/" + asset.getParentDirName() + "/";
                            saveZipFile(asset, response);
                            File zipFile = new File(parentDir, asset.getZipFileName());
                            File targetExactionFile = new File(parentDir, asset.getExtractedAssetDirName());
                            unzipFile(zipFile, targetExactionFile);
                            File[] allFilesInFolder = targetExactionFile.listFiles();
                            if (allFilesInFolder != null && allFilesInFolder[0] != null) {
                                String completeImagePath = targetExactionFile + "/" + allFilesInFolder[0].getName();
                                File file = new File(completeImagePath);
                                Uri imageUri = Uri.fromFile(file);
                                callback.onSuccess(imageUri);
                            }else{
                                callback.onFailure(resources.getString(R.string.error_no_file_found));
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Unable to download file");
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace, null);
        RequestQueue mRequestQueue;
        mRequestQueue = Volley.newRequestQueue(ZippedImagesDownloadApp.APPLICATION_CONTEXT, new HurlStack());
        mRequestQueue.add(request);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveZipFile(TestAssetData assetData, byte[] response) {
        String root = Environment.getExternalStorageDirectory().toString();
        if (isStoragePermissionGranted()) {
            File rootDir = new File(root, assetData.getParentDirName());
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
            File file = new File(rootDir, assetData.getZipFileName());
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                out.write(response);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void unzipFile(File zipFile, File targetDirectory) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry zipEntry;
            int count;
            int fourMBSize = 8192;
            byte[] buffer = new byte[fourMBSize];
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File extractedFile = new File(targetDirectory, zipEntry.getName());
                File extractedFileDir = zipEntry.isDirectory() ? extractedFile : extractedFile.getParentFile();

                if (!Objects.requireNonNull(extractedFileDir).isDirectory() && !extractedFileDir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + extractedFileDir.getAbsolutePath());
                if (zipEntry.isDirectory())
                    continue;
                try (FileOutputStream fileOutputStream = new FileOutputStream(extractedFile)) {
                    while ((count = zipInputStream.read(buffer)) != -1)
                        fileOutputStream.write(buffer, 0, count);
                }
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (ZippedImagesDownloadApp.APPLICATION_CONTEXT.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
}
