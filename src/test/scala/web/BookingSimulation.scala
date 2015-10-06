package web

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class BookingSimulation extends Simulation {

	val baseUrl: String = System.getProperty("base_url")

	val httpProtocol = http
		.baseURL(baseUrl)

	val headers = Map("Accept" -> "application/json")

	val csvFeeder = csv("quotation.csv").random

	val scn = scenario("booking")
		.feed(csvFeeder)
		.exec(http("quoteTrip")
			.get("/booking/quotation")
				.queryParam("origin", "${origin}")
				.queryParam("destination", "${destination}")
			.headers(headers)
		  .check(status.is(200)))

	setUp(
		scn
			.inject(constantUsersPerSec(5) during(1 minutes))
			.throttle(
				reachRps(5) in (30 seconds),
				holdFor(30 seconds)
			)
		)
		.protocols(httpProtocol)
		.assertions(
			global.successfulRequests.percent.greaterThan(95)
		)

	/*
	setUp(
		scn
			.inject(constantUsersPerSec(200) during(10 minutes))
			.throttle(
				reachRps(5) in (1 minute),
				holdFor(1 minute),
				jumpToRps(10),
				holdFor(1 minute),
				jumpToRps(20),
				holdFor(1 minute),
				jumpToRps(40),
				holdFor(1 minute)
			)
	).protocols(httpProtocol)
	*/
}