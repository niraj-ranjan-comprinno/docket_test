package com.tevico.test.demo.utility;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public abstract class AMQPSubscriber extends Thread {

	public static ExecutorService threadPoolExecutor = new ThreadPoolExecutor(20, 50, 30, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public final static Logger LOGGER = LoggerFactory.getLogger(AMQPSubscriber.class.getName());

	private int threadCount = 10;
	String queueName = "";
	Connection connection = null;

	public AMQPSubscriber(Connection connection, String queueName) {
		this.queueName = queueName;
		this.connection = connection;
	}
	
	public AMQPSubscriber(Connection connection, String queueName, int threadCount) {
		this.threadCount = threadCount;
		this.queueName = queueName;
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		for (int i = 0; i < threadCount; i++) {
			AMQPSubscriber.threadPoolExecutor.submit(this.subscribe(connection));
		}
		// Thread.currentThread().join();
	}

	/**
	 * @param connection
	 * @return
	 */
	public WorkerRunnable subscribe(Connection connection) {
		return new WorkerRunnable(this, connection, this.queueName);
	}

	/**
	 * Each AMQP Subscriber should implement this method to process the message received.
	 * @param msg
	 */
	protected abstract void processMessage(String msg);

}


class WorkerRunnable implements Runnable {

	private AMQPSubscriber parent;
	private Connection con;
	private String queueName;

	public WorkerRunnable(AMQPSubscriber subs, Connection con, String queueName) {
		this.parent = subs;
		this.con = con;
		this.queueName = queueName;
	}

	@Override
	public void run() {
		String consumerTag = null;
		Channel channel;
		try {
			channel = con.createChannel();
			channel.basicQos(1);
			boolean autoAck = false;
			consumerTag = channel.basicConsume(queueName, autoAck, "", new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					
					//String routingKey = envelope.getRoutingKey();
					//String contentType = properties.getContentType();
					
					long deliveryTag = envelope.getDeliveryTag();

					String msg = new String(body, "UTF-8");
					AMQPSubscriber.LOGGER.info(msg); 
					parent.processMessage(msg);
					channel.basicAck(deliveryTag, false);
				}
			});

		} catch (IOException e) {
			AMQPSubscriber.LOGGER.error("Worker encountered IOException exception - " + this.getClass().getName());
			e.printStackTrace();
		} catch (Exception e) {
			AMQPSubscriber.LOGGER.error("Worker encountered unhandled exception - " + this.getClass().getName());
			e.printStackTrace();
		}
	}

}
