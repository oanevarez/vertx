package com.ozzy.vertex_starter_v2;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.FutureTask;

import static io.vertx.core.Promise.promise;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {

  private static final Logger LOG = LoggerFactory.getLogger(FuturePromiseExample.class);

  @Test
  void promise_complete(Vertx vertx, VertxTestContext context) {
    //eventloops/bus/timers

    final Promise<String> promise = promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Success");
      context.completeNow();
    });
    LOG.debug("End");
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext context) {
    //eventloops/bus/timers

    final Promise<String> promise = promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Failed");
      context.completeNow();
    });
    LOG.debug("End");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext context) {
    //eventloops/bus/timers

    final Promise<String> promise = promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Timer done.");
    });

    final Future<String> future = promise.future();
    future
      .onSuccess(result -> {
        LOG.debug("Result: {}", result);
        context.completeNow();
      })
      .onFailure(context::failNow);

  }

  @Test
  void future_failed(Vertx vertx, VertxTestContext context) {
    //eventloops/bus/timers

    final Promise<String> promise = promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Timer done.");
    });

    final Future<String> future = promise.future();
    future
      .onSuccess(context::failNow)
      .onFailure(error -> {
        LOG.debug("Result: {}", error);
        context.completeNow();
      });

  }


  @Test
  void future_map(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Timer done.");
    });

    final Future<String> future = promise.future();
    future
      .map(asString -> {
        LOG.debug("Map String to JsonObject");
        return new JsonObject().put("key", asString);
      })
      .map(jsonObject -> new JsonArray().add(jsonObject))
      .onSuccess(result -> {
        LOG.debug("Result: {} of type {}", result, result.getClass().getSimpleName());
        context.completeNow();
      })
      .onFailure(context::failNow);
  }


  @Test
  void future_coordination(Vertx vertx, VertxTestContext context){
    vertx.createHttpServer()
      .requestHandler(request -> LOG.debug("{}", request))
      .listen(10_000)
      .compose(server -> {
        LOG.info("Another task");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        LOG.info("Even More task");
        return Future.succeededFuture(server);
      })

      .onFailure(context::failNow)
      .onSuccess(server -> {
        LOG.debug("Server Started on port {}", server.actualPort());
        context.completeNow();
      });
  }


  @Test
  void future_compositon(Vertx vertx, VertxTestContext context){
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var three = Promise.<Void>promise();

    var futureOne = one.future();
    var futureTwo = two.future();
    var futureThree = three.future();

    CompositeFuture.all(futureOne, futureTwo, futureThree)
      .onFailure(context::failNow)
      .onSuccess(result->{
        LOG.debug("Success");
        context.completeNow();
      });

    //complete futures
    vertx.setTimer(500, id->{
      one.complete();
      two.complete();
      three.complete();
    });

  }


}
