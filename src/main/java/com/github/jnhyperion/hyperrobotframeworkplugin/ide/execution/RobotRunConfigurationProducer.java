package com.github.jnhyperion.hyperrobotframeworkplugin.ide.execution;

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
import com.jetbrains.python.actions.PyExecuteSelectionAction;
import org.jetbrains.annotations.NotNull;

public class RobotRunConfigurationProducer extends LazyRunConfigurationProducer<PythonRunConfiguration> {

    @Override
    protected boolean setupConfigurationFromContext(@NotNull PythonRunConfiguration runConfig,
                                                    @NotNull ConfigurationContext context,
                                                    @NotNull Ref<PsiElement> sourceElement) {
        Location<?> location = context.getLocation();
        if (location == null) {
            return false;
        }
        runConfig.setUseModuleSdk(false);
        runConfig.setModuleMode(true);
        runConfig.setScriptName("robot.run");
        runConfig.setWorkingDirectory(context.getProject().getBasePath());
        VirtualFile file = location.getVirtualFile();
        if (file == null) {
            return false;
        }
        String pathFile = file.getPath().replace(context.getProject().getBasePath() + "/", "");
        String testCaseName = getTestCaseName(context);
        runConfig.setScriptParameters(buildParameters(testCaseName, pathFile));

        Sdk sdk = ProjectRootManager.getInstance(context.getProject()).getProjectSdk();
        if (sdk != null) {
            runConfig.setSdkHome(sdk.getHomePath());//runConfig.setSdk(sdk);
        }
        // robot file
        if (testCaseName.startsWith("***")) {
            return false;
        }

        runConfig.setName(testCaseName);

        if (file.getName().endsWith(".robot")) {
            return true;
        }
        return false;
    }

    public String buildParameters(String testCaseName, String scriptFileName) {
        return " -t \"" + testCaseName + "\" " + scriptFileName;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull PythonRunConfiguration runConfig,
                                              @NotNull ConfigurationContext context) {
        Location<?> location = context.getLocation();
        if (location == null) {
            return false;
        }

        VirtualFile file = location.getVirtualFile();
        if (file == null) {
            return false;
        }

        String pathFile = file.getPath().replace(context.getProject().getBasePath() + "/", "");
        if (runConfig.getScriptParameters().trim().equals(buildParameters(getTestCaseName(context), pathFile).trim())) {
            return true;
        }

        return false;
    }

    private String getTestCaseName(ConfigurationContext context) {
        Location location = context.getLocation();
        VirtualFile file = location.getVirtualFile();
        if (file.getName().endsWith(".robot")) {
            PsiElement e = location.getPsiElement();
            String text = e.getText();
            String pText1 = e.getParent().getText();
            String pText2 = e.getParent().getParent().getText();
            String pText3 = e.getParent().getParent().getParent().getText();
            if (pText1.contains("\n")) {
                return pText1.split("\n")[0];
            }
            if (pText2.contains("\n")) {
                return pText2.split("\n")[0];
            }
            if (pText3.contains("\n")) {
                return pText3.split("\n")[0];
            }
            while (text.startsWith(" ") || text.startsWith("\t") || text.startsWith("\n") || text.equals("")) {
                e = e.getPrevSibling();
                text = e.getText();
            }
            return text;
        }
        return "";
    }


    @NotNull
    @Override
    public ConfigurationFactory getConfigurationFactory() {
        return PythonConfigurationType.getInstance().getConfigurationFactories()[0];
    }
}