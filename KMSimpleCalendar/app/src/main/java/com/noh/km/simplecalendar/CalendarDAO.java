package com.noh.km.simplecalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashSet;

/**
 * Created by nohkyoungmo on 2016. 9. 12..
 * DB에 접근 하여 정보(입력 ,조회, 삭제, 수정)를 관리한다
 */
class CalendarDAO {
    private final SQLiteDatabase mDb;
    //조회한 메모 날짜 리스트 (년도 + 월 + 일)
    //ex. 2014130,2014210,20151231...
    private HashSet<String> mMemoDateList = new HashSet<>();
    //월기준의 조회한 메모 날짜 리스트 (년도 + 월)
    //ex. 20141,20142,20151...
    private HashSet<String> mExistMemoOfMonth = new HashSet<>();

    private static CalendarDAO _instance;
    static CalendarDAO getInstance(Context context) {
        if (_instance == null) {
            _instance = new CalendarDAO(context);
        }
        return _instance;
    }

    /***
     * 생성자 DB 객체를 생성한다
     * @param context
     */
    private CalendarDAO(Context context) {
        mDb = CalendarDBHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * 메모리스트의 날짜정보를 추가한다 (리스트타입)
     * @param memoDateList 조회한 메모 날짜 리스트 (년도 + 월 + 일)
     */
    public void addMemoDateList(HashSet<String> memoDateList){
        mMemoDateList.addAll(memoDateList);
    }
    /**
     * 메모리스트의 날짜정보를 추가한다 (단일타입)
     * @param date (년도 + 월 + 일)
     */
    public void addMemoDate(String date){
        mMemoDateList.add(date);
    }
    /**
     * 메모리스트의 날짜정보 삭제 추가한다 (단일타입)
     * @param date (년도 + 월 + 일)
     */
    public void removeMemoDate(String date){
        mMemoDateList.remove(date);
    }
    /**
     * 메모리스트의 날짜정보 리스트 반환
     */
    public HashSet<String> getMemoDateList(){
        return mMemoDateList;
    }

    /**
     * 월기준으로 메모데이터 조회내역을 추가
     * @param year 년도
     * @param month 월
     */
    public void addExistMemoOfMonth(String year, String month){
        mExistMemoOfMonth.add(year+month);
    }
    /**
     * 월기준으로 메모데이터 조회내역이 있는지 여부
     * @param year 년도
     * @param month 월
     */
    public boolean isExistMemoOfMonth(String year, String month){
        return mExistMemoOfMonth.contains(year+month);
    }

    /**
     * 메모데이터를 DB에 입력 또는 수정
     * @param year
     * @param month
     * @param day
     * @param memo
     * @return
     */
    public boolean insertOrUpdateData(String year, String month, String day, String memo) {
        boolean result = false;
        try {
            String isExist = getMemo(year,month,day);
            ContentValues cv = new ContentValues();
            cv.put(CalendarDBHelper.MemoColumns.MEMO, memo);
            if (isExist != null ) {
                result = updateData(cv,year,month,day);
            } else {
                cv.put(CalendarDBHelper.MemoColumns.YEAR, year);
                cv.put(CalendarDBHelper.MemoColumns.MONTH, month);
                cv.put(CalendarDBHelper.MemoColumns.DAY, day);
                result = insertData(cv);
            }
        } catch (Exception e) {
            Log.e("", "insertOrUpdateData ERROR:", e);
        }
        return result;
    }

    /**
     * 메모데이터 DB에 입력
     * @param cv
     * @return
     */
    public boolean insertData(ContentValues cv) {
        long result = -1;
        try {
            result = mDb.insert(CalendarDBHelper.Tables.MEMO_TABLE, null, cv);
        } catch (Exception e) {
            Log.e("", "insertData ERROR:", e);
        }
        return result > -1;
    }

    /**
     * 메모데이터 DB에 업데이트
     * @param cv
     * @param year
     * @param month
     * @param day
     * @return
     */
    public boolean updateData(ContentValues cv, String year, String month, String day) {
        long result = -1;
        try {
            String selection = CalendarDBHelper.MemoColumns.YEAR +" = ? AND "+CalendarDBHelper.MemoColumns.MONTH+" = ? AND "+CalendarDBHelper.MemoColumns.DAY+" = ?";
            String[] args = {year, month, day};
            result = mDb.update(CalendarDBHelper.Tables.MEMO_TABLE, cv, selection, args);
        } catch (Exception e) {
            Log.e("", "insertData ERROR:", e);
        }
        return result > -1;
    }
    /**
     * 메모데이터를 DB에서 삭제
     * @param year
     * @param month
     * @param day
     * @return
     */
    public boolean deleteData(String year, String month, String day) {

        long result = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put(CalendarDBHelper.MemoColumns.YEAR, year);
            cv.put(CalendarDBHelper.MemoColumns.MONTH, month);
            cv.put(CalendarDBHelper.MemoColumns.DAY, day);
            String selection = CalendarDBHelper.MemoColumns.YEAR +" = ? AND "+CalendarDBHelper.MemoColumns.MONTH+" = ? AND "+CalendarDBHelper.MemoColumns.DAY+" = ?";
            String[] args = {year, month, day};
            result = mDb.delete(CalendarDBHelper.Tables.MEMO_TABLE, selection,args);
        } catch (Exception e) {
            Log.e("", "deleteData ERROR:", e);
        }
        return result > -1;
    }

    /**
     * 메모 데이터를 DB에서 조회
     * @param year
     * @param month
     * @param day
     * @return
     */
    public String getMemo(String year, String month, String day) {
        Cursor cursor = null;
        String memo = null;
        try {
            String selection = CalendarDBHelper.MemoColumns.YEAR +" = ? AND "+CalendarDBHelper.MemoColumns.MONTH+" = ? AND "+CalendarDBHelper.MemoColumns.DAY+" = ?";
            String[] args = {year, month, day};
            cursor = mDb.query(CalendarDBHelper.Tables.MEMO_TABLE, new String[]{CalendarDBHelper.MemoColumns.MEMO}, selection,args,null,null,null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                   memo = cursor.getString(cursor.getColumnIndex(CalendarDBHelper.MemoColumns.MEMO));
                }
            }
        } catch (Exception e) {
            Log.e("", "getMemo ERROR : ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return memo;
    }

    /**
     * 월기준으로 메모데이터 리스트를 가져옴
     * @param year
     * @param month
     * @return
     */
    public HashSet<String> getMemoDateList(String year, String month) {
        Cursor cursor = null;
        HashSet<String> memoList = new HashSet<>();
        try {
            String selection = CalendarDBHelper.MemoColumns.YEAR +" = ? AND "+CalendarDBHelper.MemoColumns.MONTH+" = ?";
            String[] args = {year, month};
            cursor = mDb.query(CalendarDBHelper.Tables.MEMO_TABLE,null, selection,args,null,null,null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String yearVal = cursor.getString(cursor.getColumnIndex(CalendarDBHelper.MemoColumns.YEAR));
                        String monthVal = cursor.getString(cursor.getColumnIndex(CalendarDBHelper.MemoColumns.MONTH));
                        String dayVal = cursor.getString(cursor.getColumnIndex(CalendarDBHelper.MemoColumns.DAY));
                        memoList.add(yearVal+monthVal+dayVal);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("", "getMemoDateList ERROR : ", e);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return memoList;
    }

}
