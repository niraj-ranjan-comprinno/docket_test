package com.tevico.test.demo.utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class AMQPConnector {
	
	/**
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws URISyntaxException
	 */
	public static Connection connect(String url) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		ConnectionFactory factory = new ConnectionFactory();

		factory.setUri(url);
		Connection connection = null;
		try {
			connection = factory.newConnection();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		return connection;
	}


}

