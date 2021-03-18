package com.github.jnhyperion.hyperrobotframeworkplugin.psi.manip;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.Argument;
import org.jetbrains.annotations.NotNull;

/**
 * we seem to need this but it is not really used at this time.  it prevents NPEs for the jump to source.
 * we likely need it to do intelligent refactoring.
 *
 * @author Scott Albertine
 */
public class ArgumentManipulator extends AbstractElementManipulator<Argument> {

    @Override
    public Argument handleContentChange(@NotNull Argument element, @NotNull TextRange range,
                                        String newContent) throws IncorrectOperationException {
        return null;
    }
}
