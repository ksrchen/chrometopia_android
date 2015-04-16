package com.kchen.chrometopia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import android.location.Location;
import android.widget.Button;
import android.widget.FrameLayout;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_map_search.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_map_search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_map_search extends Fragment {
    //ODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_SECTION_NUMBER = "section_number";
    private GoogleMap mMap;

    // TODO: Rename and change types of parameters
    private int mSectionNumber;
    private String mParam2;
    Boolean mInDrawMode = true;
    Projection projection;
    public double latitude;
    public double longitude;
    private ArrayList<LatLng> mPoints = new ArrayList<LatLng>();
    private Polygon mPolygon;

    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment fragment_map_search.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_map_search newInstance(int sectionNumber) {
        fragment_map_search fragment = new fragment_map_search();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    public fragment_map_search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v =  super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_fragment_map_search, container, false);

        FrameLayout fram_map = (FrameLayout) v.findViewById(R.id.fram_map);
        Button btn_draw_State = (Button) v.findViewById(R.id.btn_draw_State);
        mInDrawMode = false; // to detect map is movable

        btn_draw_State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInDrawMode = !mInDrawMode;
                mPoints.clear();
                if (mPolygon!=null){
                    mPolygon.remove();
                    mPolygon = null;
                }
            }
        });

        Button clearButton = (Button) v.findViewById(R.id.btn_clear);
        clearButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPoints.clear();
                if (mPolygon!=null){
                    mPolygon.remove();
                    mPolygon = null;
                }
                mInDrawMode = false;
                new DataLoader().execute("");
            }
        });

        fram_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (!mInDrawMode) {
                    return false;
                }

                float x = event.getX();
                float y = event.getY();

                int x_co = Math.round(x);
                int y_co = Math.round(y);

                projection = mMap.getProjection();
                Point x_y_points = new Point(x_co, y_co);

                LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                latitude = latLng.latitude;

                longitude = latLng.longitude;

                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        // finger touches the screen
                        mPoints.add(new LatLng(latitude, longitude));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // finger moves on the screen
                        mPoints.add(new LatLng(latitude, longitude));
                        Draw_Map();
                        break;
                    case MotionEvent.ACTION_UP:
                        // finger leaves the screen
                        Draw_Map();
                        Zoom2Fit();
                        mInDrawMode = false;
                        new DataLoader().execute("");
                        break;
                }

                return true;

            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
//                    .zoom(12)                   // Sets the zoom
//                    .bearing(0)                // Sets the orientation of the camera to east
//                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
//                    .build();                   // Creates a CameraPosition from the builder
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                new DataLoader().execute("");
            }
        });
        return v;
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
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        //DataLoader loader = new DataLoader();
        //loader.execute("http://yahoo.com");


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void Draw_Map() {
        if (mPolygon != null){
            mPolygon.remove();
            mPolygon = null;
        }
        if (mPoints.size() > 0) {
            PolygonOptions rectOptions = new PolygonOptions();
            rectOptions.addAll(mPoints);
            rectOptions.strokeColor(Color.MAGENTA);
            rectOptions.strokeWidth(5);
            rectOptions.fillColor(Color.argb(50, 255, 255, 0));
            mPolygon = mMap.addPolygon(rectOptions);
        }
    }
    private  void Zoom2Fit(){
        if (mPolygon != null){
            LatLngBounds.Builder b = new LatLngBounds.Builder();
            for(LatLng p : mPolygon.getPoints()) b.include(p);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 25));
        }
    }

    private class DataLoader extends AsyncTask<String, Integer, String>{
        private String mWellknownPolygon;
        private String mFilters ="";

        @Override
        protected void onPreExecute() {

            for (Marker m : mMarkers){
                m.remove();
            }
            mMarkers.clear();

            List<LatLng> points = new ArrayList<LatLng>();
            if (mPolygon != null){

                points = mPolygon.getPoints();
            }else {
                VisibleRegion region = mMap.getProjection().getVisibleRegion();
                points.add(region.nearLeft);
                points.add(region.nearRight);
                points.add(region.farRight);
                points.add(region.farLeft);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("POLYGON((");
            for(LatLng p: points){
                sb.append(p.longitude + " " + p.latitude +", ");
            }
            LatLng p = points.get(0);
            sb.append(p.longitude + " " + p.latitude);

            sb.append("))");
            mWellknownPolygon = sb.toString();
            Log.i("mWellknownPolygon", mWellknownPolygon);

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.i("Polygon=", mWellknownPolygon);
                URL url = new URL ("http://kmlservice.azurewebsites.net/api/resincome");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.connect();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("Polygon", mWellknownPolygon);
                jsonParam.put("Filters", mFilters);

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
              //  writer.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();


                int status = connection.getResponseCode();
                String s = connection.getResponseMessage();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null)
                {
                    sb.append(line);
                }

                return sb.toString();
            }catch (Exception exp) {
                Log.e("", exp.getMessage());
                return exp.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            Log.i("onPostExecute", result);


            try {
                JSONArray json = new JSONArray(result);
                for (int i=0; i<json.length(); i++){
                    JSONObject item = json.getJSONObject(i);
                    double lat = item.getDouble("Latitude");
                    double lon = item.getDouble("longitude");

                    MarkerOptions maker = new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                    mMarkers.add(mMap.addMarker(maker));
                }
                Log.i("properties=", String.format("%d", mMarkers.size()));

            }catch (Exception exp){
                Log.e("", exp.getLocalizedMessage());
            }

        }

        private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    };
}
