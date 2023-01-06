package org.example.utils.logger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public interface ILogger {
    public static final Logger LOG = LogManager.getLogger(ILogger.class);


}
