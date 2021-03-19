package com.github.jnhyperion.hyperrobotframeworkplugin.ide.inspections.compilation;

import com.github.jnhyperion.hyperrobotframeworkplugin.RobotBundle;
import com.github.jnhyperion.hyperrobotframeworkplugin.ide.inspections.SimpleRobotInspection;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.Argument;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.KeywordInvokable;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.KeywordStatement;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.Variable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author mrubino
 * @since 2014-06-18
 */
public class RobotVariableNotFound extends SimpleRobotInspection {

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return RobotBundle.message("INSP.NAME.variable.undefined");
    }

    @Override
    public boolean skip(PsiElement element) {
        if (element instanceof Variable) {
            PsiReference reference = element.getReference();
            if (reference != null && reference.resolve() != null) {
                return true;
            }
            if (((Variable) element).isNested()) {
                // TODO: nested variables
                return true;
            }

            // TODO: what is needed below this point...
            PsiElement container = element.getParent();
            element = container;
            if (container instanceof Argument) {
                container = container.getParent();
            }
            if (container instanceof KeywordStatement) {
                // this is the case where we have a 'set test variable' call with more than one arg
                // the first is the variable name, the second is the value
                // if there is only one argument then we might want to see where it was created
                if (((KeywordStatement) container).getGlobalVariable() != null) {
                    List<Argument> arguments = ((KeywordStatement) container).getArguments();
                    if (arguments.size() > 1 && element == arguments.get(0)) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getMessage() {
        return RobotBundle.message("INSP.variable.undefined");
    }

    @NotNull
    @Override
    protected String getGroupNameKey() {
        return "INSP.GROUP.compilation";
    }
}
