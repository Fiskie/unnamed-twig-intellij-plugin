package com.fisk.twig.ide.completion

import com.fisk.twig.TwigTagUtils
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigBlockStartStatement
import com.fisk.twig.psi.TwigTag
import com.fisk.twig.psi.util.TwigPsiUtil
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

class TwigCompletionContributor : CompletionContributor() {
    // todo: detect and suggest closing tag?
    val tags = setOf(
            "if", "for", "include", "extends", "with", "block", "embed", "else",
            "sandbox", "spaceless", "verbatim", "import",
            "macro", "set", "flush", "autoescape"
    )

    init {
        extend(CompletionType.BASIC, psiElement(TwigTokenTypes.TAG), object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                val prevTags = findUnclosedStartTags(parameters.originalFile, parameters.offset)

                result.addAllElements(prevTags.toSet().map {
                    LookupElementBuilder.create(it)
                })

                result.addAllElements(tags.map {
                    LookupElementBuilder.create(it)
                })

                // It's a twig tag so what the hell else is it going to be? stop checking
                result.stopHere()
            }

            private fun findUnclosedStartTags(file: PsiFile, offset: Int): List<String> {
                return PsiTreeUtil.findChildrenOfType(file, TwigTag::class.java).filter {
                    // filters tags that appear later
                    // also the current tag we are typing, to stop it suggesting "ende" or "enden"
                    it.textOffset < offset - 3
                }.filter {
                    // filters tags that are 'end' tags
                    !TwigTagUtils.isEndTag(it.name ?: "")
                }.filter {
                    // filters tags that have a matching close statement
                    val statement = it.parent

                    if (statement is TwigBlockStartStatement) {
                        val end = statement.getMatchingEndStatement()
                        end == null
                    } else {
                        // fall through for single statements
                        true
                    }
                }.map {
                    "end${it.name}"
                }
            }
        })
    }
}