package com.xiaoyu.imgoptimizer.optimize

import org.gradle.api.Project

trait PicOptimizer {
    /**
     * @param project Project
     * @param suffix String
     * @param files List<File>
     */
    abstract void optimize(Project project, String suffix, List<File> files)
}