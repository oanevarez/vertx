package com.verticle.scaling;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    LOG.debug("Starting {}", getClass().getName());

    //deploy child verticles
    vertx.deployVerticle(new VerticleA());

    vertx.deployVerticle(new VerticleB());

    vertx.deployVerticle(VerticleN.class.getName(),
      new DeploymentOptions().setInstances(4) //multiple instances only pass name of class, 2nd parameter = deploymentOptions
        .setConfig(new JsonObject()
          .put("id", UUID.randomUUID().toString())
          .put("name", VerticleN.class.getSimpleName())
        )
    );
    startPromise.complete();
  }


}
