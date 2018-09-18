package com.example.movies.services.APIResponseData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class ProfilesItem implements Parcelable {

	@SerializedName("file_path")
	private String filePath;

	@SerializedName("aspect_ratio")
	private double aspectRatio;

	@SerializedName("vote_average")
	private double voteAverage;

	@SerializedName("width")
	private int width;

	@SerializedName("iso_639_1")
	private Object iso6391;

	@SerializedName("vote_count")
	private int voteCount;

	@SerializedName("height")
	private int height;

	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	public String getFilePath(){
		return filePath;
	}

	public void setAspectRatio(double aspectRatio){
		this.aspectRatio = aspectRatio;
	}

	public double getAspectRatio(){
		return aspectRatio;
	}

	public void setVoteAverage(double voteAverage){
		this.voteAverage = voteAverage;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getWidth(){
		return width;
	}

	public void setIso6391(Object iso6391){
		this.iso6391 = iso6391;
	}

	public Object getIso6391(){
		return iso6391;
	}

	public void setVoteCount(int voteCount){
		this.voteCount = voteCount;
	}

	public int getVoteCount(){
		return voteCount;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int getHeight(){
		return height;
	}

	@Override
 	public String toString(){
		return 
			"ProfilesItem{" + 
			"file_path = '" + filePath + '\'' + 
			",aspect_ratio = '" + aspectRatio + '\'' + 
			",vote_average = '" + voteAverage + '\'' + 
			",width = '" + width + '\'' + 
			",iso_639_1 = '" + iso6391 + '\'' + 
			",vote_count = '" + voteCount + '\'' + 
			",height = '" + height + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.filePath);
		dest.writeDouble(this.aspectRatio);
		dest.writeDouble(this.voteAverage);
		dest.writeInt(this.width);
		dest.writeInt(this.voteCount);
		dest.writeInt(this.height);
	}

	public ProfilesItem() {
	}

	protected ProfilesItem(Parcel in) {
		this.filePath = in.readString();
		this.aspectRatio = in.readDouble();
		this.voteAverage = in.readDouble();
		this.width = in.readInt();
		this.voteCount = in.readInt();
		this.height = in.readInt();
	}

	public static final Parcelable.Creator<ProfilesItem> CREATOR = new Parcelable.Creator<ProfilesItem>() {
		@Override
		public ProfilesItem createFromParcel(Parcel source) {
			return new ProfilesItem(source);
		}

		@Override
		public ProfilesItem[] newArray(int size) {
			return new ProfilesItem[size];
		}
	};
}