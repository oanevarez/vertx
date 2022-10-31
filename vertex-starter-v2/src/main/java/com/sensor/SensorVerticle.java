package com.sensor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.core.appender.routing.Route;
import org.apache.logging.log4j.core.appender.routing.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

public class SensorVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(SensorVerticle.class);
  private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "8888"));

  private final String uuid = UUID.randomUUID().toString();
  private double temperature = 21.0;
  private final Random random = new Random();


  @Override
  // starts a http server, everything is asynchronous in vertx, thats why we have promise
  public void start(Promise<Void> startPromise) throws Exception {
    //object
    vertx.setPeriodic(2000, this::updateTemperature);

    //Create HTTP server
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(request -> {
      HttpServerResponse response = request.response(); // every request has this handler
      response.putHeader("Content-type", "text-plain");
      response.end(getData().toString());
    });

    server.listen(httpPort)
      .onSuccess(ok->{
      LOG.debug("http server running: http://localhost:{}", httpPort);
      startPromise.complete();
    })
      .onFailure(startPromise::fail);

  }

  //method for task
  private void updateTemperature(Long id) {
    temperature = temperature + (random.nextGaussian() / 2.0d);
    LOG.debug("Temperature updated: {} ", temperature);

    //event bus publishing method
    vertx.eventBus().publish("temp updates", getData());
  }

  private JsonObject getData(){
    //LOG.debug("Refreshed on HTTP Server... Processing request, will display JSON");

    JsonObject payload = new JsonObject()
      .put("uuid", uuid)
      .put("temperature", temperature)
      .put("timestamp", System.currentTimeMillis());

    payload.encodePrettily();
    return  payload;
  }
}
