package com.github.jnhyperion.hyperrobotframeworkplugin.psi.element;

import com.intellij.psi.PsiNamedElement;

/**
 * @author mrubino
 */
public interface VariableDefinition extends RobotStatement, PsiNamedElement {

    // TODO: this should go away once we identify the nesting correctly
    boolean isNested();
}
