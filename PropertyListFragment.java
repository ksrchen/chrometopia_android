package com.kchen.chrometopia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.kchen.chrometopia.dummy.DummyContent;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class PropertyListFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static PropertyListFragment newInstance(String param1, String param2) {
        PropertyListFragment fragment = new PropertyListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PropertyListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setListAdapter(new PropertyListAdapter(PropertyStore.getInstance(getActivity()).getProperties()));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateUI() {
        ArrayAdapter<Property> adapter = (ArrayAdapter<Property>) getListAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Property p = PropertyStore.getInstance(getActivity()).get(position);
        Intent i = new Intent(getActivity(), PropertyDetailActivity.class);
        i.putExtra(fragment_property_detail.ARG_MLS_NUMBER, p.getMLSNumber());
        startActivity(i);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    private class PropertyListAdapter extends ArrayAdapter<Property> {
        public PropertyListAdapter(ArrayList<Property> properties){
            super(getActivity(), 0, properties);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.propety_list_item
                        , null);
            }

            Property c = getItem(position);

            ((TextView) convertView.findViewById(R.id.property_list_item_mls_number)).setText("ML No:" + c.getMLSNumber());
            ((TextView) convertView.findViewById(R.id.property_list_item_address)).setText(c.getAddress());
            ((TextView) convertView.findViewById(R.id.property_list_item_roi)).setText("ROI: " + NumberFormat.getPercentInstance().format(c.getROI()));
            ((TextView) convertView.findViewById(R.id.property_list_item_price)).setText("Price: " + NumberFormat.getCurrencyInstance().format(c.getPrice()));

            String url = c.getMediaUrl();
            if (url.length()> 0) {
                ImageView imageView = (ImageView) convertView.findViewById(R.id.property_list_item_image);
                new DownloadImageTask(imageView).execute(c.getMediaUrl());
            }
            return convertView;
        }

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
