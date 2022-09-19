package com.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PublishSubscribeExample {



  public static void main(String[] args){
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(new Subscriber_One());
    vertx.deployVerticle(Subscriber_Two.class.getName(), new DeploymentOptions().setInstances(2));

  }

  static class Publish extends AbstractVerticle{
    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      startPromise.complete();
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id->{
        vertx.eventBus().publish(Publish.class.getName(), "A message for everyone!");
      });

    }
  }

  static class Subscriber_One extends AbstractVerticle{
    private static Logger LOG = LoggerFactory.getLogger(Subscriber_One.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      vertx.eventBus().consumer(Publish.class.getName(), message->{
        LOG.debug("Received: {} ", message.body().toString());
      });
    }
  }

  public static class Subscriber_Two extends AbstractVerticle{
    private static Logger LOG = LoggerFactory.getLogger(Subscriber_Two.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception{
      vertx.eventBus().consumer(Publish.class.getName(), message->{
        LOG.debug("Received: {} ", message.body().toString());
      });
    }
  }


}

