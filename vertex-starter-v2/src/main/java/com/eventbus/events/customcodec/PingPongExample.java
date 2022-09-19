package com.eventbus.events.customcodec;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {


  private static final Logger LOG = LoggerFactory.getLogger(PingPongExample.class);

  public static void main(String [] args){
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), logOnError());
    vertx.deployVerticle(new PongVerticle(), logOnError());

  }

  private static Handler<AsyncResult<String>> logOnError() {
    return ar -> {
      if (ar.failed()) {
        LOG.error("ERR", ar.cause());
      }
    };
  }

  static class PingVerticle extends AbstractVerticle{

    public static final String ADDRESS = PingVerticle.class.getName();

    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      startPromise.complete();
      var eventBus = vertx.eventBus();
      final Ping message = new Ping("Hello", true);

      LOG.debug("Sending: {} ", message);
      eventBus.<Pong>request(ADDRESS, message, // address and message
        reply-> {
        if(reply.failed()){
          LOG.error("Failed: ", reply.cause());
        }
        LOG.debug("Response: {} ", reply.result().body());
        });
    }

  }

  static class PongVerticle extends AbstractVerticle{

    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      startPromise.complete();
      vertx.eventBus().<Ping>consumer(PingVerticle.ADDRESS, message ->{
        LOG.debug("Receiving: {} ", message.body());
        message.reply(new Pong(0));
      }).exceptionHandler(error ->{
        LOG.error("ERROR: ", error);
      });
    }

  }


}

