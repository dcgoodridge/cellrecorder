package net.dcgoodridge.cellrecorder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class CellrecorderListAdapter extends ArrayAdapter<String> {

    private static final int DEFAULT_ARRAY_SIZE = 100;
    public int textColor = Color.BLACK;
    private int totalAdded = 0;
    private CircularArray<String> buffer = new CircularArray<>(DEFAULT_ARRAY_SIZE);

    public CellrecorderListAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
    }

    @Override
    public String getItem(int position) {
        int index = position % buffer.capacity();
        return buffer.get(index);
    }

    @Override
    public void add(String object) {
        buffer.add(object);
        totalAdded++;
    }

    @Override
    public int getCount() {
        return totalAdded;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String nmea = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_scrollinglog, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.nmea = (TextView) convertView.findViewById(R.id.item_nmea_nmea);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.nmea.setText(nmea);
        holder.nmea.setTextColor(textColor);
        return convertView;
    }

    static class ViewHolder {
        TextView nmea;
    }

}
