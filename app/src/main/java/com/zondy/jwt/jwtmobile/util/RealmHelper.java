package com.zondy.jwt.jwtmobile.util;

import android.content.Context;

import com.zondy.jwt.jwtmobile.entity.EntitySearchHistory;


import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by sheep
 */

public class RealmHelper {
    public static final String DB_NAME = "myRealm.realm";
    private Realm mRealm;


    public RealmHelper(Context context) {

        mRealm = Realm.getDefaultInstance();
    }

    /**
     * add （增）
     */
    public void addHistory(final EntitySearchHistory history) {
        mRealm.beginTransaction();
//        mRealm.copyToRealm(history);
        mRealm.copyToRealmOrUpdate(history);
        mRealm.commitTransaction();

    }

    /**
     * delete （删）
     */
    public void deleteHistory(String id) {
        EntitySearchHistory history = mRealm.where(EntitySearchHistory.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        history.deleteFromRealm();
        mRealm.commitTransaction();

    }
    /**
     * delete （删除所有）
     */
    public void deleteAllhistory() {
        RealmResults<EntitySearchHistory> histories = mRealm.where(EntitySearchHistory.class).findAll();
        mRealm.beginTransaction();
        histories.deleteAllFromRealm();
        mRealm.commitTransaction();
    }
    /**
     * update （改）
     */
//    public void updateDog(String id, String newName) {
//        Dog dog = mRealm.where(Dog.class).equalTo("id", id).findFirst();
//        mRealm.beginTransaction();
//        dog.setName(newName);
//        mRealm.commitTransaction();
//    }

    /**
     * query （查询所有）
     */
    public List<EntitySearchHistory> queryAllhistory() {
        RealmResults<EntitySearchHistory> histories = mRealm.where(EntitySearchHistory.class).findAll();
        /**
         * 对查询结果，按Id进行排序，只能对查询结果进行排序
         */
        //增序排列
        histories=histories.sort("id", Sort.DESCENDING);
//        //降序排列
//        dogs=dogs.sort("id", Sort.DESCENDING);
        return mRealm.copyFromRealm(histories);
    }

    /**
     * query （根据Id（主键）查）
     */
    public EntitySearchHistory queryHistoryById(String id) {
        EntitySearchHistory history = mRealm.where(EntitySearchHistory.class).equalTo("id", id).findFirst();

        return history;
    }


    /**
     * query （根据age查）
     */
//    public List<Dog> queryDobByAge(int age) {
//        RealmResults<Dog> dogs = mRealm.where(Dog.class).equalTo("age", age).findAll();
//
//        return mRealm.copyFromRealm(dogs);
//    }

    public boolean isHistoryExist(String id){
        EntitySearchHistory history=mRealm.where(EntitySearchHistory.class).equalTo("id",id).findFirst();
        if (history==null){
            return false;
        }else {
            return  true;
        }
    }

    public Realm getRealm(){

        return mRealm;
    }

    public void close(){
        if (mRealm!=null){
            mRealm.close();
        }
    }
}
