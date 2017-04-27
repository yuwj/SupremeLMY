package com.zondy.jwt.jwtmobile.presenter.impl;

import android.content.Context;

import com.zondy.jwt.jwtmobile.callback.ILoginCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryContactsAndZZJGSByKeywordCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryContactsByZZJGCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryZZJGCallback;
import com.zondy.jwt.jwtmobile.entity.EntityContact;
import com.zondy.jwt.jwtmobile.entity.EntityContactsAndZZJGS;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.entity.EntityZD;
import com.zondy.jwt.jwtmobile.model.IContactModel;
import com.zondy.jwt.jwtmobile.model.ILoginModel;
import com.zondy.jwt.jwtmobile.model.impl.ContactModelImpl;
import com.zondy.jwt.jwtmobile.model.impl.LoginModelImpl;
import com.zondy.jwt.jwtmobile.presenter.IContactPresenter;
import com.zondy.jwt.jwtmobile.view.IContactView;
import com.zondy.jwt.jwtmobile.view.ILoginView;

import java.util.List;

import static android.provider.CallLog.Calls.NEW;

/**
 * Created by sheep on 2017/1/17.
 */

public class ContactPresenterImpl implements IContactPresenter{
    private IContactView contactView;
    private IContactModel contactModel;
    Context context;
    public ContactPresenterImpl(Context context,IContactView contactView){
        super();
        this.context=context;
        this.contactView=contactView;
        contactModel=new ContactModelImpl();
    }


    @Override
    public void queryZDDatasByZZJG(String zdlx) {
        contactModel.queryZZJG(context,zdlx, new IQueryZZJGCallback() {
            @Override
            public void querySuccessed(List<EntityZD> allEntitys) {
                contactView.queryZZJGSuccessed(allEntitys);
            }

            @Override
            public void queryUnSuccessed(String msg) {
                contactView.queryZZJGUnSuccessed(msg);
            }
        });
    }

    @Override
    public void queryContactsByZZJG(String zzjg) {
        contactModel.queryContactsByZZJG(context, zzjg, new IQueryContactsByZZJGCallback() {
            @Override
            public void querySuccessed(List<EntityContact> contacts) {
                contactView.queryContactsByZZJGSuccessed(contacts);
            }

            @Override
            public void queryUnSuccessed(String msg) {
                contactView.queryContactsByZZJGUnSuccessed(msg);
            }
        });
    }

    @Override
    public void queryZZJGByParentZZJG(String parentZZJG) {
        contactModel.queryZZJGByParentZZJG(context,parentZZJG,new IQueryZZJGCallback(){
            @Override
            public void querySuccessed(List<EntityZD> allEntitys) {
                contactView.queryZZJGSuccessed(allEntitys);
            }

            @Override
            public void queryUnSuccessed(String msg) {
                contactView.queryZZJGUnSuccessed(msg);

            }
        });
    }

    @Override
    public void queryContactsAndZZJGSByKeyword(String keyword) {
        contactModel.queryContactsAndZZJGSByKeyword(context, keyword, new IQueryContactsAndZZJGSByKeywordCallback() {
            @Override
            public void onQueryContactsAndZZJGsSuccess(EntityContactsAndZZJGS result) {
                contactView.queryContactsAndZZJGsByKeywordSuccessed(result);
            }

            @Override
            public void onQueryContactsAndZZJGsFail(Exception e) {
                contactView.queryContactsAndZZJGsByKeywordFail(e);
            }
        });
    }
}
