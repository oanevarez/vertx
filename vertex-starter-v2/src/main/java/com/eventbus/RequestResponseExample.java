package com.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {


  private static final Logger LOG = LoggerFactory.getLogger(RequestResponseExample.class);

  public static void main(String [] args){
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());

  }

  static class RequestVerticle extends AbstractVerticle{

    public static final String ADDRESS = "my.request.address";

    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      startPromise.complete();
      var eventBus = vertx.eventBus();
      final String message = "Hello WORLD!";

      LOG.debug("Sending: {} ", message);
      eventBus.<String>request(ADDRESS, message, // address and message
        reply-> {
        LOG.debug("Response: {} ", reply.result().body());
        });
    }

  }

  static class ResponseVerticle extends AbstractVerticle{

    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      startPromise.complete();
      vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS, message ->{
        LOG.debug("Receiving: {} ", message.body());
        message.reply("Received the message!");
      });
    }

  }


}

