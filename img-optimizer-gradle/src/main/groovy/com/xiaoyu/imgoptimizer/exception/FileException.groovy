package com.xiaoyu.imgoptimizer.exception
import com.xiaoyu.imgoptimizer.exception.BaseException

class FileException extends BaseException{

    FileException(String exceptionCode, String exceptionMsg) {
        super(exceptionCode, exceptionMsg)
    }
}