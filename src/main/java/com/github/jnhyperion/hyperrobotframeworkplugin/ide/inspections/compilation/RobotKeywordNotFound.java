package com.github.jnhyperion.hyperrobotframeworkplugin.ide.inspections.compilation;

import com.github.jnhyperion.hyperrobotframeworkplugin.RobotBundle;
import com.github.jnhyperion.hyperrobotframeworkplugin.ide.inspections.SimpleRobotInspection;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.KeywordStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.KeywordInvokable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author mrubino
 * @since 2014-06-07
 */
public class RobotKeywordNotFound extends SimpleRobotInspection {

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return RobotBundle.message("INSP.NAME.keyword.undefined");
    }

    @Override
    public boolean skip(PsiElement element) {
        if (element instanceof KeywordInvokable) {
            PsiReference reference = element.getReference();
            if (reference != null && reference.resolve() != null) {
                return true;
            }

            String text = ((KeywordInvokable) element).getPresentableText();
            final String[] RESERVED_KW = {"FOR", "END", "IF", "ELSE", "ELSE IF"};
            return Arrays.asList(RESERVED_KW).contains(text);
        } else {
            return true;
        }
    }

    @Override
    public String getMessage() {
        return RobotBundle.message("INSP.keyword.undefined");
    }

    @NotNull
    @Override
    protected String getGroupNameKey() {
        return "INSP.GROUP.compilation";
    }
}
