package com.example.kothakhali.activity.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AdList(
        @PrimaryKey
        val _id: String = "",
        val clientid: String? = null,
        val adtitle: String? = null,
        val pradesh: String? = null,
        val district: String? = null,
        val city: String? = null,
        val street: String? = null,
        val photo: String? = null,
        val category: String? = null,
        val rent: String? = null,
        val negotiable: String? = null,
        val status: String? = null,
        val description: String? = null,
        val postedAt: String? = null
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(_id)
                parcel.writeString(clientid)
                parcel.writeString(adtitle)
                parcel.writeString(pradesh)
                parcel.writeString(district)
                parcel.writeString(city)
                parcel.writeString(street)
                parcel.writeString(photo)
                parcel.writeString(category)
                parcel.writeString(rent)
                parcel.writeString(negotiable)
                parcel.writeString(status)
                parcel.writeString(description)
                parcel.writeString(postedAt)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<AdList> {
                override fun createFromParcel(parcel: Parcel): AdList {
                        return AdList(parcel)
                }

                override fun newArray(size: Int): Array<AdList?> {
                        return arrayOfNulls(size)
                }
        }
}
