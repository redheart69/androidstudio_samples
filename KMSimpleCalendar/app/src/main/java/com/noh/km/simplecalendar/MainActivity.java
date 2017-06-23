package com.noh.km.simplecalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by nohkyoungmo on 2016. 9. 12..
 * 달력의 메인 화면 엑티비티
 */
public class MainActivity extends AppCompatActivity {
    //Fragment 페이지 넘버 이전달 : 0 , 이번달 : 1 , 다음달 : 2
    private static final int SECTION_NUMBER_PRE_MONTH = 0;
    private static final int SECTION_NUMBER_CURRENT_MONTH = 1;
    private static final int SECTION_NUMBER_NEXT_MONTH = 2;
    //뷰페이저 객체
    private ViewPager mViewPager;
    //현재달의 캘린더 객체
    private Calendar mCurrentCalendal = Calendar.getInstance();
    //이전달 프레그먼트
    private PlaceholderFragment mCalendarFragmentPre;
    //이번달 프레그먼트
    private PlaceholderFragment mCalendarFragmentCurrent;
    //다음달 프레그먼트
    private PlaceholderFragment mCalendarFragmentNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mViewPager.setCurrentItem(SECTION_NUMBER_CURRENT_MONTH);
    }

    /**
     * 레이아웃 초기화
     */
    private void initView(){
        mCalendarFragmentPre = PlaceholderFragment.newInstance(SECTION_NUMBER_PRE_MONTH);
        mCalendarFragmentCurrent = PlaceholderFragment.newInstance(SECTION_NUMBER_CURRENT_MONTH);
        mCalendarFragmentNext = PlaceholderFragment.newInstance(SECTION_NUMBER_NEXT_MONTH);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //스크롤이 멈췃을때
                if(state == ViewPager.SCROLL_STATE_IDLE) {
                    //왼쪽 또는 오른쪽으로 스와이프 했을경우에는 가운데로 다시 셋팅해줌
                    if(mViewPager.getCurrentItem() == SECTION_NUMBER_PRE_MONTH || mViewPager.getCurrentItem() == SECTION_NUMBER_NEXT_MONTH) {
                        //현재 캘린더 객체에 월을 +- 해줌
                        if(mViewPager.getCurrentItem() == SECTION_NUMBER_PRE_MONTH) {
                            mCurrentCalendal.add(Calendar.MONTH,-1);
                        } else {
                            mCurrentCalendal.add(Calendar.MONTH,1);
                        }
                        //현재달의 화면을 업데이트후
                        mCalendarFragmentCurrent.updateView();
                        //페이지를 현재달로 셋팅한다
                        mViewPager.setCurrentItem(SECTION_NUMBER_CURRENT_MONTH,false);
                        //이전달 , 다음달의 화면을 업데이트
                        mCalendarFragmentPre.updateView();
                        mCalendarFragmentNext.updateView();
                    }
                }
            }
        });
    }

    /**
     * 다음달 정보를 가져옴
     * @return
     */
    private Calendar getNextCalendar(){
        Calendar nextCalendal = (Calendar) mCurrentCalendal.clone();
        nextCalendal.add(Calendar.MONTH,1);
        return nextCalendal;
    }

    /**
     * 이전달 정보를 가져옴
     * @return
     */
    private Calendar getPreCalendar(){
        Calendar preCalendal = (Calendar) mCurrentCalendal.clone();
        preCalendal.add(Calendar.MONTH,-1);
        return preCalendal;
    }

    /**
     * 현재달의 정보를 가져옴
     * @return
     */
    private Calendar getCurrentCalendar(){
        return mCurrentCalendal;
    }

    /**
     * 다음달로 이동
     */
    public void nextMonth(){
        mViewPager.setCurrentItem(SECTION_NUMBER_NEXT_MONTH);
    }

    /**
     * 이전달로 이동
     */
    public void preMonth(){
        mViewPager.setCurrentItem(SECTION_NUMBER_PRE_MONTH);
    }

    /**
     * 달력 데이터 정보 리스트를 가져온다
     * @param currentCalendar
     * @return
     */
    private ArrayList<CalendarData> getCalendarList(Calendar currentCalendar){
        if(currentCalendar == null) {
            return null;
        }
        //이번달을 날짜를 1일로 초기화
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        //이번달 1일의 요일
        int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
        //이번달의 월을 가져온다
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        //1일 이전의 빈칸의 인덱스를 가져온다 (이전달의 날짜표현을위해)
        int dayIndex = -(dayOfWeek - 1);
        ArrayList<CalendarData> calenarList = new ArrayList<>();
        while (true){
            Calendar calendar = (Calendar) currentCalendar.clone();
            calendar.add(Calendar.DAY_OF_MONTH,dayIndex);
            CalendarData calenarData = new CalendarData(calendar);
            //이번달이 아닐경우
            if(currentMonth != calendar.get(Calendar.MONTH)) {

                if(dayIndex > 0 && calenarList.size() % 7 == 0){
                    //달력데이터 완료
                    break;
                } else if(dayIndex < 0) {
                    //이전달
                    calenarData.setMonthType(CalendarData.TYPE_PRE_MONTH);
                } else {
                    //다음달
                    calenarData.setMonthType(CalendarData.TYPE_NEXT_MONTH);
                }
            } else {
                //이번달
                calenarData.setMonthType(CalendarData.TYPE_CURRENT_MONTH);
            }
            calenarList.add(calenarData);
            dayIndex++;
        }
        return calenarList;
    }

    /**
     * 뷰페이저를 관리하는 어댑터
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case SECTION_NUMBER_PRE_MONTH:
                    return mCalendarFragmentPre;
                case SECTION_NUMBER_CURRENT_MONTH:
                    return mCalendarFragmentCurrent;
                case SECTION_NUMBER_NEXT_MONTH:
                    return mCalendarFragmentNext;
            }
            return  mCalendarFragmentPre;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //메모 수정 , 삭제후 화면 업데이트
        if(resultCode == Activity.RESULT_OK) {
            mCalendarFragmentCurrent.updateView();
            mCalendarFragmentPre.updateView();
            mCalendarFragmentNext.updateView();
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
        public TextView mTvDate;
        private GridAdapter mGridCalendarAdapter;
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

        /**
         * 달력 화면 갱신
         */
        public void updateView(){
            Calendar calendar = null;
            String year = "";
            String month = "";
            final MainActivity mainActivity = (MainActivity)getActivity();
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case SECTION_NUMBER_PRE_MONTH:
                    //이전달 달력정보 가져옴
                    calendar = mainActivity.getPreCalendar();
                    break;
                case SECTION_NUMBER_CURRENT_MONTH:
                    //이번달 달력정보 가져옴
                    calendar = mainActivity.getCurrentCalendar();
                    break;
                case SECTION_NUMBER_NEXT_MONTH:
                    //다음달 달력정보 가져옴
                    calendar = mainActivity.getNextCalendar();
                    break;
            }
            //그리드뷰에 달력정보를 셋팅
            mGridCalendarAdapter.setCalData(mainActivity.getCalendarList(calendar));
            mGridCalendarAdapter.notifyDataSetChanged();
            year = String.valueOf(calendar.get(Calendar.YEAR));
            month = String.valueOf((calendar.get(Calendar.MONTH)+1));
            //이번달 메모정보를 가져온적이 있는지 체크
            if(CalendarDAO.getInstance(getContext()).isExistMemoOfMonth(year,month) == false) {
                //가져온적이 없다면 DB에서 메모정보를 가져옴
                getMemoDataList(year,month);
            }
            this.mTvDate.setText(year+"년 "+month+"월");
        }

        /**
         * 메모 정보 리스트를 가져옴
         * @param year
         * @param month
         */
        private void getMemoDataList(final String year, final String month){
            new AsyncTask<String, Void, HashSet<String>>() {
                @Override
                protected HashSet<String> doInBackground(String... params) {
                    // DB에서 메모 정보 리스트를 가져옴
                    return CalendarDAO.getInstance(getContext()).getMemoDateList(year, month);
                }

                @Override
                protected void onPostExecute(HashSet<String> resultList) {
                    if(resultList != null) {
                        //메모 정보를 저장
                        CalendarDAO.getInstance(getContext()).addMemoDateList(resultList);
                        CalendarDAO.getInstance(getContext()).addExistMemoOfMonth(year,month);
                        mGridCalendarAdapter.notifyDataSetChanged();
                    }
                }
            }.execute();
        }
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater,container,savedInstanceState);
            final MainActivity mainActivity = (MainActivity)getActivity();

            View rootView = inflater.inflate(R.layout.fragment_main, null, false);
            GridView gridCalander = (GridView) rootView.findViewById(R.id.grid_calendal);
            mTvDate = (TextView) rootView.findViewById(R.id.tv_currentDate);
            //이전달 버튼
            rootView.findViewById(R.id.btn_preMonth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.preMonth();
                }
            });
            //다음달 버튼
            rootView.findViewById(R.id.btn_nextMonth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.nextMonth();
                }
            });
            //달력 그리드 아답터 초기화
            mGridCalendarAdapter = new GridAdapter(getContext());
            gridCalander.setAdapter(mGridCalendarAdapter);
            gridCalander.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CalendarData calendarData = (CalendarData)parent.getAdapter().getItem(position);
                    //현재달의 날짜를 클릭 했을시에 메모 화면 호출
                    if(calendarData.getMonthType() == CalendarData.TYPE_CURRENT_MONTH) {
                        Intent intent = new Intent(getActivity(), MemoDetailActivity.class);
                        intent.putExtra("year",String.valueOf(calendarData.getYear()));
                        intent.putExtra("month",String.valueOf(calendarData.getMonth()));
                        intent.putExtra("day",String.valueOf(calendarData.getDay()));
                        startActivityForResult(intent,0);
                    } else if(calendarData.getMonthType() == CalendarData.TYPE_PRE_MONTH) {
                        //이전달의 날짜를 클릭했을시에 이전달로 화면 전환
                        mainActivity.preMonth();
                    } else if(calendarData.getMonthType() == CalendarData.TYPE_NEXT_MONTH) {
                        //다음달의 날짜를 클릭했을시에 다음달로 화면 전환
                        mainActivity.nextMonth();
                    }
                }
            });
            //화면 재갱신
            updateView();
            return rootView;
        }
    }

}
