package com.fisk.twig.editor.actions

import com.fisk.twig.TwigLanguage
import com.fisk.twig.config.TwigConfig
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigPsiUtil
import com.fisk.twig.psi.TwigTag
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

class TwigTypedHandler : TypedHandlerDelegate() {
    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): TypedHandlerDelegate.Result {
        val offset = editor.caretModel.offset

        if (offset == 0 || offset > editor.document.textLength) {
            return TypedHandlerDelegate.Result.CONTINUE
        }

        val previousChar = editor.document.getText(TextRange(offset - 1, offset))

        if (file.language is TwigLanguage) {
            PsiDocumentManager.getInstance(project).commitAllDocuments()

            // we suppress the built-in "}" auto-complete when we see "{{"
            if (c == '{' && previousChar == "{") {
                // since the "}" autocomplete is built in to IDEA, we need to hack around it a bit by
                // intercepting it before it is inserted, doing the work of inserting for the user
                // by inserting the '{' the user just typed...
                editor.document.insertString(offset, Character.toString(c))
                // ... and position their caret after it as they'd expect...
                editor.caretModel.moveToOffset(offset + 1)

                // ... then finally telling subsequent responses to this charTyped to do nothing
                return TypedHandlerDelegate.Result.STOP
            }
        }

        return TypedHandlerDelegate.Result.CONTINUE
    }

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): TypedHandlerDelegate.Result {
        val offset = editor.caretModel.offset
        val provider = file.viewProvider

        autoInsertCloseTag(project, offset, editor, provider)
        return TypedHandlerDelegate.Result.CONTINUE
    }

    private fun getOpenTagForBlock(psi: PsiElement): PsiElement? {
        val openTag = TwigPsiUtil.findParentOpenTagElement(psi)

        if (openTag != null && openTag.children.size > 1) {
            return PsiTreeUtil.findChildOfType(openTag, TwigTag::class.java)
        }

        return null
    }

    private fun shouldAutocompleteEndTagForBlock(psi: PsiElement): Boolean {
        val baseTags = arrayOf("if", "for", "block", "embed")
        val tagName = getOpenTagForBlock(psi)

        tagName?.let {
            if (baseTags.contains(tagName.text)) {
                return true
            }

            if (tagName.text == "set") {
                // todo: {% set %} is a special case where it can be used as a standalone statement
            }
        }

        return false
    }


    private fun autoInsertCloseTag(project: Project, offset: Int, editor: Editor, provider: FileViewProvider) {
        if (!TwigConfig.isAutoGenerateCloseTagEnabled) {
            return
        }

        PsiDocumentManager.getInstance(project).commitDocument(editor.document)

        val elementAtCaret = provider.findElementAt(offset - 1, TwigLanguage::class.java)

        if (elementAtCaret == null || elementAtCaret.node.elementType !== TwigTokenTypes.STATEMENT_CLOSE) {
            return
        }

        if (shouldAutocompleteEndTagForBlock(elementAtCaret)) {
            val openTag = getOpenTagForBlock(elementAtCaret)
            editor.document.insertString(offset, "{% end${openTag?.text} %}")
        }
    }
}