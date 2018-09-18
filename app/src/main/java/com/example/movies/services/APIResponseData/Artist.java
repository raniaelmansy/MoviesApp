package com.example.movies.services.APIResponseData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Artist implements Parcelable {

	@SerializedName("popularity")
	private double popularity;

	@SerializedName("known_for")
	private List<KnownForItem> knownFor;

	@SerializedName("name")
	private String name;

	@SerializedName("profile_path")
	private String profilePath;

	@SerializedName("id")
	private int id;

	@SerializedName("adult")
	private boolean adult;


    @SerializedName("birthday")
	private String birthday;

    @SerializedName("biography")
    private String biography;

    @SerializedName("place_of_birth")
    private String placeOfBirth;

    @SerializedName("known_for_department")
    private String knownForDepartment;

    public String getKnownForDepartment() {
        return knownForDepartment;
    }

    public void setKnownForDepartment(String knownForDepartment) {
        this.knownForDepartment = knownForDepartment;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPopularity(double popularity){
		this.popularity = popularity;
	}

	public double getPopularity(){
		return popularity;
	}

	public void setKnownFor(List<KnownForItem> knownFor){
		this.knownFor = knownFor;
	}

	public List<KnownForItem> getKnownFor(){
		return knownFor;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setProfilePath(String profilePath){
		this.profilePath = profilePath;
	}

	public String getProfilePath(){
		return profilePath;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAdult(boolean adult){
		this.adult = adult;
	}

	public boolean isAdult(){
		return adult;
	}

	@Override
 	public String toString(){
		return 
			"Artist{" +
			"popularity = '" + popularity + '\'' + 
			",known_for = '" + knownFor + '\'' + 
			",name = '" + name + '\'' + 
			",profile_path = '" + profilePath + '\'' + 
			",id = '" + id + '\'' + 
			",adult = '" + adult + '\'' + 
			"}";
		}

	public Artist() {
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.popularity);
        dest.writeList(this.knownFor);
        dest.writeString(this.name);
        dest.writeString(this.profilePath);
        dest.writeInt(this.id);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.birthday);
        dest.writeString(this.biography);
        dest.writeString(this.placeOfBirth);
        dest.writeString(this.knownForDepartment);
    }

    protected Artist(Parcel in) {
        this.popularity = in.readDouble();
        this.knownFor = new ArrayList<KnownForItem>();
        in.readList(this.knownFor, KnownForItem.class.getClassLoader());
        this.name = in.readString();
        this.profilePath = in.readString();
        this.id = in.readInt();
        this.adult = in.readByte() != 0;
        this.birthday = in.readString();
        this.biography = in.readString();
        this.placeOfBirth = in.readString();
        this.knownForDepartment = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}