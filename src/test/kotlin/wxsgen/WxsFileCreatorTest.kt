package wxsgen

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Java6Assertions.assertThat
import wxsgen.common.MustacheUtil
import wxsgen.model.WxsGeneratorParameter
import wxsgen.service.UuidGenerator
import wxsgen.service.WxsFileGenerator
import java.io.FileReader
import java.io.StringWriter
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class WxsFileCreatorTest {

    val testLogger = TestLogImpl()
    val uuidGeneratorService: UuidGenerator = mock()

    private var root: Path? = null
    private val testTargetFile: Path = Paths.get("tmp/test.wxs")
    private val expectedFile: Path = Paths.get("src/test/resources/expected.wxs")

    class ExpectedTemplate(val testDirectory:String)

    @BeforeTest
    fun setup() {
        root = createSampleFiles()
    }

    private fun buildExpectedWxs() : String  {
        val mustacheTemplate = MustacheUtil.prepareTemplateFromResource("expected.wxs.mustache")
        val writer = StringWriter()
        mustacheTemplate.execute(writer, ExpectedTemplate(root!!.toAbsolutePath().toString()))
        return writer.toString()
    }

    @Test
    fun createWXSTest() {

        // given
        whenever(uuidGeneratorService.generateUuid()).thenReturn("new-UUID")
        val testData = WxsGeneratorParameter(
            productUid = "myprodiuct.id",
            rootPath = root.toString(),
            targetFile = testTargetFile.toString(),
            productVersion = "0.5.1",
            mainExecutable = "run.bat",
            productComment = "test comment",
            iconPath = "",
            licenceRtfPath = "",
            installerLocale = "de-de",
            manufacturer = "test orga name",
            productName = "test product 1.5",
            requestAdminPrivileges = true,
            autostart = true
        )

        // when
        val wxsGenerator = WxsFileGenerator(testLogger, uuidGeneratorService)
        wxsGenerator.generate(testData)

        // then
        val expectedWxs = buildExpectedWxs()
        val generatedWxs = FileReader(testTargetFile.toFile()).readText()
        assertThat(generatedWxs).isEqualTo(expectedWxs)
    }

    @AfterTest
    fun teardown() {
        FileUtils.deleteDirectory(root!!.toFile())
        FileUtils.deleteDirectory(testTargetFile.parent.toFile())
    }
}

