package com.xiaoyu.imgoptimizer.optimize.imple

import com.xiaoyu.imgoptimizer.optimize.PicOptimizer
import com.xiaoyu.imgoptimizer.util.Logger
import org.gradle.api.Project
import com.xiaoyu.imgoptimizer.util.PngquantUtil

//极限压缩
class PngquantOptimizerImpl implements PicOptimizer{

    Logger logger
    PngquantOptimizerImpl(Logger logger){
        this.logger = logger
    }
    @Override
    void optimize(Project project, String suffix, List<File> files) {
        PngquantUtil.copyPngquant2BuildFolder(project)
        if (suffix == null || suffix.trim().isEmpty()) {
            suffix = ".png"
        } else if (!suffix.endsWith(".png")) {
            suffix += ".png"
        }

        int succeed = 0
        int skipped = 0
        int failed = 0
        long totalSaved = 0L
        def pngquant = PngquantUtil.getPngquantFilePath(project)
        files.each { file ->
            long originalSize = file.length()

            Process process = new ProcessBuilder(pngquant, "-v", "--force", "--skip-if-larger",
                    "--speed=1", "--ext=${suffix}", file.absolutePath).redirectErrorStream(true).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))
            StringBuilder error = new StringBuilder()
            String line
            while (null != (line = br.readLine())) {
                error.append(line)
            }
            int exitCode = process.waitFor()

            if (exitCode == 0) {
                succeed++
                String output = file.absolutePath.substring(0, file.absolutePath.lastIndexOf(".")).concat(suffix)
                long optimizedSize = new File(output).length()
                float rate = 1.0f * (originalSize - optimizedSize) / originalSize * 100
                totalSaved += (originalSize - optimizedSize)
                logger.i("Succeed! ${originalSize}B-->${optimizedSize}B, ${rate}% saved! ${file.absolutePath}")
            } else if (exitCode == 98) {
                skipped++
                logger.w("Skipped! ${file.absolutePath}")
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