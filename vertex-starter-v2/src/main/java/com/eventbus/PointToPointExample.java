package com.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample {

  private static final Logger LOG = LoggerFactory.getLogger(PointToPointExample.class);

  public static void main(String [] args){
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());

  }

  static class Sender extends AbstractVerticle{
    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      startPromise.complete();
      LOG.debug("Sent");
      vertx.setPeriodic(1000, id->{
        //send message every second
        vertx.eventBus().send(Sender.class.getName(), "Sending a message...");
      });

    }
  }

  static class Receiver extends AbstractVerticle{
    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      startPromise.complete();
      LOG.debug("Opening");
      vertx.eventBus().consumer(Sender.class.getName(), message ->{
        LOG.debug("Received: {}", message.body());
      });
    }
  }

}
