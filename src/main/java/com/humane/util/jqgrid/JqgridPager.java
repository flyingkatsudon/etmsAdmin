package com.humane.util.jqgrid;

import java.util.ArrayList;
import java.util.List;

public class JqgridPager {

    private Integer page;
    private Integer rows;
    private String sidx;
    private String sord;

    public int getPage() {
        return page == null ? 1 : page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows == null ? 1000 : rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public String[] getSort() {
        if (sidx == null || sidx.isEmpty()) return null;

        List<String> l = new ArrayList<>();
        String s = sidx + " " + sord;

        String[] a = s.split(",");
        for (String string : a) {
            String[] b = string.trim().split(" ");
            l.add(b[0] + "," + b[1]);
        }
        return l.toArray(new String[l.size()]);
    }
}