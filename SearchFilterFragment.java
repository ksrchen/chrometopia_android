package com.kchen.chrometopia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFilterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SeekBar mLotSize;
    private SeekBar mGrossOperatingIncome;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment SearchFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFilterFragment newInstance() {
        SearchFilterFragment fragment = new SearchFilterFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public SearchFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search_filter, container, false);

        mLotSize = (SeekBar) v.findViewById(R.id.fragment_search_filter_lotsize);
        final TextView lotSizeDisplay = (TextView) v.findViewById(R.id.fragment_search_filter_lotsize_display);

        mLotSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lotSizeDisplay.setText(NumberFormat.getInstance().format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mGrossOperatingIncome = (SeekBar) v.findViewById(R.id.fragment_search_filter_gross_operating_income);
        final TextView grossOperatingIncomeDisplay = (TextView) v.findViewById(R.id.fragment_search_filter_gross_operating_income_display);

        mGrossOperatingIncome.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                grossOperatingIncomeDisplay.setText(NumberFormat.getCurrencyInstance().format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button apply = (Button) v.findViewById(R.id.fragment_search_filter_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    String filters = String.format("LotSquareFootage > %d AND GrossOperatingIncome > %d",
                            mLotSize.getProgress(), mGrossOperatingIncome.getProgress());
                    mListener.onApplyFilter(filters);
                }
            }
        });

        Button reset = (Button) v.findViewById(R.id.fragment_search_filter_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLotSize.setProgress(0);
                mGrossOperatingIncome.setProgress(0);
                if (mListener != null){
                    mListener.OnResetFilter();
                }
            }
        });


        return v;


    }

    public void setListener(OnFragmentInteractionListener listener) {
        this.mListener = listener;
    }


    public interface OnFragmentInteractionListener {
        public void onApplyFilter(String filter);
        public void OnResetFilter();
    }

}
