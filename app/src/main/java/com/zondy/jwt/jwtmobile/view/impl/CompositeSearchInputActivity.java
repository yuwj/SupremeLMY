package com.zondy.jwt.jwtmobile.view.impl;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zondy.jwt.jwtmobile.entity.EntityPoi;
import com.zondy.jwt.jwtmobile.entity.EntityPoiType;
import com.zondy.jwt.jwtmobile.entity.EntitySearchHistory;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.ISearchPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.SearchPresenterImpl;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.RealmHelper;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ICompositeSearchInputView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

//import io.realm.Realm;


public class CompositeSearchInputActivity extends BaseActivity implements ICompositeSearchInputView {


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
    //    private String searchMC = "";
    private RealmHelper mRealmHelper;
    private CommonAdapter<String> adapterWithSearchHistory;
    private CommonAdapter<EntityPoiType> adapterWithEntityPoiType;
    private List<EntityPoiType> displayPoiTypeDatas;
    private List<EntityPoiType> allPoiTypeDatas;
    private List<String> searchHistoryDatas;
    EntityUser userInfo;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, CompositeSearchInputActivity.class);
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

                Date date = new Date();
                String id = date.getTime() + "";
                EntitySearchHistory history = new EntitySearchHistory(id, "" + searchHistoryDatas.get(position - 1));
                mRealmHelper.addHistory(history);

                updateSearchHistory();
//                Intent intent = new Intent(CompositeSearchInputActivity.this, ScrollActivityResult.class);
//                intent.putExtra("MC", "" + searchHistoryDatas.get(position - 1));
//                startActivity(intent);
                startActivity(CompositeSearchResultListActivity.createIntent(context,searchHistoryDatas.get(position - 1)));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        allPoiTypeDatas = new ArrayList<>();
        displayPoiTypeDatas = new ArrayList<>();
        adapterWithEntityPoiType = new CommonAdapter<EntityPoiType>(context, R.layout.item_poi_type, displayPoiTypeDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityPoiType entityPoiType, int position) {

                holder.setText(R.id.tv_value, entityPoiType.getLargeClass());

            }
        };
        adapterWithEntityPoiType.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EntityPoiType type = displayPoiTypeDatas.get(position);
                if(type.getLargeClass().contains("更多")){
                    displayPoiTypeDatas.clear();
                    displayPoiTypeDatas.addAll(allPoiTypeDatas);
                    adapterWithEntityPoiType.notifyDataSetChanged();
                    return;
                }
                EntitySearchHistory history = new EntitySearchHistory(new Date().getTime()+"", type.getLargeClass());
                mRealmHelper.addHistory(history);
                updateSearchHistory();
                startActivity(CompositeSearchResultListActivity.createIntent(context, type));

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mRealmHelper = new RealmHelper(this);


    }

    private void initViews() {
        tvSearchConfirm.setVisibility(View.GONE);
        ivSearchCancel.setVisibility(View.GONE);

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
        rvCommonPoiType.setLayoutManager(new FullyGridLayoutManager(context, 4));
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
                String keyword = etActivitySearch.getText().toString().trim();
                if(TextUtils.isEmpty(keyword)){
                    ToastTool.getInstance().shortLength(context,"请输入待搜索的关键字",true);
                    return;
                }
                mRealmHelper.addHistory(new EntitySearchHistory(new Date().getTime() + "", keyword));
                updateSearchHistory();

                startActivity(CompositeSearchResultListActivity.createIntent(context,etActivitySearch.getText().toString()));
            }
        });


    }

    public void initOperator() {
        String jh = userInfo.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        searchPresenter.queryPoiTypes(jh, simid);
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
    public void queryPoiTypesSuccess(List<EntityPoiType> poiTypes) {
        if (poiTypes != null) {
            allPoiTypeDatas.clear();
            allPoiTypeDatas.addAll(poiTypes);
            if(allPoiTypeDatas.size()>12){
                List<EntityPoiType> l = allPoiTypeDatas.subList(0,11);
                EntityPoiType t = new EntityPoiType();
                t.setLargeClass("更多 >");
                t.setClassCode("000");
                displayPoiTypeDatas.clear();
                displayPoiTypeDatas.addAll(l);
                displayPoiTypeDatas.add(t);
            }else{
                displayPoiTypeDatas.clear();
                displayPoiTypeDatas.addAll(allPoiTypeDatas);
            }
            adapterWithEntityPoiType.notifyDataSetChanged();
        }
    }

    @Override
    public void queryPoiTypesFail(Exception e) {
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);

    }
}
