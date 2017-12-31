package com.fisk.twig.completion

import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext

class TwigCompletionContributor : CompletionContributor() {
    // todo: detect and suggest closing tag?
    val tags = setOf(
            "if", "for", "include", "extends", "with", "block", "embed","else", "endif", "endfor", "endblock",
            "endembed", "sandbox", "endsandbox", "spaceless", "endspaceless", "verbatim", "endverbatim", "import",
            "macro", "endmacro", "set", "endset", "flush", "autoescape", "endautoescape"
    )

    init {
        extend(CompletionType.BASIC, psiElement(TwigTokenTypes.TAG), object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                tags.forEach({
                    result.addElement(LookupElementBuilder.create(it))
                })
            }
        })
    }
}