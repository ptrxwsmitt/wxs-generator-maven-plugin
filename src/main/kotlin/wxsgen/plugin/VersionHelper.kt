package wxsgen.plugin

import java.lang.StringBuilder

const val VERSION_PATTERN = "\\d+(\\.\\d+){0,4}(-.*)?"

fun isVersionValid(version: String): Boolean {
    val pattern = Regex(VERSION_PATTERN)
    return version.matches(pattern)
}


fun normalizeVersion(version: String): String {
    //remove everything after the first '-'
    val versionOnly = version.substringBefore('-')
    val versionNumberArray = versionOnly.split(".")
    //maximum of 3 version numbers
    val versionNumberCount = Math.min(3, versionNumberArray.size)
    val targetVersionSB = StringBuilder()
    for (i in 0 until versionNumberCount) {
        if(i!=0) {
            targetVersionSB.append('.')
        }
        targetVersionSB.append(versionNumberArray[i])
    }
    return targetVersionSB.toString()
}
