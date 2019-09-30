package com.masthuggis.boki.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SearchFragment extends Fragment implements Filterable {

    private List<Advertisement> advertisements; //TODO make sure this is copy of Local adList
    private View view;
    private SearchView searchField;

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchField = view.findViewById(R.id.searchField);
        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false; //not relevant since we want real-time updates
            }

            @Override
            public boolean onQueryTextChange(String query) { //gives real-time updates
                getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.search_fragment, container, false);

        return inflater.inflate(R.layout.search_fragment, container, false);
    }


    @Override
    public Filter getFilter() {
        return null;
    }

    private Filter advertFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Advertisement> filteredList = new ArrayList<>();
            Iterator<Advertisement> iterator = advertisements.iterator();
            if (constraint == null || constraint.length() == 0)
                filteredList.addAll(advertisements); //No filter applied, all adverts to be shown
            else {
                String filterPattern = constraint.toString().toLowerCase().trim(); //Consistent string
                while (iterator.hasNext()) {
                    Advertisement ad = iterator.next();
                    if (ad.getTitle().toLowerCase().contains(filterPattern));
                        filteredList.add(ad);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        };

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            advertisements.clear();
            advertisements.addAll((List)filterResults.values); //necessary cast
        }
    };
}
