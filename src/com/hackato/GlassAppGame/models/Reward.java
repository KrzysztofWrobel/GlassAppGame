package com.hackato.GlassAppGame.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alek on 7/6/13.
 */
public class Reward implements Parcelable {
	private String id;
	private String description;
	private int latitude;
	private int longitude;
	private String image_link;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage_link() {
		return image_link;
	}

	public void setImage_link(String image_link) {
		this.image_link = image_link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(description);
		dest.writeInt(latitude);
		dest.writeInt(longitude);
		dest.writeString(image_link);
	}

	public Reward(Parcel in) {
		id = in.readString();
		description = in.readString();
		latitude = in.readInt();
		longitude = in.readInt();
		image_link = in.readString();
	}
}
