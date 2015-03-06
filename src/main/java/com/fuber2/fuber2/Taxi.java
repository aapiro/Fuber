package com.fuber2.fuber2;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Taxi{

	@Id @ObjectId
    private String id;//db primary key
	 @NotEmpty
    private String licensePlate; //Licence Plate
    @Max(-180)
    @Min(180)
    private double latitude; // -180  to 180
    @Max(-90)
    @Min(90)
    private double longitude; // -90 to 90
    private boolean isPink; // is it pink colored taxi
    private boolean isOccupied; // is it currently occupied
    private boolean isActive; // is the driver working now
    
    public Taxi()
    {
    	this.isActive = true;
    }
	public Taxi(String licensePlate,double latitude ,double longitude,boolean isPink ,boolean occupied, boolean isActive)
	{
		this.licensePlate = licensePlate;
		this.latitude = isValidLatitude(latitude);
		this.longitude = isValidLongitude(longitude);
		this.isPink = isPink;
		this.isOccupied = occupied;
		this.isActive = isActive;
		
	}
	
	public static double isValidLatitude(double latitude)
	{ 
		if(latitude < -180)
			return -180;
		else if(latitude>180)
			return 180;
		return latitude;
	}
	
	public static double isValidLongitude(double longitude)
	{ 
		if(longitude < -90)
			return -90;
		else if(longitude>90)
			return 90;
		return longitude;
	}
	
	public double distance(double latitude,double longtitude2 )
	{
		return Math.sqrt(Math.pow(latitude - latitude,2) + Math.pow(longitude - longitude,2));
	}
	
	public void free(double latitude ,double longtitude)
	{
		this.latitude = latitude;
		this.longitude = longtitude;
		this.isOccupied = false;
	}
	
	public void reserve()
	{
		this.isOccupied = true;
	}

	public void unregister() {
		this.isActive = false;
		
	}
	
    @Override
    public String toString(){
        return "{\"licensePlate\":" + licensePlate + ",\"Latitude\":" + 
        		latitude + ",\"longitude\":" + longitude +",\"isPink\":" + isPink+
        		",\"isOccupied\":" + isOccupied +",\"isActive\":" + isActive +"}";

    }


    @ Override
    public boolean equals(Object o){
        if(!(o instanceof Taxi))       
            return false;
        Taxi that=(Taxi)o;
        return that.latitude==this.latitude && that.longitude==this.longitude;
    }

    @ObjectId
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}
    public void setId() {
		// TODO Auto-generated method stub
		this.id=id;
	}
    

    @ObjectId
	public String getLicensePlate() {
		return licensePlate;
	}
    public void setLicensePlate() {
		this.licensePlate=licensePlate;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	public boolean isPink() {
		return isPink;
	}
	public void setIsPink(boolean isPink){
		this.isPink = isPink;
	}
	public boolean isOccupied() {
		return isOccupied;
	}
	public void setIsOccupied(boolean isOccupied){
		this.isOccupied = isOccupied;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}
	
}