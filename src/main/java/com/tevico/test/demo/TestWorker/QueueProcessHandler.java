package com.tevico.test.demo.TestWorker;

public interface QueueProcessHandler {

  public abstract void process(String message) throws Exception;

}
