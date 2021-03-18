package com.github.jnhyperion.hyperrobotframeworkplugin.psi.manip;

import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.KeywordInvokable;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * we seem to need this but it is not really used at this time.  it prevents NPEs for the jump to source.
 * we likely need it to do intelligent refactoring.
 *
 * @author mrubino
 */
public class KeywordInvokableManipulator extends AbstractElementManipulator<KeywordInvokable> {

    @Override
    public KeywordInvokable handleContentChange(@NotNull KeywordInvokable element, @NotNull TextRange range,
                                                String newContent) throws IncorrectOperationException {
        return null;
    }
}
