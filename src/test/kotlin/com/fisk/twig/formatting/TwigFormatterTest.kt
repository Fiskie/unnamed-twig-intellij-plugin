package com.fisk.twig.formatting

import com.fisk.twig.TwigLanguage
import com.fisk.twig.util.TwigTestUtils
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.impl.DocumentImpl
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.intellij.util.IncorrectOperationException
import org.jetbrains.annotations.NonNls
import java.io.File

abstract class TwigFormatterTest : LightPlatformCodeInsightFixtureTestCase(), TwigFormattingModelBuilderTest {
    private val TEST_DATA_PATH = File(TwigTestUtils.BASE_TEST_DATA_PATH, "formatter").absolutePath

    private lateinit var formatterTestSettings: FormatterTestSettings

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()

        formatterTestSettings = FormatterTestSettings(CodeStyleSettingsManager.getSettings(project))
        formatterTestSettings.setUp()
    }

    @Throws(Exception::class)
    override fun tearDown() {
        try {
            formatterTestSettings.tearDown()
        } finally {
            super.tearDown()
        }
    }

    /**
     * Passes [com.intellij.testFramework.UsefulTestCase.getTestName]
     * as a parameter to [.doFileBasedTest]
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    internal fun doFileBasedTest() {
        doFileBasedTest(getTestName(false) + ".twig")
    }

    /**
     * Call this to run the formatter on a test file in the [.TEST_DATA_PATH] directory.
     *
     *
     * The test will validate the results against a file of the same name with "_expected" appended.
     * (i.e. for fileNameBefore "TestFile.twig", the formatter will be run on [.TEST_DATA_PATH]/TestFile.twig
     * the test will look for [.TEST_DATA_PATH]/TestFile_expected.twig to validate the results).
     *
     * @param fileNameBefore The name of the file to test (must have the '.twig' extension).
     * @throws Exception
     */
    @Throws(Exception::class) private fun doFileBasedTest(@NonNls fileNameBefore: String) {
        doFileBasedTest(fileNameBefore, TwigLanguage.defaultTemplateLang)
    }

    /**
     * Specialization of [.doFileBasedTest] which adds the option of specifying a templated language
     * other than [com.fisk.twig.TwigLanguage.defaultTemplateLang]
     *
     * @param fileNameBefore           The name of the file to test
     * @param templateDataLanguageType The LanguageFileType of the templated file
     * @throws Exception
     */
    @Throws(Exception::class)
    internal fun doFileBasedTest(@NonNls fileNameBefore: String, templateDataLanguageType: LanguageFileType) {
        doTextTest(loadFile(fileNameBefore), loadFile(fileNameBefore.replace(".twig", ".after.twig")), templateDataLanguageType)
    }

    @Throws(IncorrectOperationException::class)
    internal fun doStringBasedTest(@NonNls text: String, @NonNls textAfter: String) {
        doTextTest(text, textAfter, TwigLanguage.defaultTemplateLang)
    }

    private abstract class FormatRunnableFactory {
        internal abstract fun createFormatRunnable(file: PsiFile?): Runnable
    }

    /**
     * This method runs both a full-file reformat on beforeText, and a line-by-line reformat.  Though the tests
     * would output slightly better errors if these were separate tests, enforcing that they are always both run
     * for any test defined is the easiest way to ensure that the line-by-line is not messed up by formatter changes
     *
     * @param beforeText               The text run the formatter on
     * @param textAfter                The expected result after running the formatter
     * @param templateDataLanguageType The templated language of the file
     * @throws IncorrectOperationException
     */
    @Throws(IncorrectOperationException::class) private fun doTextTest(beforeText: String, textAfter: String, templateDataLanguageType: LanguageFileType) {
        // define action to run "Reformat Code" on the whole "file" defined by beforeText
        val fullFormatRunnableFactory = object : FormatRunnableFactory() {
            override fun createFormatRunnable(file: PsiFile?): Runnable {
                return Runnable {
                    try {
                        val rangeToUse = file!!.textRange
                        val styleManager = CodeStyleManager.getInstance(project)
                        styleManager.reformatText(file, rangeToUse.startOffset, rangeToUse.endOffset)
                    } catch (e: IncorrectOperationException) {
                        assertTrue(e.localizedMessage, false)
                    }
                }
            }
        }

        doFormatterActionTest(fullFormatRunnableFactory, beforeText, textAfter, templateDataLanguageType)
    }

    private fun doFormatterActionTest(formatAction: FormatRunnableFactory,
                                      beforeText: String,
                                      textAfter: String,
                                      templateDataLanguageType: LanguageFileType) {
        val baseFile = myFixture.configureByText("A.twig", beforeText)

        val virtualFile = baseFile.virtualFile!!
        TemplateDataLanguageMappings.getInstance(project).setMapping(virtualFile, templateDataLanguageType.language)

        // fetch a fresh instance of the file -- the template data mapping creates a new instance,
        // which was causing problems in PsiFileImpl.isValid()
        val file = PsiManager.getInstance(project).findFile(virtualFile)!!

        CommandProcessor.getInstance().executeCommand(project,
                {
                    ApplicationManager.getApplication()
                            .runWriteAction(formatAction.createFormatRunnable(file))
                }, "", "")

        TemplateDataLanguageMappings.getInstance(project).cleanupForNextTest()

        assertEquals("Reformat Code failed", prepareText(textAfter), prepareText(file.text))
    }

    private fun prepareText(actual: String): String {
        var actual = actual
        actual = StringUtil.trimStart(actual, "\n")
        actual = StringUtil.trimStart(actual, "\n")

        // Strip trailing spaces
        val doc = EditorFactory.getInstance().createDocument(actual)
        CommandProcessor.getInstance().executeCommand(project, { ApplicationManager.getApplication().runWriteAction { (doc as DocumentImpl).stripTrailingSpaces(project) } }, "formatting", null)

        return doc.text
    }

    @Throws(Exception::class)
    private fun loadFile(name: String): String {
        val fullName = File(TEST_DATA_PATH, name).absolutePath
        var text = FileUtil.loadFile(File(fullName))
        text = StringUtil.convertLineSeparators(text)
        return text
    }
}