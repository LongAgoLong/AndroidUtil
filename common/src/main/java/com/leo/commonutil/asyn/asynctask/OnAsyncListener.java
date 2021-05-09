package com.leo.commonutil.asyn.asynctask;

public interface OnAsyncListener<T> {
    void onPreExecute();

    void onPostExecute(T result);
}
