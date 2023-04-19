package com.xiaoyu.imgoptimizer.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.SourceProvider
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.xiaoyu.imgoptimizer.util.Constants
import com.xiaoyu.imgoptimizer.extension.PicOptimizerExtension
import com.xiaoyu.imgoptimizer.task.PicOptimizerTask

/**
 *这是一个用于对PNG文件进行压缩的gradle插件
 * 支持极限压缩和无损压缩
 * */
class PicOptimizerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if(project.plugins.hasPlugin(LibraryPlugin)||project.plugins.hasPlugin(AppPlugin)){
            applyPlugin(project)
        } else {
            throw new IllegalAccessException("PicOptimizerPlugin only works in Android module")
        }
    }
    static void applyPlugin(Project project){
        DomainObjectCollection<BaseVariant> variants
        if(project.plugins.hasPlugin(LibraryPlugin)){
            variants = (DomainObjectCollection<BaseVariant>)project.android.libraryVariants
        }else {
            variants = (DomainObjectCollection<BaseVariant>)project.android.applicationVariants
        }

        def extOptions = project.extensions.create(Constants.EXT_OPTION_NAME,PicOptimizerExtension)

        variants.all { BaseVariant variant->
            List<File> picDirs = []
            variant.sourceSets.each { SourceProvider source ->
                source.resDirectories.each {File res ->
                    if(res.exists()){
                        res.eachDir {
                            if (it.directory && (it.name.startsWith("drawable") || it.name.startsWith("mipmap"))) {
                                picDirs << it
                            }
                        }
                    }
                }
            }
            if(!picDirs.isEmpty()){
                project.tasks.create("${Constants.TASK_NAME}${variant.name.capitalize()}", PicOptimizerTask){
                    it.imgDirs  = picDirs
                    it.triggerSize = extOptions.triggerSize
                    it.type = extOptions.type
                    it.suffix = extOptions.suffix
                    it.group = "optimize"
                    it.description = "Optimize ${variant.name} images"
                    it.fileFilters = extOptions.fileFilters
                }
            }
        }


    }
}