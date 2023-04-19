package com.xiaoyu.imgoptimizer.exception

class BaseException extends Exception {
    String exceptionCode
    String exceptionMsg
    BaseException( String exceptionCode,String exceptionMsg){
        this.exceptionCode = exceptionCode
        this.exceptionMsg = exceptionMsg
    }

    interface Code{
        public static final int Base = 1000;
        public static final int FileException = Base+1;
        public static final int OtherException = Base+2;

    }

}