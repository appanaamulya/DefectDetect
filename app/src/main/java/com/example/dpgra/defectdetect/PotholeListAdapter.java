package com.example.dpgra.defectdetect;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import animations.DeleteAnimation;
import animations.SlidingSpringAnimation;
import model.Pothole;

/**
 * The adapter for the pothole list. Transforms each item in the pothole list into a view that can
 * be used by the list view.
 *
 * @author Daniel Pinson, Vamsi Yadav
 * @version 1.0
 */
public class PotholeListAdapter extends ArrayAdapter<Pothole> {

    private List<Pothole> list;
    private PotholeListFragment fragment;
    private TextView textView;

    /**
     * Constructor for the pothole list adapter.
     *
     * @param context context
     * @param list the list to create
     * @param fragment the fragment of the list view
     */
    public PotholeListAdapter(@NonNull Context context, List<Pothole> list, PotholeListFragment fragment) {
        super(context, 0, list);
        this.list = list;
        this.fragment = fragment;
        textView = null;
    }

    /**
     * Turns the items of a list into the view items of the pothole list.
     * @param position the position in the list
     * @param convertView the return view
     * @param parent the parent view group
     * @return the view to display
     */
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        if ( !list.isEmpty() ) {
            final Pothole pothole = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pothole, parent, false);
            Button button = convertView.findViewById(R.id.del_button);
            LinearLayout linearLayout = convertView.findViewById(R.id.liner_layout);
            SlidingSpringAnimation animation = new SlidingSpringAnimation(button, linearLayout, SlidingSpringAnimation.RIGHT_TO_LEFT, fragment, pothole);
            button.setOnClickListener(new DeleteAnimation((ListView) fragment.getView().findViewById(R.id.list_view), list, position));
            linearLayout.setOnTouchListener(animation);
            this.registerDataSetObserver(animation);
            // Lookup view for data population
            TextView header = (TextView) convertView.findViewById(R.id.pothole_id);
            TextView coords = (TextView) convertView.findViewById(R.id.coords);
            TextView severity = (TextView) convertView.findViewById(R.id.severity);
            // Populate the data into the template view using the data object
            header.setText(pothole.getId());
            Geocoder geocoder = new Geocoder(fragment.getContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(pothole.getLat(), pothole.getLon(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String address = addresses.get(0).getAddressLine(0);
            coords.setText(address);
            severity.setText("Severity: " + pothole.getSize());
            // Return the completed view to render on screen
        }
        return convertView;
    }
}
