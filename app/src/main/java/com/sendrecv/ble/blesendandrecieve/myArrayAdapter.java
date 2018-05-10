package com.sendrecv.ble.blesendandrecieve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class myArrayAdapter extends ArrayAdapter<Device> {
    private Context context;
    private List<Device> list = new ArrayList<Device>();

    public myArrayAdapter(Context context, ArrayList<Device> list)
    {
        super(context,0,list);
        this.context=context;
        this.list=list;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.device_list,   parent, false);
        TextView deviceName = (TextView) rowView.findViewById(R.id.device_name);
        TextView deviceRssi = (TextView)rowView.findViewById(R.id.device_rssi);
        TextView deviceMac = (TextView)rowView.findViewById(R.id.device_mac);
        Device currentDevice=list.get(position);
        deviceName.setText(currentDevice.getName());
        deviceRssi.setText(String.valueOf(currentDevice.getRSSI()));
        deviceMac.setText(currentDevice.getMac());
        return rowView;
    }
}
