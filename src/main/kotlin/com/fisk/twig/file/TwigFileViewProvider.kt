package com.fisk.twig.file

import com.fisk.twig.TwigLanguage
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.OUTER_ELEMENT_TYPE
import com.intellij.lang.Language
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutors
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import com.intellij.util.containers.ContainerUtil
import gnu.trove.THashSet
import java.util.*

class TwigFileViewProvider @JvmOverloads constructor(manager: PsiManager, file: VirtualFile, physical: Boolean, private val myBaseLanguage: Language, private val myTemplateLanguage: Language = getTemplateDataLanguage(manager, file)) : MultiplePsiFilesPerDocumentFileViewProvider(manager, file, physical), ConfigurableTemplateLanguageFileViewProvider {
    override fun supportsIncrementalReparse(rootLanguage: Language): Boolean {
        return false
    }

    override fun getBaseLanguage() = myBaseLanguage
    override fun getTemplateDataLanguage() = myTemplateLanguage
    override fun getLanguages() = THashSet(Arrays.asList(myBaseLanguage, templateDataLanguage))

    override fun cloneInner(virtualFile: VirtualFile): MultiplePsiFilesPerDocumentFileViewProvider {
        return TwigFileViewProvider(manager, virtualFile, false, myBaseLanguage, myTemplateLanguage)
    }

    override fun createFile(lang: Language): PsiFile? {
        val parserDefinition = getDefinition(lang) ?: return null

        if (lang.`is`(templateDataLanguage)) {
            val file = parserDefinition.createFile(this) as PsiFileImpl
            file.contentElementType = getTemplateDataElementType(baseLanguage)
            return file
        } else return if (lang.isKindOf(baseLanguage)) {
            parserDefinition.createFile(this)
        } else {
            null
        }
    }

    private fun getDefinition(lang: Language) = when {
        lang.isKindOf(baseLanguage) -> {
            LanguageParserDefinitions.INSTANCE.forLanguage(if (lang.`is`(baseLanguage)) lang else baseLanguage)
        }
        else -> {
            LanguageParserDefinitions.INSTANCE.forLanguage(lang)
        }
    }

    companion object {
        private val TEMPLATE_DATA_TO_LANG = ContainerUtil.newConcurrentMap<String, TemplateDataElementType>()

        private fun getTemplateDataElementType(lang: Language): TemplateDataElementType {
            val result = TEMPLATE_DATA_TO_LANG[lang.id]

            if (result != null) return result
            val created = TemplateDataElementType("TWIG_TEMPLATE_DATA", lang, CONTENT, OUTER_ELEMENT_TYPE)
            val prevValue = (TEMPLATE_DATA_TO_LANG as java.util.Map<String, TemplateDataElementType>).putIfAbsent(lang.id, created)

            return prevValue ?: created
        }

        private fun getTemplateDataLanguage(manager: PsiManager, file: VirtualFile): Language {
            var dataLang = TemplateDataLanguageMappings.getInstance(manager.project).getMapping(file)
            if (dataLang == null) {
                dataLang = TwigLanguage.defaultTemplateLang.language
            }

            val substituteLang = LanguageSubstitutors.INSTANCE.substituteLanguage(dataLang, file, manager.project)

            // only use a substituted language if it's templateable
            if (TemplateDataLanguageMappings.getTemplateableLanguages().contains(substituteLang)) {
                dataLang = substituteLang
            }

            return dataLang
        }
    }
}
