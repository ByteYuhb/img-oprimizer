[English](README-english.md)

大家好，我是小余，平时开发过程中经常会有对图片进行压缩的需求，往往我们都是直接去网上找在线软件进行压缩，但是你要在百度上找一个压缩软件不是要收费就是要你注册关注什么的，反正就是用的很不舒服，而且不一定达到自己预期效果。于是小余写了一个gradle插件，只要集成这个插件，就可以让我们在Android Studio中一键对项目中所有的png图片进行压缩，而且可以根据你设置的参数对图片进行过滤，压缩后的图片信息还可以在日志文件中查看。

好了，下面我们进入正题：

# img-optimizer

>这是一款用于优化png图片的gradle插件，有效减少APK体积，支持极限压缩和无损压缩。

在`macOS`、`windows10`上测试通过，如果有更多需求，请提[issue](https://github.com/ByteYuhb/img-oprimizer/issues)。

### 如何使用

在Project的build.gradle文件中:  

```
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        ...
        classpath 'com.github.ByteYuhb:img-oprimizer:v1.0.2'
    }
}
```

然后在你想要优化的module的build.gradle文件中:  

`apply plugin: 'com.xiaoyu.imgoptimizer.plugin'`  

然后在task tree里面就可以看到对应的task:  
![原图](arts/image-optimizer.png)
双击即可执行。task的名字受当前module的命名影响。

### 支持的配置项
可以在module的build.gradle文件中添加配置选项，来自定义任务:  

```
optimizerOptions {
    triggerSize  6
    type  "lossless"
    suffix  ""
    fileFilters = ["mmpic.png","aaa.png"]
}
```

1. `triggerSize` 用于过滤图片，小于该值的图片不会进行优化。默认为0，即每张图片都进行优化。
2. `type` 优化类型，目前支持`"lossy"`和`"lossless"`。`"lossy"`为极限压缩(推荐，速度快，压缩率高)，`"lossless"`为无损压缩(速度慢，压缩率低，与原图无差别)。
3. `suffix` 优化后的图片后缀。假如配置为`"_opter"`，`ic_launcher.png`经过优化后，将会生成`ic_launcher_opter.png`。默认为空，即覆盖原图。
4. `fileFilters`支持过滤文件，对不需要压缩的图片添加白名单。

### 效果预览

|原图|极限压缩(lossy)|无损压缩(lossless)|
|:---:|:---:|:---:|
|872K|160K(减少81%)|476K(接近50%)|
|![原图](arts/atm.png)|![极限压缩](arts/atm_lossy.png)|![无损压缩](arts/atm_lossless.png)|

### 说明

如果项目有多个module，请在相应的build.gradle中配置plugin，每个module相互独立。执行task后会在module根目录下生成log文件，详细记录了每张图片的优化情况，方便查看。

**原理**

该插件主要是基于**[pngquant](https://pngquant.org/)**以及Google 的 [**zopfli**](https://github.com/google/zopfli) 工具进行压缩的，前者可以最大限度的将png图片压缩到最小，而 **zopfli** 是一个无损压缩格式，都是轻量级且高效的工具。更多原理部分大家可以去看[源码](https://github.com/ByteYuhb/img-oprimizer)，有不懂的可以call[小余](https://mp.weixin.qq.com/s?__biz=MzkwODI1NDEwMA==&mid=2247483986&idx=1&sn=57136c9c062caa1026edf9ed35915c2b&chksm=c0cd8ca9f7ba05bfcfadad10bd97006bbb57afdd048c9c46fe57d122af834f569aa9d8df0e48&token=2142008574&lang=zh_CN#rd)，当然也期待你的关注。

**参考**：

[img-optimizer-gradle-plugin](https://github.com/chenenyu/img-optimizer-gradle-plugin/blob/master/README-zh-rCN.md)

https://pngquant.org/

https://github.com/google/zopfli