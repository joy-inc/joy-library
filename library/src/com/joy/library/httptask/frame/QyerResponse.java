package com.joy.library.httptask.frame;

public class QyerResponse<T> {

    public static final int STATUS_PARSE_BROKEN = -1;// 如果status为-1，表明服务器返回JSON格式有误
    public static final int STATUS_NONE = 0;
    public static final int STATUS_SUCCESS = 1;


    private int status;
    private String info = "";
    private T data;

    public QyerResponse() {

    }

    public QyerResponse(int status, String info) {

        setStatus(status);
        setInfo(info);
    }

    public void setInfo(String info) {

        if (info == null) {

            info = "";
        } else {

            info = info.trim();
        }

        this.info = info;
    }

    public String getInfo() {

        return info;
    }

    public void setStatus(int status) {

        this.status = status;
    }

    public void setParseBrokenStatus() {

        this.status = STATUS_PARSE_BROKEN;
    }

    public int getStatus() {

        return status;
    }

    public void setData(T data) {

        this.data = data;
    }

    public T getData() {

        return data;
    }

    public boolean isSuccess() {

        return this.status == STATUS_SUCCESS;
    }

    public boolean isFailed() {

        return this.status != STATUS_SUCCESS;
    }

    public boolean isParseBroken() {

        return this.status == STATUS_PARSE_BROKEN;
    }
}
