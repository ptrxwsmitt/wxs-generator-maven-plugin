package wxsgen.service

import java.util.*

class JdkUuidGenerator : UuidGenerator {
    override fun generateUuid() = UUID.randomUUID().toString()
}
