package com.worker.threads;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle{

  private static final Logger LOG = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String [] args){
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception{
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-verticle")
    );
    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> {
      LOG.debug("Executing Blocking Code");
      try{
        Thread.sleep(5000);
        event.complete(); // worker thread 0
        //event.fail("FORCED FAIL");
      }catch (InterruptedException e){
        LOG.error("Failed: " , e);
        event.fail(e);
      }
    }, result ->{
      if (result.succeeded()){
        LOG.debug("Blocking Call Done.");
      }else {
        LOG.debug("Blocking Call Failed due to: ", result.cause());
      }
    });
  }

}
