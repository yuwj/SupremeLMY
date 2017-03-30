package com.zondy.jwt.jwtmobile.entity;

/**
 * 分页参数.
 * Created by ywj on 2017/3/25 0025.
 */

public class EntityPage {
    public static final int DEFAULT_PAGE_SIZE = 10;

    private int pageSize;

    private int pageNo;

    private int totalCount;

    private int nextPage;

    private int prePage;

    private int totalPages;

    private int startRow;

    public EntityPage() {
        this.pageNo = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.totalPages = 100;
    }

    public EntityPage(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        setStartRow(getComputeStartRow());
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        setTotalPages(getComputeTotalPages());
        setPrePage(getComputePrePage());
        setNextPage(getComputeNextPage());
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    //开始读取的行数
    public int getComputeStartRow() {
        return (this.pageNo-1)*this.pageSize;
    }

    //总页数
    public int getComputeTotalPages() {
        int page = this.totalCount/this.pageSize;
        if(this.totalCount%this.pageSize!=0){
            page++;
        }
        return page;
    }

    //前一页的页码
    public int getComputePrePage() {
        int page = this.pageNo;
        page--;
        if(page<1){
            page++;
        }
        return page;
    }

    //后一页的页码
    public int getComputeNextPage() {
        int page = this.pageNo;
        page++;
        if(page>this.getTotalPages()){
            page--;
        }
        return page;
    }

}
