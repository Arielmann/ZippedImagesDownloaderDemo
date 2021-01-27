package com.example.zippedimagesdownloaderdemo.network;

import android.net.Uri;

public interface NetworkCallback<E> {

    void onSuccess(Uri response);

    void onFailure(String message);

}