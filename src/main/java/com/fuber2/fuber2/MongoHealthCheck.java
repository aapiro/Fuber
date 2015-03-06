package com.fuber2.fuber2;

import com.mongodb.Mongo;
import com.yammer.metrics.core.HealthCheck;

/**
 * @author Dimple Joseph
 *
 */
public class MongoHealthCheck extends HealthCheck {

	private Mongo mongo;

	public MongoHealthCheck(Mongo mongo) {
		super("MongoHealthCheck");
		this.mongo = mongo;
	}

	@Override
	protected Result check() throws Exception {
		mongo.getDatabaseNames();
		return Result.healthy();
	}

}