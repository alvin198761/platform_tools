/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alvin.api.ui.renderer;

/**
 *
 * @author tangzhichao
 */
public class PlatTypeItem {

    private String title;
    private String value;
    private String url;

    private boolean comment;
    private boolean hasNull;
    private boolean javaFiled;
    private boolean onlyName;
    private boolean toStringVar;
    private String searchText;
    private boolean privateKeyWork;

    public PlatTypeItem(String title, String value, String url) {
        this.title = title;
        this.value = value;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return title;
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public boolean isHasNull() {
        return hasNull;
    }

    public void setHasNull(boolean hasNull) {
        this.hasNull = hasNull;
    }

    public boolean isJavaFiled() {
        return javaFiled;
    }

    public void setJavaFiled(boolean javaFiled) {
        this.javaFiled = javaFiled;
    }

    public boolean isOnlyName() {
        return onlyName;
    }

    public void setOnlyName(boolean onlyName) {
        this.onlyName = onlyName;
    }

    public boolean isToStringVar() {
        return toStringVar;
    }

    public void setToStringVar(boolean toStringVar) {
        this.toStringVar = toStringVar;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public boolean isPrivateKeyWork() {
        return privateKeyWork;
    }

    public void setPrivateKeyWork(boolean privateKeyWork) {
        this.privateKeyWork = privateKeyWork;
    }
    
    

}
