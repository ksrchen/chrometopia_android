package com.kchen.chrometopia;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SECTION_NUMBER = "SECTION_NUMBER";
    private  int mSectionNumber;
    private MenuItem mSearchItem;
    private  MenuItem mListItem;
    private  fragment_map_search mMapSearchFragment;
    private PropertyListFragment mPropertyListFragment;
    private Fragment mCurrentFragment;
    private SearchFilterFragment mFilterFragment;

    private FrameLayout mListContainer;
    private FrameLayout mFilterContainer;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter 1.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(int sectionNumber) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(SECTION_NUMBER);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search, container, false);
        mListContainer = (FrameLayout) v.findViewById(R.id.search_fragment_list_container);
        mFilterContainer = (FrameLayout) v.findViewById(R.id.search_fragment_filter_container);

        mPropertyListFragment = PropertyListFragment.newInstance("", "");
        mFilterFragment = SearchFilterFragment.newInstance();
        mMapSearchFragment = fragment_map_search.newInstance(1);
        mMapSearchFragment.setmPropertyListFragment(mPropertyListFragment);

        mFilterFragment.setListener(mMapSearchFragment);

        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction()
                .add(R.id.search_fragment_container, mMapSearchFragment)
                .add(R.id.search_fragment_list_container, mPropertyListFragment)
                .add(R.id.search_fragment_filter_container, mFilterFragment)
                .commit();
        mCurrentFragment = mMapSearchFragment;
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mapsearch, menu);

        mListItem = menu.findItem(R.id.menu_item_map_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mSearchItem = menu.findItem(R.id.menu_item_search);
            SearchView searchView = (SearchView)mSearchItem.getActionView();
            SearchManager searchManager = (SearchManager)getActivity()
                    .getSystemService(Context.SEARCH_SERVICE);
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(
                    getActivity().getComponentName());
            searchView.setSearchableInfo(searchableInfo);
            searchView.setIconifiedByDefault(false);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_map_list){
//            if (mCurrentFragment == mMapSearchFragment){
//                mListItem.setTitle("Map");
//                FragmentManager fm = getChildFragmentManager();
//                fm.beginTransaction()
//                        .setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out)
//                        .replace(R.id.search_fragment_container, mPropertyListFragment)
//                       // .addToBackStack(null)
//                        .commit();
//                mCurrentFragment = mPropertyListFragment;
//            }else{
//                mListItem.setTitle("List");
//                FragmentManager fm = getChildFragmentManager();
//                fm.beginTransaction()
//                        .setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out)
//                        .replace(R.id.search_fragment_container, mMapSearchFragment)
//                        .commit();
//               // fm.popBackStack();
//                mCurrentFragment = mMapSearchFragment;
//            }
            if (mListContainer.getVisibility() == View.GONE ){
                mFilterContainer.setVisibility(View.GONE);
                mListContainer.setVisibility(View.VISIBLE);
            }else
            {
                mListContainer.setVisibility(View.GONE);
            }
            return true;
        }else if (item.getItemId() == R.id.menu_item_map_filter) {
            if (mFilterContainer.getVisibility() == View.GONE ){
                mListContainer.setVisibility(View.GONE);
                mFilterContainer.setVisibility(View.VISIBLE);
            }else
            {
                mFilterContainer.setVisibility(View.GONE);
            }

        }else if (item.getItemId() == R.id.menu_item_current_location){
            SearchView searchView = (SearchView)mSearchItem.getActionView();
            searchView.setQuery("", false);
            searchView.clearFocus();
            mMapSearchFragment.zoomToCurrentLocation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void zoomToRegion(String region){
        mMapSearchFragment.zoomToRegion(region);
    }
}
