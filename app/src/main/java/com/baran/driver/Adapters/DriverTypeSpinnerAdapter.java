package com.baran.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baran.driver.R;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DriverTypeSpinnerAdapter extends ArrayAdapter<String> {
    String[] spinnerTitles;
    int[] spinnerImages;
    Context mContext;
    public DriverTypeSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,String[] titles, int[] images) {
        super(context, resource);
        this.spinnerTitles = titles;
        this.spinnerImages = images;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.driver_type_spiner_layout, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.tvDriverTypeImg);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.tvDriverTypeRow);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(spinnerImages[position]);
        mViewHolder.mName.setText(spinnerTitles[position]);


        return convertView;
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
    }

}
