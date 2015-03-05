package com.fuber2.fuber2;

import java.util.Comparator;

public class TaxiDistanceComparator implements Comparator<Taxi>{
	private double lat;
	private double lon;
	public TaxiDistanceComparator(){this.lat = 0; this.lon = 0;}
	public TaxiDistanceComparator(double lat, double lon){this.lat = lat; this.lon = lon;}
	public int compare(Taxi o1,Taxi o2)
	{
		double dist1 = ((Taxi)o1).distance(this.lat, this.lon);
		double dist2 = ((Taxi)o2).distance(this.lat,this.lon);
		if(dist1 < dist2)
			return -1;
		else if (dist1 == dist2)
			return 0;
		else
			return 1;
	}
}
