package wxsgen.common

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import java.io.InputStreamReader


object MustacheUtil {
    fun prepareTemplateFromResource(templatePath: String): Mustache {
        val templateStream = this.javaClass.getResourceAsStream("/$templatePath")
        val templateReader = InputStreamReader(templateStream, "utf-8")

        val mustacheTemplateFactory = DefaultMustacheFactory()
        return mustacheTemplateFactory.compile(templateReader, templatePath)
    }
}
