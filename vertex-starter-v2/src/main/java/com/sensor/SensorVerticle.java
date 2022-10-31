package com.sensor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

public class SensorVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(SensorVerticle.class);
  private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "8080"));

  private final String uuid = UUID.randomUUID().toString();
  private double temperature = 21.0;
  private final Random random = new Random();


  @Override
  // starts a http server, everything is asynchronous in vertx, thats why we have promise
  public void start(Promise<Void> startPromise) throws Exception {
    //object
    vertx.setPeriodic(2000, this::updateTemperature);

    startPromise.complete(); // at the end of start
  }

  //method for task
  private void updateTemperature(Long id) {
    temperature = temperature + (random.nextGaussian() / 2.0d);
    LOG.debug("Temperature updated: {} ", temperature);
  }
}
