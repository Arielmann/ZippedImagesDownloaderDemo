package com.example.zippedimagesdownloaderdemo.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * A custom volley request for fetching input streams.
 */
class InputStreamVolleyRequest extends Request<byte[]> {

    private final Response.Listener<byte[]> responseListener;

    public InputStreamVolleyRequest(int method, String mUrl, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        super(method, mUrl, errorListener);
        responseListener = listener;
    }

    @Override
    protected void deliverResponse(byte[] response) {
        responseListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response)); //Pass the response data here
    }
}
