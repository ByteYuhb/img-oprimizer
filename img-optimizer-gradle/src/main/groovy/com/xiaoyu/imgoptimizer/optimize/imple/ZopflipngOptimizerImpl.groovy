package com.xiaoyu.imgoptimizer.optimize.imple

import com.xiaoyu.imgoptimizer.optimize.PicOptimizer
import com.xiaoyu.imgoptimizer.util.Logger
import org.gradle.api.Project
import com.xiaoyu.imgoptimizer.util.ZopflipngUtil


//无损压缩
class ZopflipngOptimizerImpl implements PicOptimizer{
    Logger logger
    ZopflipngOptimizerImpl(Logger logger){
        this.logger = logger
    }
    @Override
    void optimize(Project project, String suffix, List<File> files) {
        ZopflipngUtil.copyZopflipng2BuildFolder(project)
        if (suffix == null || suffix.trim().isEmpty()) {
            suffix = ".png"
        } else if (!suffix.endsWith(".png")) {
            suffix += ".png"
        }

        int succeed = 0
        int skipped = 0
        int failed = 0
        long totalSaved = 0L
        def zopflipng = ZopflipngUtil.getZopflipngFilePath(project)
        files.each { file ->
            long originalSize = file.length()

            String output = file.absolutePath.substring(0, file.absolutePath.lastIndexOf(".")).concat(suffix)
            Process process = new ProcessBuilder(zopflipng, "-y", "-m", file.absolutePath, output).
                    redirectErrorStream(true).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))
            StringBuilder error = new StringBuilder()
            String line
            while (null != (line = br.readLine())) {
                error.append(line)
            }
            int exitCode = process.waitFor()

            if (exitCode == 0) {
                succeed++
                long optimizedSize = new File(output).length()
                float rate = 1.0f * (originalSize - optimizedSize) / originalSize * 100
                totalSaved += (originalSize - optimizedSize)
                logger.i("Succeed! ${originalSize}B-->${optimizedSize}B, ${rate}% saved! ${file.absolutePath}")
            } else {
                failed++
                logger.e("Failed! ${file.absolutePath}")
                logger.e("Exit: ${exitCode}. " + error.toString())
            }
        }

        logger.i("Total: ${files.size()}, Succeed: ${succeed}, " +
                "Skipped: ${skipped}, Failed: ${failed}, Saved: ${totalSaved / 1024}KB")
    }
}