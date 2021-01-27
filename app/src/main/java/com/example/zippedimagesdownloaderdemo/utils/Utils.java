package com.example.zippedimagesdownloaderdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.zippedimagesdownloaderdemo.app.ZippedImagesDownloadApp;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    /**
     * Determines if the device is connected to the internet.
     * @return True if device is connected to the internet
     */
    public static Boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) ZippedImagesDownloadApp.APPLICATION_CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /**
     * Unzips data represented by a {@link ZipInputStream} into a new folder
     * @param data The zip file's data
     * @param targetDirectory The directory to save the extracted data
     * @throws IOException Thrown if there was a problem completing the operation
     */
    public static void unzipFile(byte[] data, File targetDirectory) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(data)))) {
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

}
