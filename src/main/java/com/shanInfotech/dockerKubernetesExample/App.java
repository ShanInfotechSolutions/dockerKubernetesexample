package com.shanInfotech.dockerKubernetesExample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        String instanceId = System.getenv("INSTANCE_ID");
        String greeting = System.getenv("APP_GREETING");

        if (instanceId == null) {
            instanceId = "LOCAL_INSTANCE";
        }

        if (greeting == null) {
            greeting = "Hello from replica!";
        }

        logger.info("Starting ReplicaSafeApp instance: {}", instanceId);
        logger.info("Greeting: {}", greeting);

        while (true) {
            try {
                Thread.sleep(10000);
                logger.info("Running... Instance ID: {}", instanceId);
            } catch (InterruptedException e) {
                logger.error("Interrupted: ", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
