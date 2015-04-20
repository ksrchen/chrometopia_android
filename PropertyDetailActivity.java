package com.kchen.chrometopia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by kchen on 4/17/2015.
 */
public class PropertyDetailActivity extends SingleFragmentActivity {
    @Override
    protected Fragment CreateFragment() {
        Intent i = getIntent();
        return fragment_property_detail.newInstance(i.getStringExtra(fragment_property_detail.ARG_MLS_NUMBER));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getActionBar().setTitle("Property Details");

    }


}
