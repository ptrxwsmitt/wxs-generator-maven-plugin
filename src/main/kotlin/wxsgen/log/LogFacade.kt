package wxsgen.log

/**
 * Simple Log Facade for the WXS Generator. SLF4J is a little heavy for this purpose right now.
 *
 * @author Patrick Waldschmitt
 */
interface LogFacade {
    fun debug(msg: String, e: Throwable? = null)
    fun error(msg: String, e: Throwable? = null)
    fun warn(msg: String, e: Throwable? = null)
    fun info(msg: String)
}
