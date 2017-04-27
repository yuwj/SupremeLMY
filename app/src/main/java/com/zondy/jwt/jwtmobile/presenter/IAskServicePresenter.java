package com.zondy.jwt.jwtmobile.presenter;

import com.zondy.jwt.jwtmobile.entity.EntityAskBuk;
import com.zondy.jwt.jwtmobile.entity.EntityAskChax;
import com.zondy.jwt.jwtmobile.entity.EntityAskQit;
import com.zondy.jwt.jwtmobile.entity.EntityAskZengy;
import com.zondy.jwt.jwtmobile.entity.EntityAskZous;

/**
 * 请求服务
 * Created by ywj on 2017/3/23 0023.
 */

public interface IAskServicePresenter {
    public void queryAskServiceList(String jh, String simid, String xingm, int pageSize, int pageNo);

    public void queryAskBukDetail(String jh,String simid,String askBukId);
    public void queryAskZengyDetail(String jh,String simid,String askZengyId);
    public void queryAskChaxDetail(String jh,String simid,String askChaxId);
    public void queryAskZousDetail(String jh,String simid,String askZousId);
    public void queryAskQitDetail(String jh,String simid,String askQitId);

    //请求布控
    public void askBuk(String jh,String simid, EntityAskBuk entityAskForBuk);
    public void askZengy(String jh,String simid, EntityAskZengy entityAskZengy);
    public void askChax(String jh, String simid, EntityAskChax entityAskChax);
    public void askZous(String jh, String simid, EntityAskZous entityAskZous);
    public void askQit(String jh, String simid, EntityAskQit entityAskQit);
}
