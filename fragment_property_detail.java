package com.kchen.chrometopia;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link fragment_property_detail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_property_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_property_detail extends Fragment {
    public static final String ARG_MLS_NUMBER = "MLSNumber";
    private String mMLSNumber;


    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mLSNumber Parameter 1.
     * @return A new instance of fragment fragment_property_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_property_detail newInstance(String mLSNumber) {
        fragment_property_detail fragment = new fragment_property_detail();
        Bundle args = new Bundle();
        args.putString(ARG_MLS_NUMBER, mLSNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public fragment_property_detail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMLSNumber = getArguments().getString(ARG_MLS_NUMBER);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_property_detail, container, false);

       // getActivity().getActionBar().setTitle("Property Details");
        TextView text = (TextView) view.findViewById(R.id.MLSNumber);
        text.setText(mMLSNumber);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.propertydetail, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        public void onFragmentInteraction(Uri uri);
    }



}
