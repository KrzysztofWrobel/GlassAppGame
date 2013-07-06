package com.github.krzysztofwrobel.glassappgame.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alek on 7/6/13.
 */
public class Challenge implements Parcelable {
	private String id;
	private String description;
	private int points;
	private int latitude, longitude;
	private String image_link;
    private String title;

	@Override
	public String toString() {
		return "Challenge{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				", points='" + points + '\'' +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", image_link='" + image_link + '\'' +
				", title='" + title + '\'' +
				'}';
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public static final Parcelable.Creator<Challenge> CREATOR
            = new Parcelable.Creator<Challenge>() {
        public Challenge createFromParcel(Parcel in) {
            return new Challenge(in);
        }

        public Challenge[] newArray(int size) {
            return new Challenge[size];
        }
    };

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(description);
		dest.writeInt(points);
		dest.writeInt(latitude);
		dest.writeInt(longitude);
		dest.writeString(image_link);
        dest.writeString(title);
	}

	public Challenge(Parcel in) {
		id = in.readString();
		description = in.readString();
		points = in.readInt();
		latitude = in.readInt();
		longitude = in.readInt();
		image_link = in.readString();
        title = in.readString();
	}
}
