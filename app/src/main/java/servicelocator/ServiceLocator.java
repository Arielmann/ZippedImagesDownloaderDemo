package servicelocator;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.example.zippedimagesdownloaderdemo.app.ZippedImagesDownloadApp;
import com.example.zippedimagesdownloaderdemo.network.ImageFileDownloadService;
import com.example.zippedimagesdownloaderdemo.network.ImageFileDownloadServiceImpl;
import com.example.zippedimagesdownloaderdemo.network.ImagesRepository;

/**
 * A singleton designated to provide dependencies for app components that requires those dependencies.
 * This is an alternative for Android's dependency injection solutions and fits for a project of this type.
 */
public final class ServiceLocator {

    private static final ServiceLocator INSTANCE = new ServiceLocator();

    private ServiceLocator() { }

    public static ServiceLocator getInstance() {
        return INSTANCE;
    }

    public ImagesRepository newImageRepository() {
        return new ImagesRepository();
    }

    public ImageFileDownloadService newImageFileDownloadService() {
        return new ImageFileDownloadServiceImpl();
    }

    public SharedPreferences getSharedPreference() {
        return PreferenceManager.getDefaultSharedPreferences(ZippedImagesDownloadApp.APPLICATION_CONTEXT);
    }

    public Resources getResources() {
        return ZippedImagesDownloadApp.APPLICATION_CONTEXT.getResources();
    }

}
