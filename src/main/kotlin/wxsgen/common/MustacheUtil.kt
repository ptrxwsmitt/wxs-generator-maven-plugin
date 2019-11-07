package wxsgen.common

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import java.io.InputStreamReader


object MustacheUtil {
    fun prepareTemplateFromResource(templatePath: String): Mustache? {
        val templateStream = this.javaClass.getResourceAsStream("/${templatePath}")

        val mustacheTemplateFactory = DefaultMustacheFactory()

        val templateReader = InputStreamReader(templateStream, "utf-8")
        var mustache: Mustache? = null
        templateReader.use {
            mustache = mustacheTemplateFactory.compile(templateReader, templatePath)
        }
        return mustache
    }
}
