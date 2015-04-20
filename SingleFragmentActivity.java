package com.kchen.chrometopia;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by kchen on 4/17/2015.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {
    protected abstract Fragment CreateFragment();
    protected int getLayoutResId() {
        return R.layout.activity_fragement;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragmentContainer) ;
        if (fragment == null){
            fragment = CreateFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }
}
