package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.MonthView;
import com.tubb.calendarselector.library.SCDateUtils;
import com.tubb.calendarselector.library.SCMonth;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.calendar.AnimDayViewInflater;
import com.zondy.jwt.jwtmobile.util.ToastTool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.zondy.jwt.jwtmobile.R.id.vp;
import static com.zondy.jwt.jwtmobile.R.id.vpMonth;

/**
 * Created by sheep on 2017/3/7.
 */

public class CalendarSelectorActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static String currentYear;
    private static String currentDay;
    private static String currentMonth;
    TextView tvMonthTitle;
    ViewPager vpMonth;
    @BindView(R.id.btn_xzrq_confirm)
    Button btnXzrqConfirm;

    private List<SCMonth> months;
    private static MonthFragment mCurrentFrg;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_calendar_selector;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btnXzrqConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MonthFragment.dayChoosed!=null){
                    Log.i("sheep",MonthFragment.dayChoosed.toString());
                    Intent intent=new Intent();
                    intent.putExtra("day",MonthFragment.dayChoosed);
                    CalendarSelectorActivity.this.setResult(RESULT_OK,intent);
                    MonthFragment.dayChoosed=null;
                    finish();
                }else {
                    Log.i("sheep",new FullDay(Integer.parseInt(currentYear), Integer.parseInt(currentMonth), Integer.parseInt(currentDay)).toString());
                    Intent intent=new Intent();
                    intent.putExtra("day",new FullDay(Integer.parseInt(currentYear), Integer.parseInt(currentMonth), Integer.parseInt(currentDay)));
                    CalendarSelectorActivity.this.setResult(RESULT_OK,intent);
                    MonthFragment.dayChoosed=null;
                    finish();
                }
            }

        });
        Date date = new Date(System.currentTimeMillis());
        Date date1 = new Date(System.currentTimeMillis() - 315360000000l);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentTime = sdf.format(date);
        currentYear = currentTime.substring(0, 4);
        currentMonth = currentTime.substring(4, 6);
        if(currentMonth.substring(0,1).equals("0")){
            currentMonth=currentMonth.substring(1);
        }
        currentDay = currentTime.substring(6);
        if(currentDay.substring(0,1).equals("0")){
            currentDay=currentDay.substring(1);
        }
        String firstTime = sdf.format(date1);
        String firstYear = firstTime.substring(0, 4);
        String firstMonth = firstTime.substring(4, 6);
        tvMonthTitle = (TextView) findViewById(R.id.tvMonthTitle);
        vpMonth = (ViewPager) findViewById(R.id.vpMonth);

        months = SCDateUtils.generateMonths(Integer.parseInt(firstYear), Integer.parseInt(firstMonth),
                Integer.parseInt(currentYear), Integer.parseInt(currentMonth), SCMonth.MONDAY_OF_WEEK);
        tvMonthTitle.setText(currentYear + "年  " + currentMonth + "月");

        List<Fragment> fragments = new ArrayList<>(months.size());
        for (SCMonth month : months) {
            fragments.add(MonthFragment.newInstance(month));
        }
        vpMonth.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                SCMonth month = months.get(position);
                tvMonthTitle.setText(month.getYear() + "年  " + month.getMonth() + "月");
            }
        });
        MonthFragmentAdapter adapter = new MonthFragmentAdapter(getSupportFragmentManager(), fragments);
        vpMonth.setAdapter(adapter);
        vpMonth.setCurrentItem(months.size() - 1);

    }
    public void clickNextMonthDay(SCMonth currentMonth,MonthView monthView) {

        int currentIndex = months.indexOf(currentMonth);
        if (currentIndex + 1 < months.size()){
            monthView.clearSelectedDays();
            vpMonth.setCurrentItem(currentIndex + 1, false);
        }
        else{
            Toast.makeText(this, "不能超过当前月份！", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickPrevMonthDay(SCMonth currentMonth,MonthView monthView) {
        monthView.clearSelectedDays();
        int currentIndex = months.indexOf(currentMonth);
        if (currentIndex - 1 >= 0)
            vpMonth.setCurrentItem(currentIndex - 1, false);
    }

    class MonthFragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments;

        public MonthFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentFrg = (MonthFragment) object;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public static final class MonthFragment extends Fragment {
        public MonthView monthView;
        public static FullDay dayChoosed=null;
        public static Fragment newInstance(SCMonth month) {
            Fragment fragment = new MonthFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("month", month);
            fragment.setArguments(bundle);
            return fragment;
        }

        public MonthView getMonthView() {
            return monthView;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.item_calendar_vp, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            final SCMonth month = getArguments().getParcelable("month");
            monthView = (MonthView) view.findViewById(R.id.scMv);
            monthView.setSCMonth(month,new AnimDayViewInflater(getActivity()));
            monthView.setMonthDayClickListener(new MonthView.OnMonthDayClickListener() {
                @Override
                public void onMonthDayClick(FullDay day) {
                    if(day.getYear()==Integer.parseInt(currentYear)&&day.getMonth()==Integer.parseInt(currentMonth)&&day.getDay()>Integer.parseInt(currentDay)){
                        ToastTool.getInstance().shortLength(getActivity(),"选择日期不能超过当天！",true);
                        return;
                    }
                    if (SCDateUtils.isPrevMonthDay(monthView.getYear(), monthView.getMonth(),
                            day.getYear(), day.getMonth())) {
                        clickPrev(month,monthView);
                    } else if (SCDateUtils.isNextMonthDay(monthView.getYear(), monthView.getMonth(),
                            day.getYear(), day.getMonth())) {
                        clickNext(month,monthView);
                    } else {
                        dayChoosed=day;
                        monthView.clearSelectedDays();
                        monthView.addSelectedDay(day);
                    }
                }
            });
            MonthView monthView = mCurrentFrg.getMonthView();
            FullDay day = new FullDay(Integer.parseInt(currentYear), Integer.parseInt(currentMonth), Integer.parseInt(currentDay));
            monthView.addSelectedDay(day);
        }

        private void clickNext(SCMonth month,MonthView monthView) {
            ((CalendarSelectorActivity) getActivity()).clickNextMonthDay(month,monthView);
        }

        private void clickPrev(SCMonth month,MonthView monthView) {
            ((CalendarSelectorActivity) getActivity()).clickPrevMonthDay(month,monthView);
        }
    }

}
