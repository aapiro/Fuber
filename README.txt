Framework:dropwizard +MongoDB - https://dropwizard.github.io/dropwizard/getting-started.html


Coding challenge:

You are the proprietor of füber, an on call taxi service.
You have a fleet of cabs at your disposal, and each cab has a location, determined by it's latitude and longitude.
A customer can call one of your taxis by providing their location, and you must assign the nearest taxi to the customer.
Some customers are particular that they only ride around in pink cars, for hipster reasons. You must support this ability.
When the cab is assigned to the customer, it can no longer pick up any other customers
If there are no taxis available, you reject the customers request.
The customer ends the ride at some location. The cab waits around outside the customer's house, and is available to be assigned to another customer.
Notes:
You can build this in any programming language of your choice
We expect good unit tests
Unfortunately, you skipped Geography, and believe the earth is flat. The distance between two points can be calculated by Pythagoras' theorem.
We don't expect a front end for this, but try to build an restful API.


Api: Json web service that responds with json message http status code

    GET     /fuber/taxi/get/{licensePlate}
    Find Taxi's given a licensePlate

    GET    /fuber/taxi/register/{licensePlate}/{latitude}/{longitude}/{isPink} 
    Allow taxi's to be registered as available 

    GET     /fuber/taxi/unregister/{licenseplate} 
    Remove taxi when taxi is no longer available

    GET     /fuber/taxi/reserve/{latitude}/{longitude}/{isPink} 
    Reserve taxi within configurable distance range .Try to reserve .Allows a few tries in case new taxis became available and desired taxi was booked

    GET     /fuber/taxi/free/{licenseplate}/{latitude}/{longtitude} 
    Set taxi as available from drop off location

How to run: execute com.fuber2.fuber2.FuberTaxiServer

TestClasses TaxiResourceTest which tests api response is same getting same info from db