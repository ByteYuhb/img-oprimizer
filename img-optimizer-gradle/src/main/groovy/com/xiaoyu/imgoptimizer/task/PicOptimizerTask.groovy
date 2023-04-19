package com.xiaoyu.imgoptimizer.task

import com.xiaoyu.imgoptimizer.util.Logger
import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import com.xiaoyu.imgoptimizer.optimize.PicOptimizerFactory

class PicOptimizerTask extends DefaultTask {
    @Input
    String suffix

    @Input
    int triggerSize

    @Input
    String type
    /**
     * 图片文件夹(drawable-xxx, mipmap-xxx)
     */
    @Input
    List<File> imgDirs

    @Input
    List<String> fileFilters


    Logger _log

    @TaskAction
    void optimize(){
        _log = new Logger(project)
        def optimizer = PicOptimizerFactory.getOptimizer(type,_log)
        optimizer.optimize(project,suffix,checkFile())
    }

    def checkFile() {
        List<File> files = new ArrayList<>()
        imgDirs.each { dir ->
            dir.eachFile(FileType.FILES) { file ->
                if (file.size() >= (1024 * triggerSize) && !file.name.endsWith('.9.png') &&
                        (file.name.endsWith('.png'))) {
                    //过滤需要优化的文件
                    if(fileFilters==null||!fileFilters.contains(file.name)){
                        files << file
                    }
                }
            }
        }
        _log.i("${files.size()} images need to be optimized.")
        return files
    }
}