package com.logoocc.light_netlistview.bean;

/**
 * Created by samchen on 8/26/15.
 */
public class Result<T> {

    public final static int STATUS_SUC = 100;
    public final static int STATUS_FIAL = 101;
    public final static int STATUS_PARAM_ERR = 102;
    public final static int STATUS_NO_MODE = 103;

    public int status;
    public String msg;
    public T data;

}
