package com.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExampleJSON {


  private static final Logger LOG = LoggerFactory.getLogger(RequestResponseExampleJSON.class);

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
      final var message = new JsonObject()
        .put("message", "HELLO WORLD!")
        .put("version", 1);

      LOG.debug("Sending: {} ", message);
      eventBus.<JsonArray>request(ADDRESS, message, // address and message
        reply-> {
        LOG.debug("Response: {} ", reply.result().body());
        });
    }

  }

  static class ResponseVerticle extends AbstractVerticle{

    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      startPromise.complete();
      vertx.eventBus().<JsonObject>consumer(RequestVerticle.ADDRESS, message ->{
        LOG.debug("Receiving: {} ", message.body());
        message.reply(new JsonArray().add("ONE").add("TWO").add("THREE"));
      });
    }

  }


}

