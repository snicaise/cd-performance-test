package web

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class BasicSimulation extends Simulation {

	val baseUrl: String = System.getProperty("base_url")

	val httpProtocol = http
		.baseURL(baseUrl)
		/*.inferHtmlResources(
			BlackList(""".*\.js""", """.*\.css""", """css?.*""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""),
			WhiteList())*/

	val headers = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

	val csvFeeder = csv("quotation.csv").random

	val scn = scenario("BasicSimulation")
		.feed(csvFeeder)
		.exec(http("landing")
			.get("/")
			.headers(headers)
		  .check(status.is(200)))
		.pause(5)
		.exec(http("quoteTrip")
			.get("/booking/quotation")
				.queryParam("origin", "${origin}")
				.queryParam("destination", "${destination}")
			.headers(headers)
		  .check(status.is(200)))

	setUp(
		scn.inject(
			rampUsersPerSec(1) to (10) during(1 minute),
			constantUsersPerSec(10) during(1 minute),
			rampUsersPerSec(10) to (20) during(1 minute),
			constantUsersPerSec(20) during(1 minute),
			rampUsersPerSec(20) to (30) during(1 minute),
			constantUsersPerSec(30) during(1 minute),
			rampUsersPerSec(30) to (1) during(1 minutes)
		)
	).protocols(httpProtocol)
}