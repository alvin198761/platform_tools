package org.alvin.office;

import java.util.List;

/**
 * @author 唐植超
 * @date 2020/05/15
 */
public class ParamObject {

    private String vendor;
    private String auth;
    private String time;
    private String cmd;
    private String interval;
    private String remark;
    private String example;
    private List<ParamItem> fList;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public List<ParamItem> getfList() {
        return fList;
    }

    public void setfList(List<ParamItem> fList) {
        this.fList = fList;
    }


    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
