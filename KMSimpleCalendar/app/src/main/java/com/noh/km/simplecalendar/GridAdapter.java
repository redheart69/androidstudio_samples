package com.noh.km.simplecalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by nohkyoungmo on 2016. 9. 13..
 */

public class GridAdapter extends BaseAdapter {
    private ArrayList<CalendarData> mCalendarList = null;
    private Context mContext = null;
    GridAdapter(Context context) {
        mContext = context;
    }
    public void setCalData(ArrayList<CalendarData> calendarList){
        mCalendarList = calendarList;
    }
    @Override
    public int getCount() {

        return mCalendarList == null ? 0 : mCalendarList.size();
    }

    @Override
    public CalendarData getItem(int position) {
        return mCalendarList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
        }
        TextView tvDay = (TextView) convertView.findViewById(R.id.grid_calendar_day);
        TextView tvMemo = (TextView) convertView.findViewById(R.id.grid_calendar_memo);
        CalendarData calendarData = getItem(position);
        if(calendarData.getMonthType() == CalendarData.TYPE_CURRENT_MONTH) {
            int dayOfWeek = calendarData.getDayOfWeek();

            if(Calendar.SUNDAY == dayOfWeek) {
                tvDay.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
            } else if(Calendar.SATURDAY == dayOfWeek) {
                tvDay.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
            } else {
                tvDay.setTextColor(mContext.getResources().getColor(android.R.color.black));
            }
        } else {
            tvDay.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        }
        tvDay.setText(String.valueOf(calendarData.getDay()));
        HashSet<String> memoList = CalendarDAO.getInstance(mContext).getMemoDateList();
        if(memoList.contains(calendarData.getYear()+""+calendarData.getMonth()+""+calendarData.getDay())) {
            tvMemo.setVisibility(View.VISIBLE);
        } else {
            tvMemo.setVisibility(View.GONE);
        }
        return convertView;
    }
}
