package debug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myhomie.module.common.base.BaseApplication;
import com.myhomie.module.common.http.HttpClient;
import com.myhomie.module.common.http.OnResultListener;
import com.orhanobut.logger.Logger;

public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // login();
    }

    private void login() {
        String data = "{\"username\": \"15869027957\", \"password\": \"jiejunyan\"}";
        HttpClient client = new HttpClient.Builder()
                .baseUrl("http://myhomie.chinaxueyun.com/info_platform/public/index.php/")
                .url("login/index/login")
                .data(data)
                .build();
        client.post(new OnResultListener<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                JSONObject json = JSONObject.parseObject(result);
                System.out.println(json.getString("msg"));
            }

            @Override
            public void onError(int code, String message) {
                Logger.e(message);
            }

            @Override
            public void onFailure(String message) {
                Logger.e(message);
            }
        });
    }
}
