Framework:dropwizard +MongoDB - 
https://dropwizard.github.io/dropwizard/getting-started.html
https://dropwizard.github.io/dropwizard/manual/example.html
http://code.tutsplus.com/tutorials/getting-started-with-mongodb-part-1--net-22879

4 hour Coding challenge:

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

Assumptions

Latitude measures how far north or south of the equator a place is located. The equator is situated at 0°, the North Pole at 90° north (or 90°, because a positive latitude implies north), and the South Pole at 90° south (or –90°). Latitude measurements range from 0° to (+/–)90°.
Longitude measures how far east or west of the prime meridian a place is located. The prime meridian runs through Greenwich, England. Longitude measurements range from 0° to (+/–)180°.

for simplicity Latitude and longitude coordinates are represented as decimal numbers

Api: Json web service that responds with json message http status code

    GET     /fuber/taxi/get/{licensePlate}
    Find Taxi's given a licensePlate

    GET    /fuber/taxi/register/{licensePlate}/{latitude}/{longitude}/{isPink} 
    Allow taxi's to be registered as available 

    GET     /fuber/taxi/unregister/{licenseplate} 
    Remove taxi when taxi is no longer available

    GET     /fuber/taxi/reserve/{latitude}/{longitude}/{isPink} 
    Reserve taxi within configurable distance range .Try to reserve .Allows a few tries in case new taxis became available and desired taxi was booked

    GET     /fuber/taxi/free/{licenseplate}/{latitude}/{longitude} 
    Set taxi as available from drop off location

Demo video -https://www.youtube.com/watch?v=FGyuWaWZIu0

How to run: execute com.fuber2.fuber2.FuberTaxiServer
https://dropwizard.github.io/dropwizard/manual/example.html

Tests
TaxiResourceTest which tests api response is same getting same info from db
ReserveTaxiResourceTest
UnregisterTaxiResourceTest
FreeTaxiResourceTest
RegisterTaxiResourceTest