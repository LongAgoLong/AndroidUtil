package com.leo.commonutil.asyn.asynctask;

import android.os.AsyncTask;

public abstract class BaseAsyncTask<T> extends AsyncTask<Object, Void, T> {
    protected OnAsyncListener<T> mOnAsyncListener;

    public void setOnAsyncListener(OnAsyncListener<T> onAsyncListener) {
        this.mOnAsyncListener = onAsyncListener;
    }

    @Override
    protected void onPreExecute() {
        if (null != mOnAsyncListener) {
            mOnAsyncListener.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(T result) {
        if (null != mOnAsyncListener) {
            mOnAsyncListener.onPostExecute(result);
        }
    }

}
