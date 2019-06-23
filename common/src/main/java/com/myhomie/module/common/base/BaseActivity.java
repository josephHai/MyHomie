package com.myhomie.module.common.base;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.myhomie.module.common.MyDBHelper;

/**
 * <p>Activity基类 </p>
 *
 * @author 2019/5/29
 * @version V1.0.0
 */
@Keep
public abstract class BaseActivity extends AppCompatActivity {
    private MyDBHelper dbHelper;
    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDBHelper(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public final MyDBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(MyDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
}
