package com.think42lab.arangam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Segment extends ListItem implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("ticketsAvailable")
    private String ticketsAvailable;
    @SerializedName("ticketCost")
    private String ticketsCost;
    @SerializedName("SegmentTypeId")
    private String segId;
    @SerializedName("segment_date")
    private String segmentDate;
    @SerializedName("ArtistId")
    private String artistID;
    @SerializedName("VenueId")
    private String venueID;
    @SerializedName("segment_time")
    private String segmentTime;
    @SerializedName("accompanists")
    private String Accompanists;
    @SerializedName("Artist")
    private Artists artists;
    @SerializedName("Venue")
    private Venues venues;
    @SerializedName("SegmentType")
    private SegmentType segmentType;

    @SerializedName("period")
    private String period;



    private String venueName;
    private boolean isFavorite;

    private String latidue;

    private String longitude;








    public Segment(String id, String ticketsAvailable, String ticketsCost, String segId, String segmentDate, String artistID, String venueID, String venueName, String segmentTime, String accompanists, boolean isFavorite, String latidue, String longitude, Artists artists, Venues venues, SegmentType segmentType) {
        this.id = id;
        this.ticketsAvailable = ticketsAvailable;
        this.ticketsCost = ticketsCost;
        this.segId = segId;
        this.segmentDate = segmentDate;
        this.artistID = artistID;
        this.venueID = venueID;
        this.venueName = venueName;
        this.segmentTime = segmentTime;
        Accompanists = accompanists;
        this.isFavorite = isFavorite;
        this.latidue = latidue;
        this.longitude = longitude;
        this.artists = artists;
        this.venues = venues;
        this.segmentType = segmentType;
    }

    public Segment() {
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Artists getArtists() {
        return artists;
    }

    public void setArtists(Artists artists) {
        this.artists = artists;
    }

    public Venues getVenues() {
        return venues;
    }

    public void setVenues(Venues venues) {
        this.venues = venues;
    }

    public SegmentType getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(SegmentType segmentType) {
        this.segmentType = segmentType;
    }

    public String getLatidue() {
        return latidue == null ? " " : latidue;
    }

    public void setLatidue(String latidue) {
        this.latidue = latidue;
    }

    public String getLongitude() {
        return longitude == null ? " " : longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(String ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public String getTicketsCost() {
        return ticketsCost;
    }

    public void setTicketsCost(String ticketsCost) {
        this.ticketsCost = ticketsCost;
    }

    public String getAccompanists() {
        return Accompanists;
    }

    public void setAccompanists(String accompanists) {
        Accompanists = accompanists;
    }


    public String getSegmentTime() {
        return segmentTime;
    }

    public void setSegmentTime(String segmentTime) {
        this.segmentTime = segmentTime;
    }

    public String getSegmentDate() {
        return segmentDate;
    }

    public void setSegmentDate(String segmentDate) {
        this.segmentDate = segmentDate;
    }

    public String getArtistID() {
        return artistID;
    }

    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }

    public String getVenueID() {
        return venueID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSegId() {
        return segId;
    }

    public void setSegId(String segId) {
        this.segId = segId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", ticketsAvailable='" + ticketsAvailable + '\'' +
                ", ticketsCost='" + ticketsCost + '\'' +
                ", segId='" + segId + '\'' +
                ", segmentDate='" + segmentDate + '\'' +
                ", artistID='" + artistID + '\'' +
                ", venueID='" + venueID + '\'' +
                ", venueName='" + venueName + '\'' +
                ", segmentTime='" + segmentTime + '\'' +
                ", Accompanists='" + Accompanists + '\'' +
                ", isFavorite=" + isFavorite +
                ", latidue='" + latidue + '\'' +
                ", longitude='" + longitude + '\'' +
                ", period='" + period + '\'' +
                ", artists:" + artists +
                ", venues:" + venues +
                ", segmentType:" + segmentType +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "{" +
//                "id='" + id + '\'' +
//                ", ticketsAvailable='" + ticketsAvailable + '\'' +
//                ", ticketCost='" + ticketsCost + '\'' +
//                ", SegmentTypeId='" + segId + '\'' +
//                ", segment_date='" + segmentDate + '\'' +
//                ", ArtistId='" + artistID + '\'' +
//                ", VenueId='" + venueID + '\'' +
//                ", venueName='" + venueName + '\'' +
//                ", segment_time='" + segmentTime + '\'' +
//                ", accompanists='" + Accompanists + '\'' +
//                ", isFavorite=" + isFavorite +
//                ", latidue='" + latidue + '\'' +
//                ", longitude='" + longitude + '\'' +
//                ", Artist=" + artists +
//                ", Venue=" + venues +
//                ", SegmentType=" + segmentType +
//                '}';
//    }

//        @Override
//    public String toString() {
//        return "{" +
//                "id='" + id + '\'' +
//                ", segId='" + segId + '\'' +
//                ", segmentDate='" + segmentDate + '\'' +
//                ", artistID='" + artistID + '\'' +
//                ", venueID='" + venueID + '\'' +
//                ", isFavorite=" + isFavorite +
//                ", venueName='" + venueName + '\'' +
//                ", segmentTime='" + segmentTime + '\'' +
//                ", Accompanists='" + Accompanists + '\'' +
//                ", ticketsAvailable='" + ticketsAvailable + '\'' +
//                ", ticketCost='" + ticketsCost + '\'' +
//                ", latitude='" + latidue + '\'' +
//                ", logitude='" + longitude + '\'' +
//                '}';
//    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
