package com.middleendien.middrides.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.middleendien.middrides.R;
import com.middleendien.middrides.models.Location;
import com.middleendien.middrides.utils.Synchronizer;
import com.parse.ParseObject;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nosagie on 10/23/15.
 *
 * This dialog presents a list of stops for the user to choose from
 *
 * now reads from local storage
 */
public class LocationSelectDialogFragment extends DialogFragment {


    //List of MiddRide stops
    private ArrayList<Location> locationList = new ArrayList<Location>();

    private ListView locationsListView;

    private static final int LOCATION_UPDATE_FROM_LOCAL_REQUEST_CODE        = 0x011;

    ArrayAdapter<Location> locationsArrayListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_select_location_dialog_fragment, container, false);
        getDialog().setTitle(getString(R.string.dialog_select_location));

        initData();

        initView(rootView);

        initEvent();

        return rootView;
    }

    private void initView(View rootView) {
        //Initialize buttons and list view
        locationsListView = (ListView) rootView.findViewById(R.id.dialogLocationListView);
    }

    private void initData(){
        Synchronizer synchronizer = Synchronizer.getInstance(getActivity());
        synchronizer.getListObjectsLocal(getString(R.string.parse_class_locaton), LOCATION_UPDATE_FROM_LOCAL_REQUEST_CODE);
    }

    public void updateLocations(List<ParseObject> objectList) {
        locationList.clear();
        for (ParseObject obj : objectList) {
            locationList.add(new Location(obj.getString(getString(R.string.parse_location_name)),
                    obj.getDouble(getString(R.string.parse_location_lat)),
                    obj.getDouble(getString(R.string.parse_location_lng)))
                    .setObjectId(obj.getObjectId()));
        }
        locationsArrayListAdapter.notifyDataSetChanged();
    }

    // configures adapter and sets item click listener for List
    private void initEvent() {
        locationsArrayListAdapter = new ArrayAdapter<Location>
                (getActivity(), android.R.layout.simple_list_item_activated_1, locationList);

        locationsListView.setAdapter(locationsArrayListAdapter);

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location locationSelected = (Location) locationsListView.getItemAtPosition(position);

                // return selected Location to caller Activity and dismiss dialog
                SelectLocationDialogListener callback = (SelectLocationDialogListener) getActivity();
                callback.onLocationSelected(locationSelected);

                dismiss();
            }
        });
    }

    //Interface for caller activity
    public interface SelectLocationDialogListener {

        void onLocationSelected(Location locationSelected);

    }
}