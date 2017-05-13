package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.YuwjBaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.entity.EntityContact;
import com.zondy.jwt.jwtmobile.entity.EntityContactsAndZZJGS;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.entity.EntityZD;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.presenter.IContactPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.ContactPresenterImpl;
import com.zondy.jwt.jwtmobile.ui.DividerGridItemDecoration;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.ui.FullyLinearLayoutManager;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IContactView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywj on 2017/4/25 0025.
 */

public class ContactsActivity2 extends YuwjBaseActivity implements IContactView {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_contact_search)
    ImageView ivContactSearch;
    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;
    @BindView(R.id.horizontalScrollView)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.sv_contact_container)
    ScrollView svContactContainer;
    @BindView(R.id.rcv_contacts)
    RecyclerView rcvContacts;
    @BindView(R.id.rcv_suboffices)
    RecyclerView rcvSuboffices;
    @BindView(R.id.ll_contact_search)
    LinearLayout llContactSearch;

    IContactPresenter contactPresenter;

    EntityUser userInfo;
//    int currentLevel = 1;// 当前展示的机构级别
    EntityZD currentOffice;// 当前选中的机构
    List<EntityContact> currentOfficeContacts;// 当前机构对应的联系人
    List<EntityZD> currentChildrenOffices;// 当前机构的子机构
    CommonAdapter<EntityZD> adapterOffice;
    CommonAdapter<EntityContact> adapterContact;

    public static Intent createIntent(Context context, EntityZD currentOffice) {
        Intent intent = new Intent(context, ContactsActivity2.class);
        intent.putExtra("currentOffice", currentOffice);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_contacts2;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        currentOffice = (EntityZD) intent.getSerializableExtra("currentOffice");
        initParam();
        initView();
        initOperator();
    }

    boolean isFirstLoad = true;//是否是第一次加载

    @Override
    public void initParam() {
        if(isFirstLoad){//如果是第一次加载,初始组织结构从intent中来,如果不是(即从搜索结果中来)

            Intent intent = getIntent();
            currentOffice = (EntityZD) intent.getSerializableExtra("currentOffice");
            isFirstLoad = false;
        }
        if (currentOffice == null) {
            if (Constant.JWT_AREA_SELECTED.equals(Constant.JWT_AREA_HA)) {
                currentOffice = new EntityZD("320800000000", "淮安市公安局", "");
            } else if (Constant.JWT_AREA_SELECTED.equals(Constant.JWT_AREA_YZ)) {
                currentOffice = new EntityZD("431100000000", "永州市公安局", "");
            }
        }

        userInfo = SharedTool.getInstance().getUserInfo(context);
        currentChildrenOffices = new ArrayList<EntityZD>();
        currentOfficeContacts = new ArrayList<EntityContact>();// 当前机构对应的联系人
        contactPresenter = new ContactPresenterImpl(context, this);
        adapterContact = new CommonAdapter<EntityContact>(context
                , R.layout.item_contact, currentOfficeContacts) {

            @Override
            protected void convert(ViewHolder holder, EntityContact entityContact, int position) {
                holder.setText(R.id.tv_contact_name, entityContact.getXm());
            }
        };
        adapterContact.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(ContactsItemActivity.createIntent(context, currentOfficeContacts.get(position)));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        adapterOffice = new CommonAdapter<EntityZD>(context,
                R.layout.item_contact_office, currentChildrenOffices) {


            @Override
            protected void convert(ViewHolder holder, EntityZD entityZD, int position) {

                holder.setText(R.id.tv_office, entityZD.getMc());
            }
        };
        adapterOffice.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                EntityZD zd = currentChildrenOffices.get(position);
                if (zd != null) {
                    currentOffice = zd;
                    addOfficeIndicatorContainer2(currentOffice);
                    contactPresenter.queryContactsByZZJG(currentOffice.getBm());
                    contactPresenter.queryZZJGByParentZZJG(currentOffice.getBm());

                    showLoadingDialog("正在加载...");
                    showLoadingDialog("正在加载...");
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

//    public void addOfficeIndicatorContainer() {
//        final View indicatorContainer = LayoutInflater.from(context).inflate(R.layout.content_contact_office_indicator, llIndicator, false);
//        TextView tvIndicator = (TextView) indicatorContainer.findViewById(R.id.tv_contact_office_indicator);
//        indicatorContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                horizontalScrollView.post(new Runnable() {
//                    public void run() {
//                        horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
//                    }
//                });
//                View item = v.findViewById(R.id.tv_contact_office_indicator);
//                OfficeTag tag = (OfficeTag) item.getTag();
//                int clickItemIndex = -1;
//                for (int i = 0; i < llIndicator.getChildCount(); i++) {
//                    if (indicatorContainer == llIndicator.getChildAt(i)) {
//                        clickItemIndex = i;
//                        break;
//                    }
//                }
//                if (clickItemIndex >= 0 && clickItemIndex < llIndicator.getChildCount()) {
//                    for (int i = llIndicator.getChildCount() - 1; i > clickItemIndex; i--) {
//                        llIndicator.removeViewAt(i);
//                    }
//                }
//                currentLevel = tag.getLevel();
//                currentOffice = new EntityZD(tag.getBm(), tag
//                        .getMc(), "");
//                contactPresenter.queryZZJGByParentZZJG(currentOffice.getBm());
//                contactPresenter.queryContactsByZZJG(currentOffice.getBm());
//
//                showLoadingDialog("正在加载...");
//                showLoadingDialog("正在加载...");
//            }
//        });
//        OfficeTag tag = new OfficeTag(currentOffice.getBm(), currentOffice.getMc(),
//                currentLevel);
//        tvIndicator.setTag(tag);
//        if (currentLevel > 1) {
//            tvIndicator.setText(" > " + currentOffice.getMc());
//        } else {
//            tvIndicator.setText(currentOffice.getMc());
//        }
//        llIndicator.addView(indicatorContainer);
//        horizontalScrollView.post(new Runnable() {
//            public void run() {
//                horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
//            }
//        });
//    }

    public void addOfficeIndicatorContainer2(EntityZD office){
        final View indicatorContainer = LayoutInflater.from(context).inflate(R.layout.content_contact_office_indicator, llIndicator, false);
        TextView tvIndicator = (TextView) indicatorContainer.findViewById(R.id.tv_contact_office_indicator);
        indicatorContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView.post(new Runnable() {
                    public void run() {
                        horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
                    }
                });
                View item = v.findViewById(R.id.tv_contact_office_indicator);

                int clickItemIndex = -1;
                for (int i = 0; i < llIndicator.getChildCount(); i++) {
                    if (indicatorContainer == llIndicator.getChildAt(i)) {
                        clickItemIndex = i;
                        break;
                    }
                }
                if (clickItemIndex >= 0 && clickItemIndex < llIndicator.getChildCount()) {
                    for (int i = llIndicator.getChildCount() - 1; i > clickItemIndex; i--) {
                        llIndicator.removeViewAt(i);
                    }
                }

                currentOffice = (EntityZD) item.getTag();
                contactPresenter.queryZZJGByParentZZJG(currentOffice.getBm());
                contactPresenter.queryContactsByZZJG(currentOffice.getBm());

                showLoadingDialog("正在加载...");
                showLoadingDialog("正在加载...");
            }
        });
//        OfficeTag tag = new OfficeTag(office.getBm(), office.getMc(),
//                currentLevel);
        tvIndicator.setTag(office);
        if (llIndicator.getChildCount()>0) {
            tvIndicator.setText(" > " + office.getMc());
        } else {
            tvIndicator.setText(office.getMc());
        }
        llIndicator.addView(indicatorContainer);
        horizontalScrollView.post(new Runnable() {
            public void run() {
                horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
    }

    @Override
    public void initView() {
        initActionBar(toolbar, tvTitle, "通讯录");
        llIndicator.removeAllViews();
        if(!currentOffice.getBm().endsWith("00000000")){
            switch (Constant.JWT_AREA_SELECTED){
                case Constant.JWT_AREA_HA:

                        EntityZD zd = new EntityZD("320800000000", "淮安市公安局", "");
                    addOfficeIndicatorContainer2(zd);
                    break;
                case Constant.JWT_AREA_DZ:
                    break;
            }
        }

        addOfficeIndicatorContainer2(currentOffice);
        llContactSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ContactSearchActivity.createIntent(context));
            }
        });
        rcvContacts.setLayoutManager(new FullyGridLayoutManager(context, 5));
        rcvContacts.setAdapter(adapterContact);
        rcvContacts.addItemDecoration(new DividerGridItemDecoration(ContactsActivity2.this));

        rcvSuboffices.setLayoutManager(new FullyLinearLayoutManager(context));
        rcvSuboffices.setAdapter(adapterOffice);
        rcvSuboffices.addItemDecoration(new DividerItemDecoration(ContactsActivity2.this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void initOperator() {
        contactPresenter.queryZZJGByParentZZJG(currentOffice.getBm());
        contactPresenter.queryContactsByZZJG(currentOffice.getBm());
        showLoadingDialog("正在加载...");
        showLoadingDialog("正在加载...");
    }

    @Override
    public void queryContactsByZZJGSuccessed(List<EntityContact> contacts) {
        dismissLoadingDialog();
        svContactContainer.smoothScrollTo(0, 0);
        currentOfficeContacts.clear();
        currentOfficeContacts.addAll(contacts);
        rcvContacts.addItemDecoration(new DividerGridItemDecoration(context));
        adapterContact.notifyDataSetChanged();
    }

    @Override
    public void queryZZJGSuccessed(List<EntityZD> allEntitys) {
        dismissLoadingDialog();
        svContactContainer.smoothScrollTo(0, 0);
        if (allEntitys != null) {
            currentChildrenOffices.clear();
            currentChildrenOffices.addAll(allEntitys);
            rcvSuboffices.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
            adapterOffice.notifyDataSetChanged();
        }
    }

    @Override
    public void queryZZJGUnSuccessed(String msg) {
        dismissLoadingDialog();
        svContactContainer.smoothScrollTo(0, 0);
        currentChildrenOffices.clear();
        adapterOffice.notifyDataSetChanged();
//        ToastTool.getInstance().shortLength(context, msg, true);

    }

    @Override
    public void queryContactsByZZJGUnSuccessed(String msg) {
        dismissLoadingDialog();
//        ToastTool.getInstance().shortLength(context, msg, true);
        svContactContainer.smoothScrollTo(0, 0);
        currentOfficeContacts.clear();
        adapterContact.notifyDataSetChanged();
    }

    @Override
    public void queryContactsAndZZJGsByKeywordSuccessed(EntityContactsAndZZJGS contactsAndZZJGSes) {

    }

    @Override
    public void queryContactsAndZZJGsByKeywordFail(Exception e) {

    }

    /**
     * 定义的组织机构,用于定义当前机构的等级
     */
//    class OfficeTag {
//        String bm;
//        String mc;
//        int level;
//
//        public OfficeTag(String bm, String mc, int level) {
//            super();
//            this.bm = bm;
//            this.mc = mc;
//            this.level = level;
//        }
//
//        public String getBm() {
//            return bm;
//        }
//
//        public void setBm(String bm) {
//            this.bm = bm;
//        }
//
//        public String getMc() {
//            return mc;
//        }
//
//        public void setMc(String mc) {
//            this.mc = mc;
//        }
//
//        public int getLevel() {
//            return level;
//        }
//
//        public void setLevel(int level) {
//            this.level = level;
//        }
//
//    }
}
