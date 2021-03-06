package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityContact;
import com.zondy.jwt.jwtmobile.entity.EntityContactsAndZZJGS;
import com.zondy.jwt.jwtmobile.entity.EntityZD;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.presenter.IContactPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.ContactPresenterImpl;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IContactView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sheep on 2017/1/12.
 */

public class ContactsActivity extends BaseActivity implements View.OnClickListener, IContactView {

    IContactPresenter contactPresenter = new ContactPresenterImpl(this, this);

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.indexBar)
    IndexBar mIndexBar;
    @BindView(R.id.tvSideBarHint)
    TextView mTvSideBarHint;
    @BindView(R.id.rl_choose_zzjg)
    RelativeLayout rlChooseZzjg;
    @BindView(R.id.tv_zzjg)
    TextView tvZzjg;

    PopupWindow popwindow;//popupWindowSelectZZJG;//选择组织结构的popupwindow
    View popContentView;//pop弹框view
    TextView tvChooseTitle;//pop弹框标题

    int currentLevel = 1;//当前展示的机构级别
    List<EntityZD> mZZJGDatas = new ArrayList<EntityZD>();//选择组织机构的数据源
    CommonAdapter<EntityZD> commonAdapterZZJG;//选择组织机构的adapter
    String dwbm = "";
    String zzjg = "淮安市公安局";
    private LinearLayoutManager mManager;
    private List<EntityContact> mDatas = new ArrayList<>();
    private SuspensionDecoration mDecoration;
    private CommonAdapter<EntityContact> adapter;


    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initViews();
        initOperator();
    }

    private void initParam() {
        commonAdapterZZJG = new CommonAdapter<EntityZD>(context, R.layout.item_contacts_pop_zzjg, mZZJGDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityZD entityZD, int position) {
                holder.setText(R.id.tv_item_scrollresults_shaixuan, entityZD.getMc());
            }
        };
        commonAdapterZZJG.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(tvChooseTitle != null){
                    tvChooseTitle.setText(mZZJGDatas.get(position).getMc());
                }
                dwbm = mZZJGDatas.get(position).getBm();

                zzjg = mZZJGDatas.get(position).getMc();
                contactPresenter.queryContactsByZZJG(dwbm);
                showLoadingDialog("正在加载...");
                if (popwindow != null) {
                    popwindow.dismiss();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        adapter = new CommonAdapter<EntityContact>(this, R.layout.item_rv_activity_contacts, mDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityContact entityContact, int position) {
                holder.setText(R.id.tv_xm, entityContact.getXm());
                holder.setText(R.id.tv_dh, entityContact.getDh());
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(ContactsActivity.this, ContactsItemActivity.class);
                intent.putExtra("DH", mDatas.get(position).getDh());
                intent.putExtra("MC", mDatas.get(position).getXm());
                intent.putExtra("JH", mDatas.get(position).getJh());
                intent.putExtra("SSDWMC", mDatas.get(position).getSsdwmc());
                intent.putExtra("ZW", mDatas.get(position).getZw());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }



    private void initOperator() {
        contactPresenter.queryContactsByZZJG("320800000000");
        showLoadingDialog("正在加载...");
    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        rlChooseZzjg.setOnClickListener(this);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));

        mRv.setAdapter(adapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mDatas));
        mRv.addItemDecoration(new DividerItemDecoration(ContactsActivity.this, DividerItemDecoration.VERTICAL_LIST));
        initDatas();

    }

    private void initDatas() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sos:
                ToastTool.getInstance().shortLength(this, "一键报警", true);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_choose_zzjg:
                contactPresenter.queryZDDatasByZZJG("");
                showLoadingDialog("正在加载...");
                if(popwindow == null){
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int width = dm.widthPixels;
                    int height = dm.heightPixels;
                    popContentView = ContactsActivity.this.getLayoutInflater().inflate(R.layout.pop_contacts_zzjg, null);
                    tvChooseTitle = (TextView) popContentView.findViewById(R.id.tv_choose_title);
                    popwindow = new PopupWindow(popContentView, width, height);
//                    showPhoneNumpopwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
//                    showPhoneNumpopwindow.setFocusable(true);
//                    showPhoneNumpopwindow.setOutsideTouchable(false);
//
//                    if (tvZzjg.getText().toString() != null) {
//                        tvChooseTitle.setText(tvZzjg.getText().toString());
//                    }
//                    backgroundAlpha(0.3f);
                    RecyclerView rvPopChooseZZJG = (RecyclerView) popContentView.findViewById(R.id.rv_pop_zzjg);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rvPopChooseZZJG.setLayoutManager(linearLayoutManager);
                    rvPopChooseZZJG.setAdapter(commonAdapterZZJG);
                    rvPopChooseZZJG.addItemDecoration(new android.support.v7.widget.DividerItemDecoration
                            (ContactsActivity.this, android.support.v7.widget.DividerItemDecoration.VERTICAL));

                    RelativeLayout rlConfirm = (RelativeLayout) popContentView.findViewById(R.id.rl_confirm);
                    RelativeLayout rlCancel = (RelativeLayout) popContentView.findViewById(R.id.rl_cancel);
                    rlCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popwindow.dismiss();
                        }
                    });
                    rlConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            zzjg = tvChooseTitle.getText().toString();
                            contactPresenter.queryContactsByZZJG(dwbm);
                            popwindow.dismiss();
                        }
                    });
//                    showPhoneNumpopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            backgroundAlpha(1f);
//                        }
//                    });
                    contactPresenter.queryZDDatasByZZJG(Constant.ZDLX_ZZJG);
                    popwindow.showAtLocation(rlChooseZzjg, Gravity.CENTER_HORIZONTAL, 0, 0);
                }else{
                    popwindow.showAtLocation(rlChooseZzjg, Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 获取子机构
     *
     * @param allParentOffices
     * @param currentZD
     * @param level
     * @return
     */
    public List<EntityZD> getChildrenOffices(List<EntityZD> allParentOffices,
                                             EntityZD currentZD, int level) {
        List<EntityZD> selectedOffices = new ArrayList<EntityZD>();
        String currentOfficeBm = currentZD.getBm();
        switch (level) {
            case 1:
                EntityZD jujg = new EntityZD("320700000000", "局机关", "");
                selectedOffices.add(jujg);
                for (EntityZD office : allParentOffices) {
                    String officeBm = office.getBm();
                    if (officeBm.startsWith("3207") && officeBm.endsWith("000000")
                            && !officeBm.startsWith("320700")) {
                        selectedOffices.add(office);
                    }
                }
                break;
            case 2:
                //第二级,3207xx,xx=00为市局机关,xx!=00为分局
                if (currentOfficeBm.subSequence(4, 6).equals("00")) {// 上一级为市局机关
                    for (EntityZD office : allParentOffices) {
                        String officeBm = office.getBm();
                        String qz = currentOfficeBm.substring(0, 6);
                        int hzLength = 12 - qz.length() - 2;
                        StringBuffer hz = new StringBuffer();
                        if (hzLength > 0) {
                            for (int i = 0; i < hzLength; i++) {
                                hz.append("0");
                            }
                        }
                        if (officeBm.startsWith(qz) && officeBm.endsWith(hz.toString())) {
                            selectedOffices.add(office);
                        }
                    }
                } else {// 上一级为分局
                    String jigBm = currentOfficeBm.substring(0, 4 + (level - 1) * 2) + "000000";
                    selectedOffices.add(new EntityZD(jigBm, "分局机关", ""));//添加分局机关
                    for (EntityZD office : allParentOffices) {//添加派出所
                        String qz = currentOfficeBm.substring(0,
                                4 + (currentLevel - 1) * 2);
                        int hzLength = 12 - qz.length() - 2;
                        StringBuffer hz = new StringBuffer();
                        if (hzLength > 0) {
                            for (int i = 0; i < hzLength; i++) {
                                hz.append("0");
                            }
                        }
                        String officeBm = office.getBm();
                        String officeMc = office.getMc();
                        if (!officeBm.equals(currentOfficeBm) && officeBm.startsWith(qz) && officeBm.endsWith(hz.toString()) && officeMc.endsWith("派出所")) {
                            selectedOffices.add(office);
                        }
                    }
                }
                break;
            case 3:
                //第三级,320703xx,xx=00为分局机关,xx!=00为派出所
                if (currentOfficeBm.subSequence(6, 8).equals("00")) {// 上一级为分局机关
                    for (EntityZD office : allParentOffices) {
                        String qz = currentOfficeBm.substring(0, 4 + (currentLevel - 2) * 2);
                        int hzLength = 12 - qz.length() - 2;
                        StringBuffer hz = new StringBuffer();
                        if (hzLength > 0) {
                            for (int i = 0; i < hzLength; i++) {
                                hz.append("0");
                            }
                        }

                        String officeBm = office.getBm();
                        String officeMc = office.getMc();
                        if (!officeBm.equals(currentOfficeBm) && officeBm.startsWith(qz)
                                && officeBm.endsWith(hz.toString()) && !officeMc.endsWith("派出所")) {
                            selectedOffices.add(office);
                        }
                    }

                } else {// 上一级为分局派出所
                    for (EntityZD office : allParentOffices) {
                        String qz = currentOfficeBm.substring(0, 4 + (currentLevel - 1) * 2);
                        int hzLength = 12 - qz.length() - 2;
                        StringBuffer hz = new StringBuffer();
                        if (hzLength > 0) {
                            for (int i = 0; i < hzLength; i++) {
                                hz.append("0");
                            }
                        }

                        String officeBm = office.getBm();
                        if (!officeBm.equals(currentOfficeBm) && officeBm.startsWith(qz) && officeBm.endsWith(hz.toString())) {
                            selectedOffices.add(office);
                        }
                    }
                }

                break;
            case 4:
                for (EntityZD office : allParentOffices) {
                    String qz = currentOfficeBm.substring(0, 4 + (currentLevel - 1) * 2);
                    int hzLength = 12 - qz.length() - 2;
                    StringBuffer hz = new StringBuffer();
                    if (hzLength > 0) {
                        for (int i = 0; i < hzLength; i++) {
                            hz.append("0");
                        }
                    }

                    String officeBm = office.getBm();
                    if (!officeBm.equals(currentOfficeBm) && officeBm.startsWith(qz) && officeBm.endsWith(hz.toString())) {
                        selectedOffices.add(office);
                    }
                }
                break;

            default:
                break;
        }
        return selectedOffices;
    }

    /**
     * 查询所有组织机构成功的回调
     *
     * @param allEntitys
     */
    @Override
    public void queryZZJGSuccessed(List<EntityZD> allEntitys) {
        dismissLoadingDialog();
        initSourceDatas(allEntitys);
        mZZJGDatas.clear();
        mZZJGDatas.addAll(allEntitys);
        if (commonAdapterZZJG != null) {
            commonAdapterZZJG.notifyDataSetChanged();
        }
    }

    /**
     * 查询所有组织机构失败的回调
     *
     * @param msg
     */
    @Override
    public void queryZZJGUnSuccessed(String msg) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(this, msg, true);
    }

    /**
     * 通过组织机构查询联系人成功的回调
     *
     * @param contacts
     */
    @Override
    public void queryContactsByZZJGSuccessed(List<EntityContact> contacts) {
        dismissLoadingDialog();
        tvZzjg.setText(zzjg);
        mDatas.clear();
        mDatas.addAll(contacts);
        adapter.notifyDataSetChanged();
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)
                .setNeedRealIndex(true)
                .setmLayoutManager(mManager)
                .setmSourceDatas(mDatas)
                .invalidate();
        mDecoration.setmDatas(mDatas);
    }

    /**
     * 通过组织机构查询联系人失败的回调
     *
     * @param msg
     */
    @Override
    public void queryContactsByZZJGUnSuccessed(String msg) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(this, "暂无相关联系人！", true);
    }

    @Override
    public void queryContactsAndZZJGsByKeywordSuccessed(EntityContactsAndZZJGS contactsAndZZJGSes) {

        dismissLoadingDialog();
    }

    @Override
    public void queryContactsAndZZJGsByKeywordFail(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(this, e.getMessage(), true);

    }

    /**
     * 将数据源按拼音排序
     *
     * @param mSourceDatas
     */
    private void initSourceDatas(List<EntityZD> mSourceDatas) {
        convert(mSourceDatas);
        fillInexTag(mSourceDatas);
        //对数据源进行排序
        Collections.sort(mSourceDatas, new Comparator<BaseIndexPinyinBean>() {
            @Override
            public int compare(BaseIndexPinyinBean lhs, BaseIndexPinyinBean rhs) {
                if (!lhs.isNeedToPinyin()) {
                    return 0;
                } else if (!rhs.isNeedToPinyin()) {
                    return 0;
                } else if (lhs.getBaseIndexTag().equals("#")) {
                    return 1;
                } else if (rhs.getBaseIndexTag().equals("#")) {
                    return -1;
                } else {
                    return lhs.getBaseIndexPinyin().compareTo(rhs.getBaseIndexPinyin());
                }
            }
        });
    }

    /**
     * 字符转化为拼音
     *
     * @param datas
     */
    public void convert(List<? extends BaseIndexPinyinBean> datas) {
        if (null == datas || datas.isEmpty()) {
            return;
        }
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            BaseIndexPinyinBean indexPinyinBean = datas.get(i);
            StringBuilder pySb = new StringBuilder();
            //add by zhangxutong 2016 11 10 如果不是top 才转拼音，否则不用转了
            if (indexPinyinBean.isNeedToPinyin()) {
                String target = indexPinyinBean.getTarget();//取出需要被拼音化的字段
                //遍历target的每个char得到它的全拼音
                for (int i1 = 0; i1 < target.length(); i1++) {
                    //利用TinyPinyin将char转成拼音
                    //查看源码，方法内 如果char为汉字，则返回大写拼音
                    //如果c不是汉字，则返回String.valueOf(c)
                    pySb.append(Pinyin.toPinyin(target.charAt(i1)).toUpperCase());
                }
                indexPinyinBean.setBaseIndexPinyin(pySb.toString());//设置城市名全拼音
            } else {
                //pySb.append(indexPinyinBean.getBaseIndexPinyin());
            }
        }
        return;
    }

    /**
     * 拼音->tag
     *
     * @param datas
     */
    public void fillInexTag(List<? extends BaseIndexPinyinBean> datas) {
        if (null == datas || datas.isEmpty()) {
            return;
        }
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            BaseIndexPinyinBean indexPinyinBean = datas.get(i);
            if (indexPinyinBean.isNeedToPinyin()) {
                //以下代码设置城市拼音首字母
                String tagString = indexPinyinBean.getBaseIndexPinyin().toString().substring(0, 1);
                if (tagString.matches("[A-Z]")) {//如果是A-Z字母开头
                    indexPinyinBean.setBaseIndexTag(tagString);
                } else {//特殊字母这里统一用#处理
                    indexPinyinBean.setBaseIndexTag("#");
                }
            }
        }
        return;
    }

}
