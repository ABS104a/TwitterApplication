package com.abs104a.core_data;



import twitter4j.Status;

public class TwitterStatus  {
	
	private Status status;

	public TwitterStatus(twitter4j.Status status){
		this.status = status;
	}

	public final Status getStatus() {
		return status;
	}

	public final void setStatus(Status status) {
		this.status = status;
	}
	
	
	
	/*
	private int accessLevel;
	private long[] contributors;
	private long currentUserRetweetId;
	private Date createdAt;
	private int favoriteCount;
	private GeoLocation geoLocation;
	private HashtagEntity[] hachtagEntities;
	private long id;
	private long inReplyToStatusId;
	private String inReplyToScreenName;
	private String isoLanguageCode;
	private MediaEntity[] mediaEntities;
	private Place place;
	private RateLimitStatus rateLimitStatus;
	private int retweetCount;
	private TwitterStatus retweetedStatus;
	private String source;
	private SymbolEntity[] symbolEntities;
	private String text;
	private URLEntity[] URLEntities;
	private User user;
	private UserMentionEntity[] userMentionEntities;
	private boolean isFavorited;
	private boolean isPossiblySensitive;
	private boolean isRetweet;
	private boolean isRetweeted;
	private boolean isRetweetedByMe;
	private boolean isTruncated;

	public TwitterStatus(twitter4j.Status status){
		accessLevel = status.getAccessLevel();
		contributors = status.getContributors();
		createdAt = status.getCreatedAt();
		currentUserRetweetId = status.getCurrentUserRetweetId();
		favoriteCount = status.getFavoriteCount();
		geoLocation = status.getGeoLocation();
		hachtagEntities = status.getHashtagEntities();
		id = status.getId();
		inReplyToScreenName = status.getInReplyToScreenName();
		inReplyToStatusId = status.getInReplyToStatusId();
		isoLanguageCode = status.getIsoLanguageCode();
		mediaEntities = status.getMediaEntities();
		place = status.getPlace();
		rateLimitStatus = status.getRateLimitStatus();
		retweetCount = status.getRetweetCount();
		retweetedStatus = new TwitterStatus(status.getRetweetedStatus());
		source = status.getSource();
		symbolEntities = status.getSymbolEntities();
		text = status.getText();
		URLEntities = status.getURLEntities();
		user =status.getUser();
		userMentionEntities = status.getUserMentionEntities();
		isFavorited = status.isFavorited();
		isPossiblySensitive = status.isPossiblySensitive();
		isRetweet = status.isRetweet();
		isRetweeted = status.isRetweeted();
		isRetweetedByMe = status.isRetweetedByMe();
		isTruncated = status.isTruncated();
	}

	public final int getFavoriteCount() {
		return favoriteCount;
	}

	public final void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public final String getText() {
		return text;
	}

	public final void setText(String text) {
		this.text = text;
	}

	public final boolean isFavorited() {
		return isFavorited;
	}

	public final void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}

	public final boolean isRetweet() {
		return isRetweet;
	}

	public final void setRetweet(boolean isRetweet) {
		this.isRetweet = isRetweet;
	}

	public final int getAccessLevel() {
		return accessLevel;
	}

	public final long[] getContributors() {
		return contributors;
	}

	public final long getCurrentUserRetweetId() {
		return currentUserRetweetId;
	}

	public final Date getCreatedAt() {
		return createdAt;
	}

	public final GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public final HashtagEntity[] getHachtagEntities() {
		return hachtagEntities;
	}

	public final long getId() {
		return id;
	}

	public final long getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public final String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public final String getIsoLanguageCode() {
		return isoLanguageCode;
	}

	public final MediaEntity[] getMediaEntities() {
		return mediaEntities;
	}

	public final Place getPlace() {
		return place;
	}

	public final RateLimitStatus getRateLimitStatus() {
		return rateLimitStatus;
	}

	public final int getRetweetCount() {
		return retweetCount;
	}

	public final TwitterStatus getRetweetedStatus() {
		return retweetedStatus;
	}

	public final String getSource() {
		return source;
	}

	public final SymbolEntity[] getSymbolEntities() {
		return symbolEntities;
	}

	public final URLEntity[] getURLEntities() {
		return URLEntities;
	}

	public final User getUser() {
		return user;
	}

	public final UserMentionEntity[] getUserMentionEntities() {
		return userMentionEntities;
	}

	public final boolean isPossiblySensitive() {
		return isPossiblySensitive;
	}

	public final boolean isRetweeted() {
		return isRetweeted;
	}

	public final boolean isRetweetedByMe() {
		return isRetweetedByMe;
	}

	public final boolean isTruncated() {
		return isTruncated;
	}
	*/

}
