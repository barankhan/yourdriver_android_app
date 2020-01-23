package com.baran.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baran.driver.Model.DriverType;
import com.baran.driver.R;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DriverTypeSpinnerAdapter extends ArrayAdapter<List<DriverType>> {
    String[] spinnerTitles;
    int[] spinnerImages;
    Context mContext;
    List<DriverType> driverTypeList;
    public DriverTypeSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,List<DriverType> driverTypeList) {
        super(context, resource);
//        this.spinnerTitles = titles;
//        this.spinnerImages = images;
        this.driverTypeList = driverTypeList;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return driverTypeList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.driver_type_spiner_layout, parent, false);
            mViewHolder.mFlag =  convertView.findViewById(R.id.tvDriverTypeImg);
            mViewHolder.mName =  convertView.findViewById(R.id.tvDriverTypeRow);
            mViewHolder.mExpectedFare = convertView.findViewById(R.id.tv_expected_fare);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(driverTypeList.get(position).getImageResourceId());
        mViewHolder.mName.setText(driverTypeList.get(position).getTitle());
        mViewHolder.mExpectedFare.setText(driverTypeList.get(position).getExpectedFare());


        return convertView;
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
        TextView mExpectedFare;
    }

}
