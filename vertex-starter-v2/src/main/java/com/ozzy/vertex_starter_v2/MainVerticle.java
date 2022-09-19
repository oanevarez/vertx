package com.ozzy.vertex_starter_v2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle()); // new http server created each come from the override abstract start
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> req.response()
      .putHeader("content-type", "text/plain")
      .end("Hello from Vert.x! Hello Hello Hello HELLLOOOOOO Hello")).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
//    vertx.setPeriodic(500, id->{
//      LOG.debug(String.valueOf(new Random().nextDouble()));
//    });
  }

}
