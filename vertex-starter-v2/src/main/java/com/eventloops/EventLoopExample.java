package com.eventloops;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopExample extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(EventLoopExample.class);

  public static void main(String []args){
    var vertx = Vertx.vertx(
      new VertxOptions()
        .setMaxEventLoopExecuteTime(500)
        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
        .setBlockedThreadCheckInterval(1)
        .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
    );
    vertx.deployVerticle(EventLoopExample.class.getName(),
      new DeploymentOptions().setInstances(2));
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception{
    LOG.debug("Starting {}", getClass().getName());
    startPromise.complete();
    //do not do this
    Thread.sleep(5000);
  }


}
