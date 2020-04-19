package gcu.mpd.mtq2020.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import gcu.mpd.mtq2020.DirectionHelpers.FetchURL;
import gcu.mpd.mtq2020.DirectionHelpers.TaskLoadedCallback;
import gcu.mpd.mtq2020.R;

public class RoutesFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

    private RoutesViewModel routesViewModel;
    private static final String TAG = "RoutesFragment";
    private PlacesClient placesClient;
    private LatLng start = null;
    private LatLng destination = null;
    private GoogleMap mMap;
    private Polyline currentPolyline;
    private Button showResult;
    private MarkerOptions place1, place2;

    List<MarkerOptions> markerOptionsList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.google_maps_key), Locale.UK);
        }

        placesClient = Places.createClient(Objects.requireNonNull(getContext()));

        routesViewModel =
                ViewModelProviders.of(this).get(RoutesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_routes, container, false);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_from);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("UK");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                start = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_to);

        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment2.setCountry("UK");
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                destination = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        showResult = root.findViewById(R.id.btnShowResults);
        showResult.setOnClickListener(onClickListener);


        start = new LatLng(55.8642, -4.2518);
        destination = new LatLng(55.9533, -3.1883);

        place1 = new MarkerOptions().position(start);
        place2 = new MarkerOptions().position(destination);

        markerOptionsList.add(place1);
        markerOptionsList.add(place2);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }


    private Button.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: check that start and destination values are not null
                    new FetchURL(RoutesFragment.this)
                            .execute(getUrl(start, destination, "driving"), "driving");
                }
            };

    private String getUrl(LatLng start, LatLng dest, String transportType) {
        String str_start = "origin=" + start.latitude + "," + start.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String transport_type = "travelmode=" + transportType;

        String parameter = str_start + "&" + str_dest;

        String url = getString(R.string.maps_direction_url) + parameter + "&key="
                + getString(R.string.google_maps_key);

        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(place1);
        mMap.addMarker(place2);
        showAllMarkers();
    }

    private void showAllMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (MarkerOptions m : markerOptionsList) {
            builder.include(m.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();

        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        LatLng onPath = new LatLng(55.873202, -3.655323);
        final boolean locationOnPath = PolyUtil.isLocationOnPath(onPath, currentPolyline.getPoints(), true, 10);

        if (locationOnPath) {
            MarkerOptions event = new MarkerOptions().position(onPath);
            mMap.addMarker(event);
        }
    }
}