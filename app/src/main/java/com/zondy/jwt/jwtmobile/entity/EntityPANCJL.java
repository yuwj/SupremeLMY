package com.zondy.jwt.jwtmobile.entity;

/**
 * Created by sheep on 2017/3/16.
 */

public class EntityPANCJL {
    private String pclx;//盘查类型 1：查人 2：查车
    private String pczt;//盘查状态 1：正常 2：可疑
    private String pcsj;
    private String cheph;
    private String chepp;
    private String chesyr;
    private String chesyrhm;
    private String xm;
    private String xb;
    private String mz;
    private String sfzh;
    private String dz;

    public EntityPANCJL() {
    }

    public EntityPANCJL(String pclx, String pczt, String pcsj, String cheph,
                        String chepp, String chesyr, String chesyrhm, String xm, String xb, String mz, String sfzh, String dz) {
        this.pclx = pclx;
        this.pczt = pczt;
        this.pcsj = pcsj;
        this.cheph = cheph;
        this.chepp = chepp;
        this.chesyr = chesyr;
        this.chesyrhm = chesyrhm;
        this.xm = xm;
        this.xb = xb;
        this.mz = mz;
        this.sfzh = sfzh;
        this.dz = dz;
    }

    public String getPclx() {
        return pclx;
    }

    public void setPclx(String pclx) {
        this.pclx = pclx;
    }

    public String getPczt() {
        return pczt;
    }

    public void setPczt(String pczt) {
        this.pczt = pczt;
    }

    public String getPcsj() {
        return pcsj;
    }

    public void setPcsj(String pcsj) {
        this.pcsj = pcsj;
    }

    public String getCheph() {
        return cheph;
    }

    public void setCheph(String cheph) {
        this.cheph = cheph;
    }

    public String getChepp() {
        return chepp;
    }

    public void setChepp(String chepp) {
        this.chepp = chepp;
    }

    public String getChesyr() {
        return chesyr;
    }

    public void setChesyr(String chesyr) {
        this.chesyr = chesyr;
    }

    public String getChesyrhm() {
        return chesyrhm;
    }

    public void setChesyrhm(String chesyrhm) {
        this.chesyrhm = chesyrhm;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }
}
