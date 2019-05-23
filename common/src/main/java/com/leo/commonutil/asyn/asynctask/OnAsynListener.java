package com.leo.commonutil.asyn.asynctask;

public interface OnAsynListener <T> {
    void onPreExecute();

    void onPostExecute(T result);
}
