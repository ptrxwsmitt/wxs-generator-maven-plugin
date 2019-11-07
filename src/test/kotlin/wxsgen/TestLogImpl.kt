package wxsgen

import wxsgen.log.LogFacade

class TestLogImpl : LogFacade {
    override fun debug(msg: String, e: Throwable?) {
        logWithException(msg, e)
    }

    override fun error(msg: String, e: Throwable?) {
        logWithException(msg, e)
    }

    override fun warn(msg: String, e: Throwable?) {
        logWithException(msg, e)
    }

    override fun info(msg: String) {
        println(msg)
    }

    private fun logWithException(msg: String, e: Throwable?) {
        println(msg)
        e?.printStackTrace()
    }

}
