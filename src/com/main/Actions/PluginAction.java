package com.main.Actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

public class PluginAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here

        PsiClass psiClass = getPsiFromContext(e);
    }

    private PsiClass getPsiFromContext(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Document document = e.getData(PlatformDataKeys.EDITOR).getDocument();
        VirtualFile virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        final int offset = editor.getCaretModel().getOffset();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        PsiElement element = psiFile.findElementAt(offset);
        PsiMethod containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        PsiClass containingClass = containingMethod.getContainingClass();
        final SelectionModel selectionModel = editor.getSelectionModel();
        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();

        Project currentProject = e.getProject();
        StringBuffer dialogMsg = new StringBuffer(e.getPresentation().getText() + " Selected!");
        String dlgTitle = e.getPresentation().getDescription();
        Navigatable nav = e.getData(CommonDataKeys.NAVIGATABLE);

        if (nav != null) {
            dialogMsg.append(String.format("\nSelected Element: %s", nav.toString()));
        }
        Messages.showMessageDialog(currentProject, dialogMsg.toString(), dlgTitle, Messages.getInformationIcon());

        if(psiFile == null || editor == null){
            e.getPresentation().setEnabled(false);
            return null;
        }
        if (containingClass == null){
            e.getPresentation().setEnabled(false);
        }
        return containingClass;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
}
