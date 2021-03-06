package net.school_a.app.app;

import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fanxin.easeui.controller.EaseUI;
import com.hyphenate.chat.EMOptions;
import com.jude.utils.JActivityManager;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

import net.school_a.app.model.bean.LocalUserDataModel;
import net.school_a.app.utils.BaseUtils;

import java.io.File;

import cn.smssdk.SMSSDK;

/**
 * author：Anumbrella
 * Date：16/5/23 下午6:08
 */
public class App extends MultiDexApplication {

    public static final String FILENAME = "userInfo.json";

    private static App singleton;

    public static App getApp() {
        if (singleton == null) {
            synchronized (App.class) {
                singleton = new App();
            }
        }
        return singleton;
    }


    //文件目录列表
    public enum Dir {
        Object
    }

    @Override
    public void onCreate() {
        MultiDex.install(this);
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(true);
        EaseUI.getInstance().init(this, options);
        super.onCreate();
        Fresco.initialize(this);
        JUtils.initialize(this);

        //生成文件夹
        JFileManager.getInstance().init(this, Dir.values());

        if (BaseUtils.readLocalUser(App.this) == null) {
            initLocalUserData();
        }
        String path = Environment.getExternalStorageDirectory() + "/LKShop/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        //mob短信验证初始化
        SMSSDK.initSDK(this, "14359dfc08d04", "720a87f2ddcd958ab3b4d7b987b41f38");
        registerActivityLifecycleCallbacks(JActivityManager.getActivityLifecycleCallbacks());
    }

    private void initLocalUserData() {
        LocalUserDataModel data = new LocalUserDataModel();
        data.setSignName("null");
        data.setUserImg("null");
        data.setUserName("null");
        data.setUid(0);
        data.setLogin(false);
        BaseUtils.saveLocalUser(App.this, data);
    }
}
