package com.tevico.test.demo.TestWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.rabbitmq.client.Connection;
import com.tevico.test.demo.utility.AMQPSubscriber;

public class testWorker extends AMQPSubscriber {
  private final static Logger LOGGER = LoggerFactory.getLogger(testWorker.class.getName());


  private static String queueName = "test-queue";

  public testWorker(Connection con, int threadCount) {
    super(con, queueName,threadCount);
  }

  @Override
	protected void processMessage(String msg) {
		LOGGER.info((this.currentThread().getId() + " > " + msg));
		LOGGER.info("In process message of test queue worker");

		try {
			new TestQueueProcessHandler().process(msg);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while processing message in test queue process handler : "+e.getMessage());

		}
	}
  
}
