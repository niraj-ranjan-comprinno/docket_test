package com.tevico.test.demo.TestWorker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestQueueProcessHandler implements QueueProcessHandler {

  private final static Logger LOGGER = LoggerFactory.getLogger(TestQueueProcessHandler.class.getName());

  @Override
  public void process(String message) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		LOGGER.info("TestQueueProcessHandler: message which is read from inventory queue: " + message);
    LOGGER.info("Creating file with name:: " + "test_");

    try {
      Random rnd = new Random();
      int number = rnd.nextInt(999999);
      String fileName = "test_" + number;
      String path = "/home/ec2-user/file/" +fileName+".txt" ;
      // Path path = Paths.get("C:\\demo\\javaprogram.txt");
      File file = new File(path);

      file.createNewFile();
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);

      // Write in file
      bw.write(message);

      // Close connection
      bw.close();
      // FileOutputStream fos=new FileOutputStream(fileName, true); 
      // byte[] b= message.getBytes(); 
      // fos.write(b);
      // fos.close(); 
      LOGGER.info("Files successfully saved");
    } catch (Exception e) {
      LOGGER.error("Exception occured while creating file" + e.getMessage());
    }


    
  }

}
