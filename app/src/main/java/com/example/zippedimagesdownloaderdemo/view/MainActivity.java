package com.example.zippedimagesdownloaderdemo.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.zippedimagesdownloaderdemo.R;
import com.example.zippedimagesdownloaderdemo.databinding.ActivityMainBinding;
import com.example.zippedimagesdownloaderdemo.viewmodel.ImagesViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private ActivityMainBinding binding;
    private ImagesViewModel viewModel;

    private final View.OnClickListener onImageClickedListener = view -> performImageRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ImagesViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar.show();
        binding.assetDisplayIV.setOnClickListener(onImageClickedListener);
        observeImageData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        performImageRequest();
    }


    /**
     * Observe changes within the image data and updating the view accordingly.
     */
    private void observeImageData() {
        viewModel.getImageUriLiveData().observe(this, uri -> { //Using LiveData for reacting to changes
            if (uri != null) {
                Log.d(TAG, "Obtained new image for display");
                binding.progressBar.hide();
                binding.assetDisplayIV.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this)
                        .load(uri)
                        .into(binding.assetDisplayIV);
            } else {
                onDataDisplayRequestError();
            }
        });
    }

    /**
     * Starts a new image request and displays UI related with this operation
     */
    private void performImageRequest() {
        Log.d(TAG, "Starting new image request");
        binding.progressBar.show();
        binding.errorTV.setVisibility(View.INVISIBLE);
        binding.assetDisplayIV.setVisibility(View.INVISIBLE);
        viewModel.requestNewImage();
    }

    /**
     * Displayed whenever the image request operation ends with an error
     */
    private void onDataDisplayRequestError() {
        Log.e(TAG, getString(R.string.error_msg_snapshot_network_fetching));
        binding.progressBar.hide();
        binding.assetDisplayIV.setVisibility(View.VISIBLE);
        binding.errorTV.setVisibility(View.VISIBLE);
        binding.assetDisplayIV.setImageResource(R.drawable.ic_baseline_refresh_gray_48);
    }
}