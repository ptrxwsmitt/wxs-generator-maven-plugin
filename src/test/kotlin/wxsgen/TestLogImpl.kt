package wxsgen

import wxsgen.log.LogFacade

class TestLogImpl : LogFacade {
    override fun debug(msg: String, e: Throwable?) {
        println(msg)
        e?.printStackTrace()
    }

    override fun error(msg: String, e: Throwable?) {
        println(msg)
        e?.printStackTrace()
    }

    override fun warn(msg: String, e: Throwable?) {
        println(msg)
        e?.printStackTrace()
    }

    override fun info(msg: String) {
        println(msg)
    }

}
