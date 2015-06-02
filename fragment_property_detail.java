package com.kchen.chrometopia;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
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
    private TextView mROI;
    private TextView mPrice;
    private TextView mListingAgent;
    private TextView mListingOffice;
    private TextView mImageCounter;

    private float mMortage;
    private float mPropertyTax;
    private float mPropertyManagement;
    private float mInsurance;

    private double mGrossIncome;

    private ViewPager mImageViewPager;

    private PieChart mPiechart;
    private LineChart mLineChart;

    private LruCache<String, Bitmap> mMemoryCache;
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
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
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

        mROI = (TextView) view.findViewById(R.id.property_detail_roi);
        mPrice = (TextView) view.findViewById(R.id.property_detail_price);
        mListingAgent = (TextView) view.findViewById(R.id.property_detail_listing_agent);
        mListingOffice = (TextView) view.findViewById(R.id.property_detail_listing_office);

        mPiechart = (PieChart)view.findViewById(R.id.property_detail_pie_chart);
        mLineChart = (LineChart)view.findViewById(R.id.property_detail_line_chart);
        mImageViewPager = (ViewPager)view.findViewById(R.id.property_detail_image_pager);
        mImageCounter = (TextView)view.findViewById(R.id.property_detail_image_counter);

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

                NumberFormat percentFormat = NumberFormat.getPercentInstance();
                percentFormat.setMaximumFractionDigits(2);
                double roi = item.getDouble("ROI");
                if (roi > 0){
                    mROI.setText(percentFormat.format(roi));
                }

                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                double price = item.getDouble("ListPrice");
                mPrice.setText(currencyFormat.format(price));

                String listAgent = item.getString("ListingAgentFirstName") + " "
                        + item.getString("ListingAgentLastName");
                mListingAgent.setText(listAgent);

                mListingOffice.setText(item.getString("ListingOffice"));

                mMortage = (float)item.getDouble("Mortage");
                mPropertyTax = (float) item.getDouble("PropertyTax");
                mPropertyManagement = (float)item.getDouble("PropertyManagement");
                mInsurance = (float) item.getDouble("Insurance");

                JSONArray images = (JSONArray) item.get("MediaURLs");
                if (images.length()> 0){
                    //new DownloadImageTask(mImageView).execute(images.get(0).toString());
                    ArrayList<String> imageUrls = new ArrayList<>();
                    for (int i=0; i<images.length(); i++){
                        imageUrls.add(images.getString(i));
                    }
                    ImagePagerAdapter adapter = new ImagePagerAdapter(getActivity().getApplicationContext(), imageUrls);
                    mImageViewPager.setAdapter(adapter);
                }
                ArrayList<Entry> entries = new ArrayList<Entry>();
                entries.add(new Entry(mMortage, 0));
                entries.add(new Entry(mInsurance, 1));
                entries.add(new Entry(mPropertyTax, 2));
                entries.add(new Entry(mPropertyManagement, 3));

                PieDataSet dataSet = new PieDataSet(entries, "Expenses");

                int colors[] = {Color.parseColor("#466A80"),Color.parseColor("#0078CA"),Color.parseColor("#5BC2E7"),Color.parseColor("#99E4FF")};
                dataSet.setColors(colors);


                PieData pieData = new PieData(new String[] {"Mortgage", "Insurance","Property Tax", "Property Mngt"},
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


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String urldisplay;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            urldisplay = urls[0];
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
            mMemoryCache.put(urldisplay, result);
        }
    }

    private class ImagePagerAdapter extends android.support.v4.view.PagerAdapter {
        private Context mContext;
        private ArrayList<String> mImageUrls;

        public ImagePagerAdapter (Context context, ArrayList<String> imagesUrls){
            mContext = context;
            mImageUrls = imagesUrls;
        }
        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            int padding = 2;
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ViewGroup.LayoutParams layout  = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
           // imageView.setLayoutParams(layout);
            String url = mImageUrls.get(position);
            Picasso.with(mContext)
                    .load(url)
                  .placeholder(R.drawable.placeholder)
//                    .error(R.drawable.user_placeholder_error)
                    .into(imageView);
//            Bitmap image = mMemoryCache.get(url);
//            if (image != null){
//                imageView.setImageBitmap(image);
//            }else {
//                new DownloadImageTask(imageView).execute(url);
//            }

            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mImageCounter.setText(String.format("%d/%d", position+1, getCount()));
        }
    };

}
