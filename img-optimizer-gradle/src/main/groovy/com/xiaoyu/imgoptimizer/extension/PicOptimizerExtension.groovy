package com.xiaoyu.imgoptimizer.extension
import com.xiaoyu.imgoptimizer.util.Constants
/**
 * 创建时间:20230418
 * 作者：xiaoyu
 * **/
class PicOptimizerExtension {

    /**
     * 触发优化的起始大小(kb)
     */
    int triggerSize = 0

    /**
     * 压缩模式(默认有损压缩)
     */
    String type = Constants.LOSSY

    /**
     * 优化后生成的图片名后缀
     */
    String suffix = ''

    List<String> fileFilters = []

}