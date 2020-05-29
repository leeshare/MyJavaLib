package org.lixl.hadoop.lixlsource.util;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;

class LogAdapter {
    private Log LOG;
    private Logger LOGGER;

    private LogAdapter(Log LOG){
        this.LOG = LOG;
    }
    private LogAdapter(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }

    @Deprecated
    public static LogAdapter create(Log LOG) {
        return new LogAdapter(LOG);
    }

    public static LogAdapter create(Logger LOGGER) {
        return new LogAdapter(LOGGER);
    }

    public void info(String msg) {
        if(LOG != null) {
            LOG.info(msg);
        } else if(LOGGER != null) {
            LOGGER.info(msg);
        }
    }

    public void warn(String msg, Throwable t) {
        //...
    }
}
