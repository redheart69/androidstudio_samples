package com.shrudah.simplecalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Calendar mCurrentCalendal = Calendar.getInstance();
    private PlaceholderFragment mCalendarFragmentPre;
    private PlaceholderFragment mCalendarFragmentCurrent;
    private PlaceholderFragment mCalendarFragmentNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mViewPager.setCurrentItem(1);
    }

    private void initView(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mCalendarFragmentPre = PlaceholderFragment.newInstance(0);
        mCalendarFragmentCurrent = PlaceholderFragment.newInstance(1);
        mCalendarFragmentNext = PlaceholderFragment.newInstance(2);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("","onPageSelected=======>>"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("","onPageScrollStateChanged=======>>"+state);
                if(state == ViewPager.SCROLL_STATE_IDLE) {
                    if(mViewPager.getCurrentItem() == 0 || mViewPager.getCurrentItem() == 2) {
                        if(mViewPager.getCurrentItem() == 0) {
                            mCurrentCalendal.add(Calendar.MONTH,-1);
                        } else {
                            mCurrentCalendal.add(Calendar.MONTH,1);
                        }
                        Log.d("","onPageScrollStateChanged pFragment.getView()=======>>"+mCalendarFragmentCurrent.getView());
                        mCalendarFragmentCurrent.updateView();
                        mViewPager.setCurrentItem(1,false);
                        mCalendarFragmentPre.updateView();
                        mCalendarFragmentNext.updateView();
                    }
                }
            }
        });
    }
    private Calendar getNextCalendar(){
        Calendar nextCalendal = (Calendar) mCurrentCalendal.clone();
        nextCalendal.add(Calendar.MONTH,1);
        return nextCalendal;
    }
    private Calendar getPreCalendar(){
        Calendar preCalendal = (Calendar) mCurrentCalendal.clone();
        preCalendal.add(Calendar.MONTH,-1);
        return preCalendal;
    }
    private Calendar getCurrentCalendar(){
        return mCurrentCalendal;
    }

    public void nextMonth(){
        mViewPager.setCurrentItem(2);
    }
    public void preMonth(){
        mViewPager.setCurrentItem(0);
    }

    private ArrayList<CalendarData> getCalendarList(Calendar currentCalendar){
        if(currentCalendar == null) {
            return null;
        }
        //이번달을 날짜를 1일로 초기화
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        //이번달 1일의 요일
        int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int dayIndex = -(dayOfWeek - 1);
        ArrayList<CalendarData> calenarList = new ArrayList<>();
        while (true){
            Calendar calendar = (Calendar) currentCalendar.clone();
            calendar.add(Calendar.DAY_OF_MONTH,dayIndex);
            CalendarData calenarData = new CalendarData(calendar);
            Log.d("","onPageScrollStateChanged=======>>"+calenarData.getYear()+"/"+calenarData.getMonth()+"/"+calenarData.getDay()+", index :"+dayIndex);
            if(currentMonth != calendar.get(Calendar.MONTH)) {
                if(dayIndex > 0 && calenarList.size() % 7 == 0){
                    break;
                } else {
                    calenarData.setCurrentMonth(false);
                    calenarList.add(calenarData);
                }
            } else {
                calenarData.setCurrentMonth(true);
                calenarList.add(calenarData);
            }
            dayIndex++;
        }
        return calenarList;
    }
    public class CalendarData {
        private Calendar mCalendar;
        private boolean mIsCurrentMonth;

        CalendarData(Calendar calendar){
            if(calendar == null){
                mCalendar = Calendar.getInstance();
            } else {
                mCalendar = calendar;
            }
        }
        private int getDay(){
            //start value 1
            return mCalendar.get(Calendar.DAY_OF_MONTH);
        }

        private int getYear(){
            return mCalendar.get(Calendar.YEAR);
        }
        private int getMonth(){
            //start value 0
            return mCalendar.get(Calendar.MONTH) + 1;
        }
        private int getDayOfWeek(){
            return mCalendar.get(Calendar.DAY_OF_WEEK);
        }
        public boolean hasMemo(){
            return false;
        }
        public void setCurrentMonth(boolean isCurrentMonth){
            mIsCurrentMonth = isCurrentMonth;
        }
        private boolean isCurrentMonth(){
            return mIsCurrentMonth;
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return mCalendarFragmentPre;
                case 1:
                    return mCalendarFragmentCurrent;
                case 2:
                    return mCalendarFragmentNext;
            }
            return  mCalendarFragmentPre;
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "이전달";
                case 1:
                    return "요번달";
                case 2:
                    return "다음달";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public TextView tvDate;
        private GridAdapter calAdapter;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public void updateView(){
            Log.d("","updateView=======>>"+getArguments().getInt(ARG_SECTION_NUMBER)+", getView1:"+this.tvDate);
//            Log.d("","updateView=======>>"+getArguments().getInt(ARG_SECTION_NUMBER)+", getView2:"+this.mView1);
            Log.d("","updateView=======>>"+getArguments().getInt(ARG_SECTION_NUMBER)+", getView3:"+getView());
            Calendar calendar = null;
            String year = "";
            String month = "";
            final MainActivity mainActivity = (MainActivity)getActivity();
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 0:
                    calendar = mainActivity.getPreCalendar();
                    break;
                case 1:
                    calendar = mainActivity.getCurrentCalendar();
                    break;
                case 2:
                    calendar = mainActivity.getNextCalendar();
                    break;
            }
            calAdapter.setCalData(mainActivity.getCalendarList(calendar));
            calAdapter.notifyDataSetChanged();
            year = calendar.get(Calendar.YEAR)+"년";
            month = (calendar.get(Calendar.MONTH)+1)+"월";
            Log.d("","=======>>>"+year+month);
            this.tvDate.setText(year+""+month);
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onResume() {
            Log.d("","onResume=======>>"+getArguments().getInt(ARG_SECTION_NUMBER));

            super.onResume();
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
//            this.mView1 = view;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater,container,savedInstanceState);
            Log.d("","onCreateView=======>>"+getArguments().getInt(ARG_SECTION_NUMBER));
            final MainActivity mainActivity = (MainActivity)getActivity();

            View rootView = inflater.inflate(R.layout.fragment_main, null, false);
            GridView gridCalander = (GridView) rootView.findViewById(R.id.grid_calendal);
            tvDate = (TextView) rootView.findViewById(R.id.tv_currentDate);
            rootView.findViewById(R.id.btn_preMonth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.preMonth();
                }
            });
            rootView.findViewById(R.id.btn_nextMonth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.nextMonth();
                }
            });
            calAdapter = new GridAdapter();
            gridCalander.setAdapter(calAdapter);
            updateView();
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            Log.d("","onDestroyView=======>>"+getArguments().getInt(ARG_SECTION_NUMBER));
        }

        class GridAdapter extends BaseAdapter {
            private ArrayList<CalendarData> mCalendarList;

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
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
                }
                TextView tvDay = (TextView) convertView.findViewById(R.id.grid_calendar_day);
                CalendarData calendarData = getItem(position);
                if(calendarData.isCurrentMonth()) {
                    int dayOfWeek = calendarData.getDayOfWeek();

                    if(Calendar.SUNDAY == dayOfWeek) {
                        tvDay.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    } else if(Calendar.SATURDAY == dayOfWeek) {
                        tvDay.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                    } else {
                        tvDay.setTextColor(getResources().getColor(android.R.color.black));
                    }
                } else {
                    tvDay.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
                tvDay.setText(""+getItem(position).getDay());
                return convertView;
            }
        }
    }

}
