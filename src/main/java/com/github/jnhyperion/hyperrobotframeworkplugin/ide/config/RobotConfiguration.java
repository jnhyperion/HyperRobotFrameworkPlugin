package com.github.jnhyperion.hyperrobotframeworkplugin.ide.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author mrubino
 * @since 2014-06-26
 */
public class RobotConfiguration implements SearchableConfigurable, Configurable.NoScroll {

    private JPanel panel;
    private JCheckBox enableDebug;
    private JCheckBox allowTransitiveImports;
    private JCheckBox allowGlobalVariables;
    private JCheckBox capitalizeKeywords;
    private JCheckBox inlineVariableSearch;

    @Nullable
    private RobotOptionsProvider getOptionProvider() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length > 0) {
            return RobotOptionsProvider.getInstance(projects[0]);
        } else {
            return null;
        }
    }

    @NotNull
    @Override
    public String getId() {
        return getHelpTopic();
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Robot Options";
    }

    @NotNull
    @Override
    public String getHelpTopic() {
        return "reference.idesettings.robot";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return this.panel;
    }

    @Override
    public boolean isModified() {
        RobotOptionsProvider provider = getOptionProvider();
        if (provider != null) {
            return provider.isDebug() != this.enableDebug.isSelected() ||
                    provider.allowTransitiveImports() != this.allowTransitiveImports.isSelected() ||
                    provider.allowGlobalVariables() != this.allowGlobalVariables.isSelected() ||
                    provider.capitalizeKeywords() != this.capitalizeKeywords.isSelected() ||
                    provider.inlineVariableSearch() != this.inlineVariableSearch.isSelected();
        } else {
            return false;
        }
    }

    @Override
    public void apply() throws ConfigurationException {
        RobotOptionsProvider provider = getOptionProvider();
        if (provider != null) {
            provider.setDebug(this.enableDebug.isSelected());
            provider.setTransitiveImports(this.allowTransitiveImports.isSelected());
            provider.setGlobalVariables(this.allowGlobalVariables.isSelected());
            provider.setCapitalizeKeywords(this.capitalizeKeywords.isSelected());
            provider.setInlineVariableSearch(this.inlineVariableSearch.isSelected());
        }
    }

    @Override
    public void reset() {
        RobotOptionsProvider provider = getOptionProvider();
        if (provider != null) {
            this.enableDebug.setSelected(provider.isDebug());
            this.allowTransitiveImports.setSelected(provider.allowTransitiveImports());
            this.allowGlobalVariables.setSelected(provider.allowGlobalVariables());
            this.capitalizeKeywords.setSelected(provider.capitalizeKeywords());
            this.inlineVariableSearch.setSelected(provider.inlineVariableSearch());
        }
    }

    @Override
    public void disposeUIResources() {
    }
}
