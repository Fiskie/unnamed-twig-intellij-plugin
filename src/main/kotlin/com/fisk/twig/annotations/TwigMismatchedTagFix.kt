package com.fisk.twig.annotations

import com.fisk.twig.TwigBundle
import com.fisk.twig.psi.TwigBlockEndStatement
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

class TwigMismatchedTagFix(
        val correctedName: String,
        val updateOpenTag: Boolean
) : IntentionAction {
    override fun startInWriteAction() = true

    override fun getFamilyName() = getName()

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) = true

    override fun getText() = getName()

    private fun getName() = when (updateOpenTag) {
        true -> TwigBundle.message("twig.tag.mismatch.intention.rename.open", getReplacementText())
        false -> TwigBundle.message("twig.tag.mismatch.intention.rename.close", getReplacementText())
    }

    private fun getReplacementText() = when (updateOpenTag) {
        true -> correctedName
        false -> "end$correctedName"
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val offset = editor?.caretModel?.offset!!
        val element = file?.findElementAt(offset)

        val endStatement = PsiTreeUtil.findFirstParent(element, { el ->
            el is TwigBlockEndStatement
        }) as? TwigBlockEndStatement ?: return

        val target = when {
            updateOpenTag -> endStatement.getMatchingStartStatement()
            else -> endStatement
        }?.tag

        target?.let {
            val doc = PsiDocumentManager.getInstance(project).getDocument(file!!)
            val textRange = target.textRange
            doc?.replaceString(textRange.startOffset, textRange.endOffset, getReplacementText())
        }
    }
}