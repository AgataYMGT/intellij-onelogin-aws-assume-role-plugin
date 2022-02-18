package com.github.agataymgt.intellijoneloginawsassumeroleplugin.services

import com.intellij.openapi.project.Project
import com.github.agataymgt.intellijoneloginawsassumeroleplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
