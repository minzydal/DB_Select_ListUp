package com.example.dbselectlistup.view.upgrademode;

public class UpgradeListItem {
    private boolean isChecked;
    private int vci;
    private String eventtxt;
    private String typetxt;
    private String statustxt;
    private int tsbtxt;

    public UpgradeListItem() {
    }

    public UpgradeListItem(boolean checked, int vciimg, String eventtxt, String typetxt, String statustxt, int tsbtxt) {
        this.isChecked = checked;
        this.vci = vciimg;
        this.eventtxt = eventtxt;
        this.typetxt = typetxt;
        this.statustxt = statustxt;
        this.tsbtxt = tsbtxt;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public int getVci() {
        return vci;
    }

    public String getEventtxt() {
        return eventtxt;
    }

    public void setEventtxt(String eventtxt) {
        this.eventtxt = eventtxt;
    }

    public String getTypetxt() {
        return typetxt;
    }

    public String getStatustxt() {
        return statustxt;
    }

    public int getTsbtxt() {
        return tsbtxt;
    }

}
