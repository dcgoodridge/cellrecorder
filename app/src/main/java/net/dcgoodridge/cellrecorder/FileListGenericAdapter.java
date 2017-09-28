package net.dcgoodridge.cellrecorder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileListGenericAdapter extends ArrayAdapter<File> {

    private SimpleDateFormat simpleDateFormat;
    private PrettyTime prettyTime;

    public FileListGenericAdapter(Context context, List<File> objects) {
        super(context, 0, objects);
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        prettyTime = new PrettyTime();
    }

    private String computeFileNameWithoutExtension(File file) {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }

    private String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File file = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filelistgeneric, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.item_filelistgeneric_name);
        TextView tvDate = (TextView) convertView.findViewById(R.id.item_filelistgeneric_date);
        TextView tvSize = (TextView) convertView.findViewById(R.id.item_filelistgeneric_size);


        long dateModifiedTimestamp = file.lastModified();
        Date modifiedDate = new Date(dateModifiedTimestamp);
        String dateModifiedString = simpleDateFormat.format(modifiedDate);
        String dateModifiedStringPretty = prettyTime.format(modifiedDate);

        tvName.setText(file.getName());
        tvDate.setText(dateModifiedString + " - " + dateModifiedStringPretty);

        tvSize.setText(humanReadableByteCount(file.length(), true));

        return convertView;
    }
}
