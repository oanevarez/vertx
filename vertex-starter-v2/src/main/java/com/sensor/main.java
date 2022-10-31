package com.sensor;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class main {

  private static final Logger LOG = LoggerFactory.getLogger(main.class);

  public static void main(String[] args){
    Vertx myVertx = Vertx.vertx();
    //myVertx.deployVerticle("SensorVerticle", new DeploymentOptions().setInstances(4));
    // main has myVertx object that deploys SensorVerticle, myVertx can deploy multiple verticles
    myVertx.deployVerticle(new SensorVerticle());


  }
}
