package wxsgen.util

import java.nio.file.Files
import java.nio.file.Path

fun Path.readToString() : String {
    val generatedWxsReader = Files.newBufferedReader(this)
    var content = ""
    generatedWxsReader.use {
        content = generatedWxsReader.readText()
    }
    return content
}
