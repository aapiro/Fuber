package com.fuber2.fuber2;

import net.vz.mongodb.jackson.JacksonDBCollection;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;


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
