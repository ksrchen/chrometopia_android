package com.kchen.chrometopia;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kchen on 4/24/2015.
 */
public class PropertyStore {

    private static PropertyStore ourInstance;
    private Context mAppContext;
    private ArrayList<Property> mProperties = new ArrayList<Property>();

    private PropertyStore(Context appContext) {
        mAppContext = appContext;
    }

    public static PropertyStore getInstance(Context c) {
        if (ourInstance == null) ourInstance = new PropertyStore(c);
        return ourInstance;
    }

    public void addProperty(Property p){
        mProperties.add(p);
    }

    public ArrayList<Property> getProperties(){
        return mProperties;
    }

    public void clear(){
        mProperties.clear();
    }

    public Property get(int position){
        return mProperties.get(position);
    }
}
