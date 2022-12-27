package it.itsar.fooding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

public class FilterItemAdapter extends ArrayAdapter<Integer> {

    private List<Integer> items;
    private Context context;

    public FilterItemAdapter(Context context, int resourceId, List<Integer> objects) {
        super(context, resourceId, objects);
        this.items = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.filter_spinner_item, parent, false);
        ImageView icon = row.findViewById(R.id.spinnerItemImage);
        icon.setImageResource(items.get(position));

        return row;
    }
}
