package com.verticle.scaling;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleAB extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticleAB.class);

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    LOG.debug("Starting " + getClass().getName());
    startPromise.complete();
  }

  @Override
  public void stop(final Promise<Void> stopPromise) throws Exception {
    LOG.debug("Stopped " + getClass().getName());
    stopPromise.complete();
  }


}
