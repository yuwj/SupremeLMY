package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.common.internal.Objects;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.YuwjBaseActivity;
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

public class ContactSearchActivity extends YuwjBaseActivity implements IContactView {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_contact_keyword)
    EditText etContactKeyword;
    @BindView(R.id.iv_contact_search)
    ImageView ivContactSearch;
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
    List<EntityContact> searchResultContacts;// 查询出来的联系人
    List<EntityZD> searchResultOffices;// 查询出来的机构
    CommonAdapter<EntityZD> adapterOffice;
    CommonAdapter<EntityContact> adapterContact;

    public static Intent createIntent(Context context){
        return new Intent(context,ContactSearchActivity.class);

    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_contact_search;
    }

    @Override
    public void initParam() {
        userInfo = SharedTool.getInstance().getUserInfo(context);
        searchResultOffices = new ArrayList<EntityZD>();
        searchResultContacts = new ArrayList<EntityContact>();
        contactPresenter = new ContactPresenterImpl(context, this);
        adapterContact = new CommonAdapter<EntityContact>(context
                , R.layout.item_contact, searchResultContacts) {

            @Override
            protected void convert(ViewHolder holder, EntityContact entityContact, int position) {
                holder.setText(R.id.tv_contact_name, entityContact.getXm());
            }
        };
        adapterContact.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(ContactsItemActivity.createIntent(context, searchResultContacts.get(position)));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        adapterOffice = new CommonAdapter<EntityZD>(context,
                R.layout.item_contact_office, searchResultOffices) {


            @Override
            protected void convert(ViewHolder holder, EntityZD entityZD, int position) {

                holder.setText(R.id.tv_office, entityZD.getMc());
            }
        };
        adapterOffice.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EntityZD zd = searchResultOffices.get(position);
                startActivity(ContactsActivity2.createIntent(context, zd));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    @Override
    public void initView() {
        initActionBar(toolbar, tvTitle, "通讯录搜索");
        rcvContacts.setLayoutManager(new FullyGridLayoutManager(context, 5));
        rcvContacts.setAdapter(adapterContact);
        rcvContacts.addItemDecoration(new DividerGridItemDecoration(context));

        rcvSuboffices.setLayoutManager(new FullyLinearLayoutManager(context));
        rcvSuboffices.setAdapter(adapterOffice);
        rcvSuboffices.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        ivContactSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = etContactKeyword.getText().toString();
                if(TextUtils.isEmpty(keyword)){
                    ToastTool.getInstance().shortLength(context,"请输入关键字",true);
                    return;
                }
                contactPresenter.queryContactsAndZZJGSByKeyword(keyword);
                showLoadingDialog("正在查询...");
            }
        });
    }

    @Override
    public void initOperator() {
    }

    @Override
    public void queryContactsByZZJGSuccessed(List<EntityContact> contacts) {}

    @Override
    public void queryZZJGSuccessed(List<EntityZD> allEntitys) {}

    @Override
    public void queryZZJGUnSuccessed(String msg) {}

    @Override
    public void queryContactsByZZJGUnSuccessed(String msg) {}

    @Override
    public void queryContactsAndZZJGsByKeywordSuccessed(EntityContactsAndZZJGS contactsAndZZJGSesList) {
        dismissLoadingDialog();
        svContactContainer.smoothScrollTo(0, 0);
        if (contactsAndZZJGSesList != null && contactsAndZZJGSesList.getZzjgList() != null) {
            searchResultOffices.clear();
            searchResultOffices.addAll(contactsAndZZJGSesList.getZzjgList());
            adapterOffice.notifyDataSetChanged();
            rcvSuboffices.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        }
        if (contactsAndZZJGSesList != null && contactsAndZZJGSesList.getContactList() != null) {

            searchResultContacts.clear();
            searchResultContacts.addAll(contactsAndZZJGSesList.getContactList());
            adapterContact.notifyDataSetChanged();
            rcvContacts.addItemDecoration(new DividerGridItemDecoration(context));
        }
    }

    @Override
    public void queryContactsAndZZJGsByKeywordFail(Exception e) {

        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }

    /**
     * 定义的组织机构,用于定义当前机构的等级
     */
    class OfficeTag {
        String bm;
        String mc;
        int level;

        public OfficeTag(String bm, String mc, int level) {
            super();
            this.bm = bm;
            this.mc = mc;
            this.level = level;
        }

        public String getBm() {
            return bm;
        }

        public void setBm(String bm) {
            this.bm = bm;
        }

        public String getMc() {
            return mc;
        }

        public void setMc(String mc) {
            this.mc = mc;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

    }
}
