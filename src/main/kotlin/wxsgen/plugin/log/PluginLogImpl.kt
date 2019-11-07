package wxsgen.plugin.log

import org.apache.maven.plugin.logging.Log
import wxsgen.log.LogFacade

/**
 * Maven Plugin Logging Implementation of the Log Facade for the WXS Generator.
 *
 * @author Patrick Waldschmitt
 */
class PluginLogImpl(private val log: Log) : LogFacade {
    override fun error(msg: String, e: Throwable?) {
        if (e == null) {
            log.error(msg)
        } else {
            log.error(msg, e)
        }
    }

    override fun debug(msg: String, e: Throwable?) {
        if (e == null) {
            log.debug(msg)
        } else {
            log.debug(msg, e)
        }
    }

    override fun warn(msg: String, e: Throwable?) {
        if (e == null) {
            log.warn(msg)
        } else {
            log.warn(msg, e)
        }
    }

    override fun info(msg: String) {
        log.info(msg)
    }
}
