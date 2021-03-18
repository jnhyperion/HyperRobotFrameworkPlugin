package com.github.jnhyperion.hyperrobotframeworkplugin.psi;

import com.github.jnhyperion.hyperrobotframeworkplugin.ide.icons.RobotIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author mrubino
 */
public class RobotResFileType extends LanguageFileType {

    private static final RobotResFileType INSTANCE = new RobotResFileType();

    private RobotResFileType() {
        super(RobotLanguage.INSTANCE);
    }

    public static RobotResFileType getInstance() {
        return INSTANCE;
    }

    @NotNull
    public String getName() {
        return "Robot Resource";
    }

    @NotNull
    public String getDescription() {
        return "Robot Resource Files";
    }

    @NotNull
    public String getDefaultExtension() {
        return "resource";
    }

    @Nullable
    public Icon getIcon() {
        return RobotIcons.FILE;
    }
}
