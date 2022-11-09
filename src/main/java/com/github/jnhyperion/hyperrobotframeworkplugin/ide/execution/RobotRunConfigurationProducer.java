package com.github.jnhyperion.hyperrobotframeworkplugin.ide.execution;

import com.github.jnhyperion.hyperrobotframeworkplugin.psi.RobotFeatureFileType;
import com.github.jnhyperion.hyperrobotframeworkplugin.psi.element.DefinedKeyword;
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

import java.util.Collection;

public class RobotRunConfigurationProducer extends LazyRunConfigurationProducer<PythonRunConfiguration> {

    @Override
    protected boolean setupConfigurationFromContext(@NotNull PythonRunConfiguration runConfig,
                                                    @NotNull ConfigurationContext context,
                                                    @NotNull Ref<PsiElement> sourceElement) {
        if (isValidRobotExecutableScript(context)) {
            String runParam = getRunParameters(context);
            runConfig.setUseModuleSdk(false);
            runConfig.setModuleMode(true);
            runConfig.setScriptName("robot.run");
            if (runConfig.getWorkingDirectory() == null || runConfig.getWorkingDirectory().isEmpty()) {
                runConfig.setWorkingDirectory(context.getProject().getBasePath());
            }
            runConfig.setScriptParameters(runConfig.getScriptParameters() + " " + runParam);
            Sdk sdk = ProjectRootManager.getInstance(context.getProject()).getProjectSdk();
            if (sdk != null) {
                runConfig.setSdkHome(sdk.getHomePath());
            }
            runConfig.setName(getRunDisplayName(context));
            return true;
        }
        return false;
    }

    @NotNull
    private static String getRunParameters(ConfigurationContext context) {
        String suiteName = getSuiteName(context);
        String testCaseName = getTestCaseName(context);
        return !testCaseName.equals("") ? "--test \"" + suiteName + "." + testCaseName + "\" ." :
                "--suite \"" + suiteName + "\" .";
    }

    @NotNull
    private static String getRunDisplayName(ConfigurationContext context) {
        Location<?> location = context.getLocation();
        assert location != null;
        VirtualFile file = location.getVirtualFile();
        assert file != null;
        String testCaseName = getTestCaseName(context);
        return !testCaseName.equals("") ? testCaseName : file.getName();
    }

    @NotNull
    private static String getSuiteName(ConfigurationContext context) {
        String projectName = context.getProject().getName();
        Location<?> location = context.getLocation();
        assert location != null;
        VirtualFile file = location.getVirtualFile();
        assert file != null;
        String suitePathName = file.getPath()
                .replace(context.getProject().getBasePath() + "/", "")
                .replace("/", ".");
        suitePathName = suitePathName.substring(0, suitePathName.lastIndexOf('.'));
        return projectName + "." + suitePathName;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull PythonRunConfiguration runConfig,
                                              @NotNull ConfigurationContext context) {
        if (isValidRobotExecutableScript(context)) {
            String runParam = getRunParameters(context);
            boolean ret = runParam.trim().
                    equals(runConfig.getScriptParameters().trim());
            if (ret) {
                runConfig.setName(getRunDisplayName(context));
            }
            return ret;
        }
        return false;
    }

    private boolean isValidRobotExecutableScript(@NotNull ConfigurationContext context) {
        Location<?> location = context.getLocation();
        if (location == null) {
            return false;
        }
        PsiElement psiElement = location.getPsiElement();
        VirtualFile file = location.getVirtualFile();
        if (file == null) {
            return false;
        }
        if (!(file.getFileType() instanceof RobotFeatureFileType)) {
            return false;
        }
        Collection<DefinedKeyword> testCases = RobotUtil.getTestCasesFromElement(psiElement);
        // do not have test case
        return !testCases.isEmpty();
    }

    @NotNull
    private static String getTestCaseName(@NotNull ConfigurationContext context) {
        Location<?> location = context.getLocation();
        if (location != null) {
            return getKeywordNameFromAnyElement(location.getPsiElement());
        }
        return "";
    }

    @NotNull
    private static String getKeywordNameFromAnyElement(PsiElement element) {
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