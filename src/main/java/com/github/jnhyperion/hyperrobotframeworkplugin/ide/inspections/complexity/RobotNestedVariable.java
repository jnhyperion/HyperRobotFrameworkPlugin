package com.github.jnhyperion.hyperrobotframeworkplugin.ide.inspections.complexity;

import com.github.jnhyperion.hyperrobotframeworkplugin.RobotBundle;
import com.github.jnhyperion.hyperrobotframeworkplugin.ide.inspections.SimpleRobotInspection;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.RobotTokenTypes;
import com.intellij.psi.PsiElement;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.Variable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * @author mrubino
 * @since 2015-08-24
 */
public class RobotNestedVariable extends SimpleRobotInspection {

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return RobotBundle.message("INSP.NAME.variable.nested");
    }

    @Override
    public boolean skip(PsiElement element) {
        if (element.getNode().getElementType() != RobotTokenTypes.VARIABLE) {
            return true;
        }
        PsiElement parent = element.getParent();
        return !(parent instanceof Variable) || !((Variable) parent).isNested();
    }

    @Override
    public String getMessage() {
        return RobotBundle.message("INSP.variable.nested");
    }

    @NotNull
    @Override
    protected String getGroupNameKey() {
        return "INSP.GROUP.complexity";
    }
}
