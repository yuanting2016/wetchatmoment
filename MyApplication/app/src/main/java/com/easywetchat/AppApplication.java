package com.easywetchat;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

//        if (AndPermission.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))
//            initFresco();
//        else { // default set
//            Fresco.initialize(this);
//        }
        //todo() use default set
         Fresco.initialize(this);
    }

    /**
     * init Fresco。
     */
    public void initFresco() {
//        Fresco.initialize(this, ImagePipelineConfig.newBuilder(this)
//                .setMainDiskCacheConfig(
//                        DiskCacheConfig.newBuilder(this)
//                                .setBaseDirectoryPath("//path file")
//                                .build()
//                )
//                .build()
//        );
    }

}
