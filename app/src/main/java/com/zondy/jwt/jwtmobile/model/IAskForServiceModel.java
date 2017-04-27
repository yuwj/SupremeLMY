package com.zondy.jwt.jwtmobile.model;

import com.zondy.jwt.jwtmobile.callback.IAskBukCallback;
import com.zondy.jwt.jwtmobile.callback.IAskChaxCallback;
import com.zondy.jwt.jwtmobile.callback.IAskQitCallback;
import com.zondy.jwt.jwtmobile.callback.IAskZengyCallback;
import com.zondy.jwt.jwtmobile.callback.IAskZousCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskBukDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskChaxDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskQitDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskServiceListCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskZengyDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskZousDetailCallback;
import com.zondy.jwt.jwtmobile.entity.EntityAskBuk;
import com.zondy.jwt.jwtmobile.entity.EntityAskChax;
import com.zondy.jwt.jwtmobile.entity.EntityAskQit;
import com.zondy.jwt.jwtmobile.entity.EntityAskZengy;
import com.zondy.jwt.jwtmobile.entity.EntityAskZous;

/**
 * 推送通知处理模块
 * Created by ywj on 2017/3/23 0023.
 */

public interface IAskForServiceModel {

    /**
     * 查询已请求的服务列表
     *
     * @param jh
     * @param simid
     * @param xingm
     * @param pageSize
     * @param pageNo
     * @param queryAskServiceListCallback
     */
    public void queryAskServiceList(String jh, String simid, String xingm, int pageSize, int pageNo, IQueryAskServiceListCallback queryAskServiceListCallback);

    /**
     * 查询请求布控详情
     *
     * @param jh
     * @param simid
     * @param askBukServiceId
     */
    public void queryAskBukDetail(String jh, String simid, String askBukServiceId, IQueryAskBukDetailCallback queryAskBukDetailCallback);
    /**
     * 查询请求增援详情
     *
     * @param jh
     * @param simid
     * @param askZengyId
     */
    public void queryAskZengyDetail(String jh, String simid, String askZengyId, IQueryAskZengyDetailCallback queryAskZengyDetailCallback);

    /**
     * 查询请求查询详情
     *
     * @param jh
     * @param simid
     * @param askChaxId
     */
    public void queryAskChaxDetail(String jh, String simid, String askChaxId, IQueryAskChaxDetailCallback queryAskChaxDetailCallback);

    /**
     * 查询请求走失详情
     *
     * @param jh
     * @param simid
     * @param askZousId
     */
    public void queryAskZousDetail(String jh, String simid, String askZousId, IQueryAskZousDetailCallback queryAskZousDetailCallback);

    /**
     * 查询请求其他详情
     *
     * @param jh
     * @param simid
     * @param askQitId
     */
    public void queryAskQitDetail(String jh, String simid, String askQitId, IQueryAskQitDetailCallback queryAskQitDetailCallback);


    /**
     * 请求布控
     *
     * @param entityAskForBuk
     */
    public void askBuk(String jh,String sidmid,EntityAskBuk entityAskForBuk, IAskBukCallback askBukCallback);

    /**
     * 请求增援
     * @param jh
     * @param sidmid
     * @param entityAskZengy
     * @param askZengyCallback
     */
    public void askZengy(String jh, String sidmid, EntityAskZengy entityAskZengy, IAskZengyCallback askZengyCallback);

    /**
     * 请求查询
     * @param jh
     * @param sidmid
     * @param entityAskChax
     * @param askChaxCallback
     */
    public void askChax(String jh, String sidmid, EntityAskChax entityAskChax, IAskChaxCallback askChaxCallback);

    /**
     * 请求走失
     * @param jh
     * @param sidmid
     * @param entityAskZous
     * @param askZousCallback
     */
    public void askZous(String jh, String sidmid, EntityAskZous entityAskZous, IAskZousCallback askZousCallback);

    /**
     * 请求其他
     * @param jh
     * @param sidmid
     * @param entityAskQit
     * @param askQitCallback
     */
    public void askQit(String jh, String sidmid, EntityAskQit entityAskQit, IAskQitCallback askQitCallback);

}
