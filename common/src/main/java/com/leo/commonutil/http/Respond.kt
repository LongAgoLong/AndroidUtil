package com.leo.commonutil.http

import android.os.Parcel
import android.os.Parcelable

data class Respond(var code: Int, var body: String?) : Parcelable {

    constructor() : this(0, "") {

    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Respond> {
        override fun createFromParcel(parcel: Parcel): Respond {
            return Respond(parcel)
        }

        override fun newArray(size: Int): Array<Respond?> {
            return arrayOfNulls(size)
        }
    }

}