package wxsgen

import wxsgen.common.MustacheUtil
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

private const val TEMP_PREFIX = "wixtest"

fun createSampleFiles(): Path {
    val root = Files.createTempDirectory(TEMP_PREFIX)

    val batFile = Paths.get(root.toString(), "run.bat")
    Files.write(batFile, "@echo off\n".toByteArray(StandardCharsets.UTF_8))

    val confDir = Paths.get(root.toString(), "conf")
    Files.createDirectories(confDir)
    val conf1File = Paths.get(confDir.toString(), "app.conf")
    Files.write(conf1File, "abc=xyz\n".toByteArray(StandardCharsets.UTF_8))
    val conf2File = Paths.get(confDir.toString(), "log.xml")
    Files.write(conf2File, "<log>\n</log>".toByteArray(StandardCharsets.UTF_8))

    val libDir = Paths.get(root.toString(), "lib")
    Files.createDirectories(libDir)
    val lib1File = Paths.get(libDir.toString(), "apache.jar")
    Files.write(lib1File, "13523514152612351512732525".toByteArray(StandardCharsets.UTF_8))
    val lib2File = Paths.get(libDir.toString(), "hibernate.jar")
    Files.write(lib2File, "1567314142625124121736".toByteArray(StandardCharsets.UTF_8))
    val lib3File = Paths.get(libDir.toString(), "random.jar")
    Files.write(lib3File, "89536625252272525622232622".toByteArray(StandardCharsets.UTF_8))

    return root
}

 fun fillTemplate(template:String, data: Any?): String {
    val mustacheTemplate = MustacheUtil.prepareTemplateFromResource(template)
    val writer = StringWriter()
    writer.use {
        mustacheTemplate?.execute(it, data)
    }
    return writer.toString()
}
