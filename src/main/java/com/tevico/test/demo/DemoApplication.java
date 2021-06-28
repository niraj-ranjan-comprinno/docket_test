package com.tevico.test.demo;

import com.rabbitmq.client.Connection;
import com.tevico.test.demo.TestWorker.testWorker;
import com.tevico.test.demo.utility.AMQPConnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// SpringApplication.run(DemoApplication.class, args);

		Connection con = null;
		try {
			con = AMQPConnector.connect("amqps://kswsapea:lG68TiB2ymcpoEXmuJQhgLkeb8kS5Cjy@snake.rmq2.cloudamqp.com/kswsapea");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (con != null) {
			testWorker t = null;
			int threadCount = 1;

			t = new testWorker(con, threadCount);
			t.start();

		}
	}

}
