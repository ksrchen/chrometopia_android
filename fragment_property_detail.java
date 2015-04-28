package com.kchen.chrometopia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.AnimationEasing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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


    private TextView mAddressTextView;
    private TextView mPropertyDescription;

    private PieChart mPiechart;
    private LineChart mLineChart;

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
        TextView text = (TextView) view.findViewById(R.id.property_detail_mlsnumber);
        text.setText(mMLSNumber);

        mAddressTextView = (TextView) view.findViewById(R.id.property_detail_address);
        mPropertyDescription = (TextView) view.findViewById(R.id.property_detail_property_description);

        mPiechart = (PieChart)view.findViewById(R.id.property_detail_pie_chart);
        mLineChart = (LineChart)view.findViewById(R.id.property_detail_line_chart);

        new DataLoader().execute("");
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

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");

        MenuItem item =  menu.findItem(R.id.menu_item_share);
        ShareActionProvider provider  = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        provider.setShareIntent(sendIntent);

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


    private class DataLoader extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("http://kmlservice.azurewebsites.net/api/resincome/" + mMLSNumber);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                connection.connect();

                int status = connection.getResponseCode();
                String s = connection.getResponseMessage();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            } catch (Exception exp) {
                Log.e("", exp.getMessage());
                return exp.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject item = new JSONObject(s);
                String streetNumber = item.getString("StreetNumber");
                String streetName = item.getString("StreetName");
                String city = item.getString("City");
                String state = item.getString("State");
                String postalCode = item.getString("PostalCode");
                String address = String.format("%s %s %s, %s %s",
                        streetNumber,
                        streetName,
                        city,
                        state,
                        postalCode);

                mAddressTextView.setText(address);
                mPropertyDescription.setText(item.getString("PropertyDescription"));

                ArrayList<Entry> entries = new ArrayList<Entry>();
                entries.add(new Entry(2300f, 0));
                entries.add(new Entry(300f, 1));
                entries.add(new Entry(100f, 2));
                PieDataSet dataSet = new PieDataSet(entries, "Expenses");

                int colors[] = {Color.parseColor("#466A80"),Color.parseColor("#0078CA"),Color.parseColor("#5BC2E7"),Color.parseColor("#99E4FF")};
                dataSet.setColors(colors);


                PieData pieData = new PieData(new String[] {"Mortgage", "Insurance", "Property Mngt"},
                        dataSet);

                mPiechart.setUsePercentValues(true);
                mPiechart.setDrawSliceText(true);
                mPiechart.setDescription("");
                mPiechart.getLegend().setEnabled(false);
                mPiechart.setData(pieData);
                mPiechart.animateX(2000, AnimationEasing.EasingOption.Linear);

                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

                ArrayList<Entry> incomeValues = new ArrayList<Entry>();
                incomeValues.add(new Entry(20000, 0));
                incomeValues.add(new Entry(30000, 1));
                incomeValues.add(new Entry(50000, 2));
                incomeValues.add(new Entry(90000, 3));
                LineDataSet incomeDataSet = new LineDataSet(incomeValues, "Income");
                incomeDataSet.setColor(Color.parseColor("#00ff00"));
                dataSets.add(incomeDataSet);

                ArrayList<Entry> expenseValues = new ArrayList<Entry>();
                expenseValues.add(new Entry(10000, 0));
                expenseValues.add(new Entry(15000, 1));
                expenseValues.add(new Entry(20000, 2));
                expenseValues.add(new Entry(25000, 3));
                LineDataSet expenseDataSet = new LineDataSet(expenseValues, "Expense");
                expenseDataSet.setColor(Color.parseColor("#ff0000"));
                dataSets.add(expenseDataSet);

                ArrayList<String> xVals = new ArrayList<String>();
                xVals.add("1 year"); xVals.add("5 years"); xVals.add("15 years"); xVals.add("30 years");


                LineData data = new LineData(xVals, dataSets);
                mLineChart.setData(data);
                mLineChart.setDescription("");
                mLineChart.getXAxis().setDrawAxisLine(true);
                mLineChart.getXAxis().setDrawGridLines(false);
                mLineChart.getXAxis().setAxisLineWidth(2f);
                mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                mLineChart.getXAxis().setAdjustXLabels(true);
                mLineChart.getXAxis().setAdjustXLabels(true);


                mLineChart.getAxis(YAxis.AxisDependency.LEFT).setDrawGridLines(false);
                mLineChart.getAxis(YAxis.AxisDependency.LEFT).setAxisLineWidth(2f);
                mLineChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawGridLines(false);
                mLineChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawAxisLine(false);
                mLineChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawLabels(false);

                mLineChart.animateXY(2000, 2000);

            } catch (Exception exp) {
                Log.e("", exp.getLocalizedMessage());
            }
        }

    }

}
