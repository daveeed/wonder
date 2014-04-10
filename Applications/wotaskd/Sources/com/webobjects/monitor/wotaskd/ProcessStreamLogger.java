package com.webobjects.monitor.wotaskd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.webobjects.monitor._private.MInstance;
import com.webobjects.monitor._private.MObject;

public class ProcessStreamLogger extends Thread {

    private Logger logger;
    private MInstance instance;
    BufferedReader reader;
    
    
    public ProcessStreamLogger(MInstance instance, Logger logger, Process process) {
        super();
        setName(instance.displayName());
        this.logger = logger;
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        this.instance = instance;
    }
    
    
    public void run() {
        try {
            while (instance.state == MObject.STARTING) {
                while (reader.ready()) {
                    logger.debug(reader.readLine());
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Don't care
                }
            }
            reader.close();
            logger.debug("Instance output now going to " + MObject.validatedOutputPath(instance.outputPath()));
        } catch (Exception e) {
            logger.error("The log reader died unexpectedly.", e);
        }
    }
}