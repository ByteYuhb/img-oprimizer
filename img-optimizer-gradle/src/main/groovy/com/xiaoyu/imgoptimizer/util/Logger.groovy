package com.xiaoyu.imgoptimizer.util

import com.xiaoyu.imgoptimizer.exception.BaseException
import com.xiaoyu.imgoptimizer.exception.FileException
import org.gradle.api.Project

import java.text.SimpleDateFormat

//日志工具
class Logger {
    private static final String LOG_FILE_NAME = "img_optimizer.log"
    private static final String INFO ="info:"
    private static final String WARN ="warn:"
    private static final String ERROR ="error:"

    private File file
    private PrintWriter writer

    Logger(Project project){
        file = new File(project.projectDir.absolutePath+File.separator+LOG_FILE_NAME)
        new PrintWriter(file).close()
    }
    def i(String msg) {
        write(INFO, msg)
    }

    def w(String msg) {
        write(WARN, msg)
    }

    def e(String msg) {
        write(ERROR, msg)
    }
    private def getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(new Date())
    }
    private def write(String logLevel, String msg) {
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file,true),"utf-8")),true);
        try{
            writer.write(getDateTime()+" "+logLevel)
            writer.write(msg+"\r\n")
            writer.write("----------------------------------------\r\n")
        }catch(Exception e){
            throw new FileException("文件写入错误:"+e.getMessage(), BaseException.Code.FileException)
        }finally{
            writer.close()
        }

    }
}