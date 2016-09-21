package com.noh.km.simplecalendar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nohkyoungmo on 2016. 9. 12..
 * 메모 상세보기 화면 (입력 , 수정 , 삭제)
 */
public class MemoDetailActivity extends Activity {
    private EditText mEtMemo;
    private String mYear;
    private String mMonth;
    private String mDay;
    private Button mBtnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        mYear = getIntent().getStringExtra("year");
        mMonth = getIntent().getStringExtra("month");
        mDay = getIntent().getStringExtra("day");
        initView();
        getData();
    }

    /**
     * 레이아웃 초기화 함수
     */
    private void initView(){
        //메모 내용 입력창
        mEtMemo = (EditText)findViewById(R.id.et_memo);
        TextView tvDate = (TextView) findViewById(R.id.tv_date);
        //상단의 현재 날짜를 표시
        tvDate.setText(mYear+"/"+mMonth+"/"+mDay);
        mBtnDelete = (Button)findViewById(R.id.btn_memo_delete);
        //메모 삭제 버튼
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        //DB에 해당 날짜의 메모정보를 삭제한다
                        return CalendarDAO.getInstance(MemoDetailActivity.this).deleteData(mYear,mMonth,mDay);
                    }
                    @Override
                    protected void onPostExecute(Boolean success) {
                        if(success) {
                            //삭제 성공시 메모날짜 데이터에서 제거 해주고 메인엑티비티로 콜백
                            CalendarDAO.getInstance(MemoDetailActivity.this).removeMemoDate(mYear+mMonth+mDay);
                            Toast.makeText(MemoDetailActivity.this,"삭제 완료!",Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }.execute();
            }
        });
        //메모 저장 버튼
        findViewById(R.id.btn_memo_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String memo = mEtMemo.getEditableText().toString();
                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        //입력된 메모를 DB에 저장한다
                        return CalendarDAO.getInstance(MemoDetailActivity.this).insertOrUpdateData(mYear,mMonth,mDay, params[0]);
                    }
                    @Override
                    protected void onPostExecute(Boolean success) {
                        if(success) {
                            //저장 성공시 메모날짜 데이터에 추가 해주고 메인엑티비티로 콜백
                            CalendarDAO.getInstance(MemoDetailActivity.this).addMemoDate(mYear+mMonth+mDay);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }.execute(memo);
            }
        });
    }

    /**
     * DB저장된 메모 데이터 가져오기
     */
    private void getData(){

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                //DB에 저장된 메모를 가져온다
                return CalendarDAO.getInstance(MemoDetailActivity.this).getMemo(mYear,mMonth,mDay);
            }

            @Override
            protected void onPostExecute(String resultMemo) {
                if(resultMemo != null) {
                    //메모데이터가 있다면 화면 업데이트
                    updateView(resultMemo);
                }
            }
        }.execute();

    }

    /**
     * 레이아웃 화면을 최신정보로 업데이트
     * @param memo 저장된 메모
     */
    private void updateView(String memo){
        mEtMemo.setText(memo);
        mBtnDelete.setVisibility(View.VISIBLE);
    }
}
