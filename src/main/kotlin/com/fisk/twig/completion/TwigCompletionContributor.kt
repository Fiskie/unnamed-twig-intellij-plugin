package com.fisk.twig.completion

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigBlockStartStatement
import com.fisk.twig.psi.TwigExpression
import com.fisk.twig.psi.TwigTag
import com.fisk.twig.psi.util.TwigPsiUtil
import com.fisk.twig.util.TwigTagUtil
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

data class LoopProperty(
        val prop: String,
        val typeText: String
)

class TwigCompletionContributor : CompletionContributor() {
    val tags = setOf(
            "if", "for", "include", "extends", "with", "block", "embed", "else",
            "sandbox", "spaceless", "verbatim", "import",
            "macro", "set", "flush", "autoescape"
    )

    val loopProperties = setOf(
            LoopProperty("index", "The current iteration of the loop. (1 indexed)"),
            LoopProperty("index0", "The current iteration of the loop. (0 indexed)"),
            LoopProperty("revindex", "The number of iterations from the end of the loop (1 indexed)"),
            LoopProperty("revindex0", "The number of iterations from the end of the loop (0 indexed)"),
            LoopProperty("first", "True if first iteration"),
            LoopProperty("last", "True if last iteration"),
            LoopProperty("length", "The number of items in the sequence"),
            LoopProperty("parent", "The parent context")
    )

    init {
        extend(CompletionType.BASIC, psiElement(TwigTokenTypes.LABEL), object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                // adds the special loop variable to loop contexts
                if (TwigPsiUtil.isInLoopContext(parameters.position)) {
                    val expr = PsiTreeUtil.findFirstParent(parameters.position, { el -> el is TwigExpression })

                    expr?.let {
                        if (expr.children.size == 1) {
                            // must be the first label in the expression, so show loop var + properties
                            // (it's a bit buggy if we do it after the dot right now)
                            result.addAllElements(loopProperties.map {
                                object : LookupElement() {
                                    override fun getLookupString() = "loop.${it.prop}"

                                    override fun renderElement(presentation: LookupElementPresentation?) {
                                        presentation?.itemText = lookupString
                                        presentation?.typeText = it.typeText
                                        presentation?.icon = TwigIcons.file_icon
                                    }
                                }
                            })
                        }
                    }
                }

                result.stopHere()
            }
        })

        extend(CompletionType.BASIC, psiElement(TwigTokenTypes.TAG), object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                val prevTags = findUnclosedStartTags(parameters.originalFile, parameters.offset)

                result.addAllElements(prevTags.toSet().map {
                    object : LookupElement() {
                        override fun getLookupString() = it

                        override fun renderElement(presentation: LookupElementPresentation?) {
                            presentation?.itemText = lookupString
                            presentation?.typeText = "Close tag for open block"
                            presentation?.icon = TwigIcons.file_icon
                        }
                    }
                })

                result.addAllElements(tags.map {
                    object : LookupElement() {
                        override fun getLookupString() = it

                        override fun renderElement(presentation: LookupElementPresentation?) {
                            presentation?.itemText = lookupString
                            presentation?.icon = TwigIcons.file_icon
                        }
                    }
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
                    !TwigTagUtil.isEndTag(it.name ?: "")
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