package com.fuber2.fuber2;

import net.vz.mongodb.jackson.JacksonDBCollection;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;

/**
 * Fuber
 * FuberTaxiService.java
 * Purpose: Webservice to manage taxis
 * You are the proprietor of füber, an on call taxi service.
 *You have a fleet of cabs at your disposal, and each cab has a location, 
 *determined by it's latitude and longitude.
 * A customer can call one of your taxis by providing their location, 
 * and you must assign the nearest taxi to the customer.
 *Some customers are particular that they only ride around in pink cars, for hipster reasons. 
 *You must support this ability.
 *When the cab is assigned to the customer, it can no longer pick up any other customers
 *If there are no taxis available, you reject the customers request.
 *The customer ends the ride at some location. The cab waits around outside the customer's house, 
 *and is available to be assigned to another customer.
 *Notes:
 *You can build this in any programming language of your choice
 *We expect good unit tests
 *Unfortunately, you skipped Geography, and believe the earth is flat. 
 *The distance between two points can be calculated by Pythagoras' theorem.
 *We don't expect a front end for this, but try to build an restful API.
 *
 * @author Dimple Joseph
 * @version 1.0 3/5/15
 */
public class FuberTaxiServer extends Service<TaxiConfiguration> {

	@Override
    public void initialize(Bootstrap<TaxiConfiguration> bootstrap) {
        bootstrap.setName("fuber");
    }

	@Override
	public void run(TaxiConfiguration configuration, Environment environment) throws Exception {
		Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport);
        DB db = mongo.getDB("mydb");

        //JacksonDBCollection<Taxi, String> taxis =
                //JacksonDBCollection.wrap(db.getCollection("Taxi"), Taxi.class, String.class);
        Managed mongoManaged = new MongoManaged(mongo);
        environment.manage(mongoManaged);
        environment.addResource(new TaxiResource(new TaxiDao(db)));
		environment.addResource(new RegisterTaxiResource(db));
		environment.addResource(new UnRegisterTaxiResource(db));
		environment.addResource(new ReserveTaxiResource(new TaxiDao(db)));
		environment.addResource(new FreeTaxiResource(db));
		
	}
	
	public static void main(String[] args) throws Exception {
        new FuberTaxiServer().run(new String[]{"server", "src/main/resources/config.yaml"});
    }


}
