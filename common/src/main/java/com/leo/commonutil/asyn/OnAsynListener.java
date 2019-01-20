package com.leo.commonutil.asyn;

public interface OnAsynListener <T> {
    void onPreExecute();

    void onPostExecute(T result);
}
