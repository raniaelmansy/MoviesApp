package com.example.movies.services.APIResponseData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistProfileResponse{

	@SerializedName("profiles")
	private List<ProfilesItem> profiles;

	@SerializedName("id")
	private int id;

	public void setProfiles(List<ProfilesItem> profiles){
		this.profiles = profiles;
	}

	public List<ProfilesItem> getProfiles(){
		return profiles;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"ArtistProfileResponse{" + 
			"profiles = '" + profiles + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}