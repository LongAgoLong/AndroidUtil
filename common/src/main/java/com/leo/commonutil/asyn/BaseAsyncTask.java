package com.leo.commonutil.asyn;

import android.os.AsyncTask;

public abstract class BaseAsyncTask<T> extends AsyncTask<Object, Void, T> {
    protected OnAsynListener<T> mOnAsynListener;

    public void setOnAsynListener(OnAsynListener<T> onAsynListener) {
        this.mOnAsynListener = onAsynListener;
    }

    @Override
    protected void onPreExecute() {
        if (null!=mOnAsynListener){
            mOnAsynListener.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(T result) {
        if (null!=mOnAsynListener){
            mOnAsynListener.onPostExecute(result);
        }
    }

}
