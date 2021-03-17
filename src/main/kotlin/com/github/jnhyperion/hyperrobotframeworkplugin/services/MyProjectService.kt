package com.github.jnhyperion.hyperrobotframeworkplugin.services

import com.github.jnhyperion.hyperrobotframeworkplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
