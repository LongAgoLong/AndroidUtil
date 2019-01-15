package com.leo.commonutil.http;

import android.os.Parcel;
import android.os.Parcelable;

public class Respond implements Parcelable {
    private int code;
    private String body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.body);
    }

    public Respond() {
    }

    protected Respond(Parcel in) {
        this.code = in.readInt();
        this.body = in.readString();
    }

    public static final Parcelable.Creator<Respond> CREATOR = new Parcelable.Creator<Respond>() {
        @Override
        public Respond createFromParcel(Parcel source) {
            return new Respond(source);
        }

        @Override
        public Respond[] newArray(int size) {
            return new Respond[size];
        }
    };
}
