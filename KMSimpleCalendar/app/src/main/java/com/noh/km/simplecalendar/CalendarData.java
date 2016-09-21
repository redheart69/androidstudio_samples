package com.noh.km.simplecalendar;

import java.util.Calendar;

/**
 * Created by nohkyoungmo on 2016. 9. 12..
 * 달력 데이터 객체
 * 일자별 달력에대한 정보를 저장 및 반환 한다
 */
public class CalendarData {

    //이전달
    public static final int TYPE_PRE_MONTH = -1;
    //이번달
    public static final int TYPE_CURRENT_MONTH = 0;
    //다음달
    public static final int TYPE_NEXT_MONTH = 1;

    //달력의 타입 이전달 : -1, 이번달 : 0, 다음달 : 1
    private int mMonthType = TYPE_CURRENT_MONTH;

    private Calendar mCalendar;

    CalendarData(Calendar calendar){
        if(calendar == null){
            mCalendar = Calendar.getInstance();
        } else {
            mCalendar = calendar;
        }
    }

    /**
     * 일 반환
     * @return 일
     */
    public int getDay(){
        //start value 1
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 년도 반환
     * @return 년도
     */
    public int getYear(){
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 월 반환
     * @return 월
     */
    public int getMonth(){
        //start value 0
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 요일 반환 (일요일 : 1, 월요일 : 2 ....)
     * @return 요일
     */
    public int getDayOfWeek(){
        return mCalendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 이번달의 타입 저장 (이전,이번,다음달 여부)
     * @param type TYPE_PRE_MONTH, TYPE_CURRENT_MONTH , TYPE_NEXT_MONTH
     */
    public void setMonthType(int type){
        mMonthType = type;
    }
    /**
     * 이번달의 타입 반환 (이전,이번,다음달 여부)
     * @return TYPE_PRE_MONTH, TYPE_CURRENT_MONTH , TYPE_NEXT_MONTH
     */
    public int getMonthType(){
        return mMonthType;
    }
}
