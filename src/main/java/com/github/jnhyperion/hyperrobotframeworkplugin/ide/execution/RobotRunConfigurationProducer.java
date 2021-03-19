package com.github.jnhyperion.hyperrobotframeworkplugin.ide.execution;

import com.github.jnhyperion.hyperrobotframeworkplugin.psi.RobotFeatureFileType;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.DefinedKeyword;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.KeywordDefinitionIdImpl;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.KeywordDefinitionImpl;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.util.RobotUtil;
import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.jetbrains.python.run.PythonConfigurationType;
import com.jetbrains.python.run.PythonRunConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class RobotRunConfigurationProducer extends LazyRunConfigurationProducer<PythonRunConfiguration> {

    @Override
    protected boolean setupConfigurationFromContext(@NotNull PythonRunConfiguration runConfig,
                                                    @NotNull ConfigurationContext context,
                                                    @NotNull Ref<PsiElement> sourceElement) {

        String runParam = getRunParameters(context);
        if (runParam != null) {
            runConfig.setUseModuleSdk(false);
            runConfig.setModuleMode(true);
            runConfig.setScriptName("robot.run");
            runConfig.setWorkingDirectory(context.getProject().getBasePath());
            runConfig.setScriptParameters(runParam);
            Sdk sdk = ProjectRootManager.getInstance(context.getProject()).getProjectSdk();
            if (sdk != null) {
                runConfig.setSdkHome(sdk.getHomePath());
            }
            runConfig.setName(getTestCaseName(context));
            return true;
        }
        return false;
    }

    @Nullable
    private String getRunParameters(ConfigurationContext context) {
        Location<?> location = context.getLocation();
        if (location == null) {
            return null;
        }
        VirtualFile file = location.getVirtualFile();
        if (file == null) {
            return null;
        }
        if (!(file.getFileType() instanceof RobotFeatureFileType)) {
            return null;
        }
        Collection<DefinedKeyword> testCases = RobotUtil.getTestCasesFromElement(location.getPsiElement());
        if (testCases.isEmpty()) {
            // do not have test case
            return null;
        }
        String projectName = context.getProject().getName();
        String suitePathName = file.getPath()
                .replace(context.getProject().getBasePath() + "/", "")
                .replace("/", ".");
        suitePathName = suitePathName.substring(0, suitePathName.lastIndexOf('.'));
        String suiteName = projectName + "." + suitePathName;
        String testCaseName = getTestCaseName(context);
        return !testCaseName.equals("") ? "--test \"" + suiteName + "." + testCaseName + "\" ." :
                "--suite \"" + suiteName + "\" .";
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull PythonRunConfiguration runConfig,
                                              @NotNull ConfigurationContext context) {
        String runParam = getRunParameters(context);
        if (runParam != null) {
            return runConfig.getScriptParameters().trim().
                    equals(runParam.trim());
        }
        return false;
    }

    private String getTestCaseName(ConfigurationContext context) {
        Location<?> location = context.getLocation();
        if (location != null) {
            return getKeywordNameFromAnyElement(location.getPsiElement());
        }
        return "";
    }

    @NotNull
    private String getKeywordNameFromAnyElement(PsiElement element) {
        while (true) {
            if (element instanceof KeywordDefinitionImpl) {
                return ((KeywordDefinitionImpl) element).getKeywordName();
            }
            element = element.getParent();
            if (element == null) {
                return "";
            }
        }
    }


    @NotNull
    @Override
    public ConfigurationFactory getConfigurationFactory() {
        return PythonConfigurationType.getInstance().getConfigurationFactories()[0];
    }
}