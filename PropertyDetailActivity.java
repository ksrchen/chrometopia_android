package com.kchen.chrometopia;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by kchen on 4/17/2015.
 */
public class PropertyDetailActivity extends SingleFragmentActivity {
    @Override
    protected Fragment CreateFragment() {
        return fragment_property_detail.newInstance("123");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getActionBar().setTitle("Property Details");

    }


}
