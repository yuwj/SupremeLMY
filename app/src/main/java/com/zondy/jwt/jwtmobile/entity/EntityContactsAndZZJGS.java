package com.zondy.jwt.jwtmobile.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ywj on 2017/4/26 0026.
 */

public class EntityContactsAndZZJGS implements Serializable {
    List<EntityContact> contactList;
    List<EntityZD> zzjgList;

    public List<EntityContact> getContactList() {
        return contactList;
    }

    public void setContactList(List<EntityContact> contactList) {
        this.contactList = contactList;
    }

    public List<EntityZD> getZzjgList() {
        return zzjgList;
    }

    public void setZzjgList(List<EntityZD> zzjgList) {
        this.zzjgList = zzjgList;
    }
}
