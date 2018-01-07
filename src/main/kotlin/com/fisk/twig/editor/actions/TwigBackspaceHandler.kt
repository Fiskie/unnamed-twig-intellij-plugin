package com.fisk.twig.editor.actions

import com.fisk.twig.psi.util.TwigBraceUtil
import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

class TwigBackspaceHandler : BackspaceHandlerDelegate() {
    override fun beforeCharDeleted(c: Char, file: PsiFile?, editor: Editor?) {
    }

    override fun charDeleted(c: Char, file: PsiFile?, editor: Editor?): Boolean {
        println("Char: $c")
        removeWhitespaceControlModifier(c, file!!.project, editor!!, file)
        return true
    }

    private fun removeWhitespaceControlModifier(c: Char, project: Project, editor: Editor, file: PsiFile) {
        if (c != '-') {
            return
        }

        PsiDocumentManager.getInstance(project).commitDocument(editor.document)

        // Whitespace control modifiers
        val openingBrace = file.findElementAt(editor.caretModel.offset - 1)

        openingBrace?.let {
            val closingElementType = TwigBraceUtil.getCloseBraceForElement(openingBrace.node.elementType)

            closingElementType?.let {
                val closingBrace = PsiTreeUtil.findSiblingForward(openingBrace, closingElementType, {})

                closingBrace?.let {
                    // Make sure that the brace lengths are what we expect at this point
                    if (closingBrace.textLength == 3 && openingBrace.textLength == 2) {
                        editor.document.replaceString(closingBrace.textOffset, closingBrace.textOffset + 1, "")
                    }
                }
            }

        }

        val closingBrace = file.findElementAt(editor.caretModel.offset)

        closingBrace?.let {
            val closingElementType = TwigBraceUtil.getOpenBraceForElement(closingBrace.node.elementType)

            closingElementType?.let {
                val openingBrace = PsiTreeUtil.findSiblingBackward(closingBrace, closingElementType, {})

                openingBrace?.let {
                    // Make sure that the brace lengths are what we expect at this point
                    if (closingBrace.textLength == 2 && openingBrace.textLength == 3) {
                        editor.document.replaceString(openingBrace.textOffset + 2, openingBrace.textOffset + 3, "")
                    }
                }
            }
        }
    }
}