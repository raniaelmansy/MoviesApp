package com.example.movies.services.APIResponseData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularArtistsResponse{

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<Artist> artists;

	@SerializedName("total_results")
	private int totalResults;

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<Artist> results){
		this.artists = results;
	}

	public List<Artist> getResults(){
		return artists;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
 	public String toString(){
		return 
			"PopularArtistsResponse{" + 
			"page = '" + page + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",results = '" + artists + '\'' +
			",total_results = '" + totalResults + '\'' + 
			"}";
		}
}