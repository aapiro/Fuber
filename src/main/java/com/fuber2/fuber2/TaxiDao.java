package com.fuber2.fuber2;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author Dimple Joseph
 *
 */
public class TaxiDao {
	private DB db;
	private DBCollection col;

	public TaxiDao() {
	}

	public TaxiDao(DB db) {
		this.db = db;
		col = db.getCollection("Taxi");
	}

	public Object fetchActiveTaxiByLocation(int latitude, int longitude,
			boolean isPink) {
		return col.find(new BasicDBObject("latitude", new BasicDBObject("$gt",
				latitude - 10).append("$lte", latitude + 10))
				.append("longitude",
						new BasicDBObject("$gt", longitude - 10).append("$lte",
								longitude + 10)).append("isPink", isPink)
				.append("isActive", true));

	}

	public ArrayList<Taxi> fetchAvailableTaxis(String licensePlate) {
		ArrayList<Taxi> taxis = new ArrayList<Taxi>();
		BasicDBObject query = new BasicDBObject("licensePlate", licensePlate)
				.append("isActive", true);
		DBCursor cursor = col.find(query);

		ResourceHelper.notFoundIfNull(cursor);

		try {
			while (cursor.hasNext()) {
				BasicDBObject obj = (BasicDBObject) cursor.next();
				taxis.add(new Taxi(obj.getString("licensePlate"), obj
						.getDouble("latitude"), obj.getDouble("longitude"), obj
						.getBoolean("isPink"), obj.getBoolean("isOccupied"),
						obj.getBoolean("isActive")));
			}
		} finally {
			cursor.close();
		}
		return taxis;
	}

	public Object findUnOccupiedTaxi(String licensePlate) {
		DBObject obj = col.findOne(new BasicDBObject("licensePlate",
				licensePlate).append("isOccupied", false));

		return obj;

	}

	public Taxi freeTaxi(String licensePlate, double latitude, double longitude) {
		DBCollection col = db.getCollection("Taxi");
		DBCursor cursor = col.find(new BasicDBObject("licensePlate",
				licensePlate).append("isActive", true));
		Taxi taxi = null;
		ResourceHelper.notFoundIfNull(cursor);
		try {
			while (cursor.hasNext()) {
				BasicDBObject obj = (BasicDBObject) cursor.next();
				taxi = new Taxi(obj.getString("licensePlate"),
						obj.getDouble("latitude"), obj.getDouble("longitude"),
						obj.getBoolean("isPink"), obj.getBoolean("isOccupied"),
						obj.getBoolean("isActive"));
				taxi.free(latitude, longitude);
				save(taxi);
			}
		} finally {
			cursor.close();
		}
		return taxi;
	}

	public DBCollection getCollection() {
		return col;
	}

	public Taxi registerTaxi(String licensePlate, double latitude,
			double longitude, boolean isPink) {
		DBCollection col = db.getCollection("Taxi");
		DBCursor cursor = col.find(new BasicDBObject("licensePlate",
				licensePlate).append("isActive", true));
		Taxi taxi = new Taxi(licensePlate, latitude, longitude, isPink, false,
				true);
		ResourceHelper.notFoundIfNull(cursor);
		try {
			while (cursor.hasNext()) {

				BasicDBObject obj = (BasicDBObject) cursor.next();
				if (obj.getDouble("latitude") != latitude
						|| obj.getDouble("longitude") != longitude) {
					taxi = new Taxi(obj.getString("licensePlate"), latitude,
							longitude, obj.getBoolean("isPink"), false, true);
				}

			}

			save(taxi);
		} finally {
			cursor.close();
		}
		return taxi;
	}

	public ArrayList<Taxi> reserveTaxi(double latitude, double longitude,
			boolean isPink) {
		ArrayList<Taxi> taxis = new ArrayList<Taxi>();

		int distanceRange = 10;

		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("latitude", new BasicDBObject("$gt", latitude
				- distanceRange).append("$lt", latitude + distanceRange)));
		obj.add(new BasicDBObject("longitude", new BasicDBObject("$gt",
				longitude - distanceRange).append("$lt", longitude
				+ distanceRange)));
		obj.add(new BasicDBObject("isPink", isPink));
		obj.add(new BasicDBObject("isActive", true));
		obj.add(new BasicDBObject("isOccupied", false));
		andQuery.put("$and", obj);
		System.out.println(andQuery.toString());

		DBCursor cursor = col.find(andQuery);

		if (cursor.hasNext()) {
			BasicDBObject obj1 = (BasicDBObject) cursor.next();
			System.out.println(obj1);
			taxis.add(new Taxi(obj1.getString("licensePlate"), obj1
					.getDouble("latitude"), obj1.getDouble("longitude"), obj1
					.getBoolean("isPink"), obj1.getBoolean("isOccupied"), obj1
					.getBoolean("isActive")));
		}

		return taxis;

	}

	public Taxi save(Taxi taxi) {
		return save(taxi, col);
	}

	public Taxi save(Taxi taxi, DBCollection col) {

		BasicDBObject oldDoc = new BasicDBObject("licensePlate",
				taxi.getLicensePlate()).append("isActive", true);
		BasicDBObject doc = new BasicDBObject("licensePlate",
				taxi.getLicensePlate()).append("latitude", taxi.getLatitude())
				.append("longitude", taxi.getLongitude())
				.append("isPink", taxi.isPink())
				.append("isOccupied", taxi.isOccupied())
				.append("isActive", taxi.isActive());
		System.out.println(doc.toString());
		if (col.findOne(oldDoc) != null)
			col.update(oldDoc, doc);
		else
			col.insert(doc);
		return taxi;
	}

	public Taxi unregisterTaxi(String licensePlate) {
		DBCollection col = db.getCollection("Taxi");
		DBCursor cursor = col.find(new BasicDBObject("licensePlate",
				licensePlate));
		Taxi taxi = null;
		ResourceHelper.notFoundIfNull(cursor);
		try {
			while (cursor.hasNext()) {
				BasicDBObject obj = (BasicDBObject) cursor.next();
				taxi = new Taxi(obj.getString("licensePlate"),
						obj.getDouble("latitude"), obj.getDouble("longitude"),
						obj.getBoolean("isPink"), obj.getBoolean("isOccupied"),
						obj.getBoolean("isActive"));
				taxi.unregister();
				save(taxi);
			}
		} finally {
			cursor.close();
		}
		return taxi;
	}

	public Taxi update(Taxi taxi) {

		BasicDBObject oldDoc = new BasicDBObject("licensePlate",
				taxi.getLicensePlate()).append("isActive", true);
		BasicDBObject doc = new BasicDBObject("licensePlate",
				taxi.getLicensePlate()).append("latitude", taxi.getLatitude())
				.append("longitude", taxi.getLongitude())
				.append("isPink", taxi.isPink())
				.append("isOccupied", taxi.isOccupied())
				.append("isActive", taxi.isActive());
		System.out.println(doc.toString());
		col.update(oldDoc, doc);

		return taxi;
	}
}
