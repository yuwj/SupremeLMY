package com.zondy.jwt.jwtmobile.view.impl;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntitySearchHistory;
import com.zondy.jwt.jwtmobile.entity.EntityTCFL;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.ISearchPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.SearchPresenterImpl;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.RealmHelper;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ISearchTCFLView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

//import io.realm.Realm;


public class SearchInputActivityYuwj extends BaseActivity implements ISearchTCFLView {


    @BindView(R.id.iv_search_back)
    ImageView ivSearchBack;
    @BindView(R.id.sousuotu)
    ImageView sousuotu;
    @BindView(R.id.et_activity_search)
    EditText etActivitySearch;
    @BindView(R.id.iv_search_cancel)
    ImageView ivSearchCancel;
    @BindView(R.id.tv_search_confirm)
    TextView tvSearchConfirm;
    @BindView(R.id.rv_common_poi_type)
    RecyclerView rvCommonPoiType;
    @BindView(R.id.tv_clear_history)
    TextView tvClearHistory;
    @BindView(R.id.rv_history)
    XRecyclerView rvHistory;

    private ISearchPresenter searchPresenter;
    private String searchMC = "";
    private RealmHelper mRealmHelper;
    private CommonAdapter<String> adapterWithSearchHistory;
    private CommonAdapter<EntityTCFL> adapterWithEntityPoiType;
    private List<EntityTCFL> poiTypeDatas;
    private List<String> searchHistoryDatas;
    EntityUser userInfo;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SearchInputActivityYuwj.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_search_input;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParams();
        initViews();
        initOperator();
    }




    private void initParams() {
        Intent intent = getIntent();
        if (intent != null) {
            searchMC = intent.getStringExtra("MC");
        }
        userInfo = SharedTool.getInstance().getUserInfo(context);
        searchHistoryDatas = new ArrayList<>();
        searchPresenter = new SearchPresenterImpl(this, this);

        adapterWithSearchHistory = new CommonAdapter<String>(this, R.layout.item_search_history, searchHistoryDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_item_search_history, s);
            }

        };
        adapterWithSearchHistory.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SearchInputActivityYuwj.this, ScrollActivityResult.class);
                intent.putExtra("MC", "" + searchHistoryDatas.get(position - 1));
                Date date = new Date();
                String id = date.getTime() + "";
                EntitySearchHistory history = new EntitySearchHistory(id, "" + searchHistoryDatas.get(position - 1));
                mRealmHelper.addHistory(history);
                startActivity(intent);
                updateSearchHistory();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        poiTypeDatas = new ArrayList<>();
        adapterWithEntityPoiType = new CommonAdapter<EntityTCFL>(context, R.layout.item_poi_type, poiTypeDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityTCFL entityPoiType, int position) {

                holder.setText(R.id.tv_value, entityPoiType.getLayername());

            }
        };
        adapterWithEntityPoiType.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EntityTCFL type = poiTypeDatas.get(position);
                //TODO 根据类型进行搜索
                if (type.getLayername().equals("更多")) {
                    Intent intent = new Intent(SearchInputActivityYuwj.this, SearchMoreActivity.class);
                    startActivity(intent);
                    finish();
                } else {
//                    Intent intentwb = new Intent(SearchInputActivityYuwj.this, ScrollActivityResult.class);
//                    intentwb.putExtra("MC", type.getLayername());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    String str = format.format(curDate);
                    Date datewb = new Date();
                    String idwb = datewb.getTime() + "";
                    EntitySearchHistory history = new EntitySearchHistory(idwb, type.getMc());
                    mRealmHelper.addHistory(history);
                    updateSearchHistory();
                    startActivity(SearchResultListActivity.createIntent(context,type));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mRealmHelper = new RealmHelper(this);


    }

    private void initViews() {


        if (searchMC != null) {
            etActivitySearch.setText(searchMC);
            etActivitySearch.requestFocus();
            tvSearchConfirm.setVisibility(View.VISIBLE);
            ivSearchCancel.setVisibility(View.VISIBLE);
        }else{

            tvSearchConfirm.setVisibility(View.GONE);
            ivSearchCancel.setVisibility(View.GONE);
        }
        etActivitySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etActivitySearch.getText().toString())) {
                    ivSearchCancel.setVisibility(View.VISIBLE);
                    tvSearchConfirm.setVisibility(View.VISIBLE);
                } else {
                    ivSearchCancel.setVisibility(View.GONE);
                    tvSearchConfirm.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvHistory.setAdapter(adapterWithSearchHistory);
        rvHistory.setPullRefreshEnabled(false);
        rvHistory.setLoadingMoreEnabled(false);
        rvHistory.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        rvCommonPoiType.setLayoutManager(new FullyGridLayoutManager(context,4));
        rvCommonPoiType.setAdapter(adapterWithEntityPoiType);

        ivSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etActivitySearch.setText("");
            }
        });
        tvClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealmHelper.deleteAllhistory();
                updateSearchHistory();
            }
        });
        tvSearchConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchInputActivityYuwj.this, ScrollActivityResult.class);
                intent.putExtra("MC", "" + etActivitySearch.getText().toString());
                Date currentDate = new Date();
                String idmc = currentDate.getTime() + "";
                EntitySearchHistory historymc = new EntitySearchHistory(idmc, "" + etActivitySearch.getText().toString());
                mRealmHelper.addHistory(historymc);
                startActivity(intent);
                updateSearchHistory();
            }
        });


    }

    public void initOperator() {
        String jh = userInfo.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        searchPresenter.queryTCFZList(jh, simid);
        updateSearchHistory();
    }

    public void updateSearchHistory() {
        searchHistoryDatas.clear();
        List<EntitySearchHistory> histories = mRealmHelper.queryAllhistory();
        if (histories.size() > 0) {
            for (EntitySearchHistory history : histories) {
                searchHistoryDatas.add(history.getKeyword());
                Log.i("sheep", history.getKeyword() + ":" + history.getId());
            }
            adapterWithSearchHistory.notifyDataSetChanged();
        } else {
            adapterWithSearchHistory.notifyDataSetChanged();
        }
    }

    @Override
    public void queryTCFLSuccessed(List<EntityTCFL> tcfls) {

        if (tcfls != null) {
            if (tcfls.size() > 9) {
                List<EntityTCFL> list = tcfls.subList(0, 9);
                poiTypeDatas.clear();
                poiTypeDatas.addAll(list);
            } else {
                poiTypeDatas.clear();
                poiTypeDatas.addAll(tcfls);
            }
            EntityTCFL more = new EntityTCFL();
            more.setMc("更多");
            poiTypeDatas.add(more);
            adapterWithEntityPoiType.notifyDataSetChanged();
        }
    }

    @Override
    public void queryTCFLUnSuccessed(Exception e) {
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }


}
