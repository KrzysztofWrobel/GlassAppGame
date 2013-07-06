package com.hackato.GlassAppGame;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alek on 7/6/13.
 */
public class Challenge implements Parcelable {
	private String id;
	private String description;
	private String points;
	private int latitude, longitude;
	private String image_link;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Challenge challenge = (Challenge) o;

		if (!id.equals(challenge.id)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(description);
		dest.writeString(points);
		dest.writeInt(latitude);
		dest.writeInt(longitude);
		dest.writeString(image_link);
	}

	public Challenge(Parcel in) {
		id = in.readString();
		description = in.readString();
		points = in.readString();
		latitude = in.readInt();
		longitude = in.readInt();
		image_link = in.readString();
	}
}
