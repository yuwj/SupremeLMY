package com.zondy.jwt.jwtmobile.view.impl;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntitySearchHistory;
import com.zondy.jwt.jwtmobile.entity.EntityTCFL;
import com.zondy.jwt.jwtmobile.presenter.ISearchPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.SearchPresenterImpl;
import com.zondy.jwt.jwtmobile.util.RealmHelper;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ISearchTCFLView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

import static android.R.attr.data;

public class SearchActivity extends BaseActivity implements ISearchTCFLView {
    @BindView(R.id.btn_more)
    Button btnMore;
    @BindView(R.id.cb_lvguan)
    CheckBox cbLvguan;
    @BindView(R.id.cb_wangba)
    CheckBox cbWangba;
    @BindView(R.id.cb_xuexiao)
    CheckBox cbXuexiao;
    @BindView(R.id.cb_tingchechang)
    CheckBox cbTingchechang;
    @BindView(R.id.cb_chaoshi)
    CheckBox cbChaoshi;
    @BindView(R.id.cb_jiuba)
    CheckBox cbJiuba;
    @BindView(R.id.cb_dianyingyuan)
    CheckBox cbDianyingyuan;
    private ISearchPresenter searchPresenter = new SearchPresenterImpl(this, this);
    private ImageView ivBack;
    private Button btnSearch;
    private ImageView ivCancel;
    private EditText etSearch;
    private XRecyclerView rvHistory;
    private RelativeLayout btnClearHistory;
    private String searchMC = "";
    private RealmHelper mRealmHelper;
    private CommonAdapter<String> commonAdapter;
    private List<String> mDatas = new ArrayList<>();
    private Date date=new Date();
    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            searchMC = intent.getStringExtra("MC");
        }
        initParams();
        initViews();
        initDatas();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initDatas() {
        mDatas.clear();
        List<EntitySearchHistory> histories=mRealmHelper.queryAllhistory();
        if(histories.size()>0){
            for(EntitySearchHistory history:histories  ){
                mDatas.add(history.getKeyword());
                Log.i("sheep",history.getKeyword()+":"+history.getId());
            }
            commonAdapter.notifyDataSetChanged();
        }else {
            commonAdapter.notifyDataSetChanged();
        }
    }

    private void initParams() {
        mRealmHelper=new RealmHelper(this);
        ivBack = (ImageView) findViewById(R.id.iv_search_back);
        btnSearch = (Button) findViewById(R.id.btn_search_confirm);
        ivCancel = (ImageView) findViewById(R.id.iv_search_cancel);
        etSearch = (EditText) findViewById(R.id.et_activity_search);
        rvHistory = (XRecyclerView) findViewById(R.id.rv_history);
        btnClearHistory = (RelativeLayout) findViewById(R.id.btn_clear_history);

    }

    private void initViews() {
        //暂时为了布局美观，通过返回一次的数据源，写死，需要重新搜索时，再调用。
//        searchPresenter.queryTCFZList();
        if (searchMC != null) {
            etSearch.setText(searchMC);
            etSearch.requestFocus();
            btnSearch.setVisibility(View.VISIBLE);
            ivCancel.setVisibility(View.VISIBLE);
        }
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SearchMoreActivity.class);
                startActivity(intent);
                finish();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                    ivCancel.setVisibility(View.VISIBLE);
                    btnSearch.setVisibility(View.VISIBLE);
                } else {
                    ivCancel.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        commonAdapter = new CommonAdapter<String>(this, R.layout.item_search_history, mDatas) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }

            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.tv_item_search_history, s);
            }
        };
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SearchActivity.this, ScrollActivityResult.class);
                intent.putExtra("MC", ""+mDatas.get(position-1));
                Date date=new Date();
                String id=date.getTime()+"";
                EntitySearchHistory history= new EntitySearchHistory(id,""+mDatas.get(position-1));
                mRealmHelper.addHistory(history);
                startActivity(intent);
                initDatas();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvHistory.setAdapter(commonAdapter);
        rvHistory.setPullRefreshEnabled(false);
        rvHistory.setLoadingMoreEnabled(false);
        rvHistory.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        rvHistory.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDatas.add("加载了更多搜索历史");
//                        commonAdapter.notifyDataSetChanged();
//                        rvHistory.loadMoreComplete();
//                    }
//                }, 2000);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
        btnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealmHelper.deleteAllhistory();
                initDatas();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ScrollActivityResult.class);
                intent.putExtra("MC", ""+etSearch.getText().toString());
                Date datedmc=new Date();
                String idmc=datedmc.getTime()+"";
                EntitySearchHistory historymc= new EntitySearchHistory(idmc,""+etSearch.getText().toString());
                mRealmHelper.addHistory(historymc);
                startActivity(intent);
                initDatas();
            }
        });
    }

    @Override
    public void queryTCFLSuccessed(List<EntityTCFL> tcfls) {

    }

    @Override
    public void queryTCFLUnSuccessed() {

    }

    @OnClick({R.id.cb_lvguan, R.id.cb_wangba, R.id.cb_xuexiao, R.id.cb_tingchechang, R.id.cb_chaoshi, R.id.cb_jiuba, R.id.cb_dianyingyuan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_lvguan:
                Intent intentlg=new Intent(SearchActivity.this,ScrollActivityResult.class);
                intentlg.putExtra("MC","旅馆");
                Date datelg=new Date();
                String idlg=datelg.getTime()+"";
                EntitySearchHistory historylg= new EntitySearchHistory(idlg,"旅馆");
                mRealmHelper.addHistory(historylg);
                startActivity(intentlg);
                initDatas();
                break;
            case R.id.cb_wangba:
                Intent intentwb=new Intent(SearchActivity.this,ScrollActivityResult.class);
                intentwb.putExtra("MC","网吧");
                SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                Date curDate=new Date(System.currentTimeMillis());
                String str=format.format(curDate);
                Date datewb=new Date();
                String idwb=datewb.getTime()+"";
                EntitySearchHistory history=new EntitySearchHistory(idwb,"网吧");
                mRealmHelper.addHistory(history);
                startActivity(intentwb);
                initDatas();
                break;
            case R.id.cb_xuexiao:
                Intent intentxx=new Intent(SearchActivity.this,ScrollActivityResult.class);
                intentxx.putExtra("MC","学校");
                Date datexx=new Date();
                String idxx=datexx.getTime()+"";
                EntitySearchHistory historyxx= new EntitySearchHistory(idxx,"学校");
                mRealmHelper.addHistory(historyxx);
                startActivity(intentxx);
                initDatas();
                break;
            case R.id.cb_tingchechang:
                Intent intenttcc=new Intent(SearchActivity.this,ScrollActivityResult.class);
                intenttcc.putExtra("MC","停车场");
                Date datetcc=new Date();
                String idtcc=datetcc.getTime()+"";
                EntitySearchHistory historytcc= new EntitySearchHistory(idtcc,"停车场");
                mRealmHelper.addHistory(historytcc);
                startActivity(intenttcc);
                initDatas();
                break;
            case R.id.cb_chaoshi:
                Intent intentcs=new Intent(SearchActivity.this,ScrollActivityResult.class);
                intentcs.putExtra("MC","超市");
                Date datecs=new Date();
                String idcs=datecs.getTime()+"";
                EntitySearchHistory historycs= new EntitySearchHistory(idcs,"超市");
                mRealmHelper.addHistory(historycs);
                startActivity(intentcs);
                initDatas();
                break;
            case R.id.cb_jiuba:
                Intent intentjb=new Intent(SearchActivity.this,ScrollActivityResult.class);
                intentjb.putExtra("MC","酒吧");
                Date datejb=new Date();
                String idjb=datejb.getTime()+"";
                EntitySearchHistory historyjb= new EntitySearchHistory(idjb,"酒吧");
                mRealmHelper.addHistory(historyjb);
                startActivity(intentjb);
                initDatas();
                break;
            case R.id.cb_dianyingyuan:
                Intent intentdyy=new Intent(SearchActivity.this,ScrollActivityResult.class);
                intentdyy.putExtra("MC","电影院");
                Date datedyy=new Date();
                String iddyy=datedyy.getTime()+"";
                EntitySearchHistory historydyy= new EntitySearchHistory(iddyy,"电影院");
                mRealmHelper.addHistory(historydyy);
                startActivity(intentdyy);
                initDatas();
                break;
        }
    }
}
