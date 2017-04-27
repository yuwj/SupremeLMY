package com.zondy.jwt.jwtmobile.presenter;

/**
 * Created by sheep on 2017/1/17.
 */

public interface IContactPresenter {
    void queryZDDatasByZZJG(String zdlx);
    void queryContactsByZZJG(String zzjg);

    /**
     * 通过父级组织机构查询子级组织结构
     * @param parentZZJG
     */
    void queryZZJGByParentZZJG(String parentZZJG);

    /**
     * 根据关键字查联系人或组织机构
     * @param keyword
     */
    void queryContactsAndZZJGSByKeyword(String keyword);
}
