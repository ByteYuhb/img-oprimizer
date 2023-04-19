package com.xiaoyu.imgoptimizer.optimize

import com.xiaoyu.imgoptimizer.optimize.imple.PngquantOptimizerImpl
import com.xiaoyu.imgoptimizer.optimize.imple.ZopflipngOptimizerImpl
import com.xiaoyu.imgoptimizer.util.Constants
import com.xiaoyu.imgoptimizer.util.Logger


/**
 *压缩工具工厂类
 */
class PicOptimizerFactory {

    private PicOptimizerFactory() {}

    static PicOptimizer getOptimizer(String type,Logger logger) {
        if (Constants.LOSSY == type) {
            return new PngquantOptimizerImpl(logger)
        } else if (Constants.LOSSLESS == type) {
            return new ZopflipngOptimizerImpl(logger)
        } else {
            throw new IllegalArgumentException("Unacceptable optimizer type. Please use lossy or lossless.")
        }
    }
}