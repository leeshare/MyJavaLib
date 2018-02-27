我的Java类库

包含各种涉及到的类的用法及相关第三方类库

高斯模糊

    org.lixl.maths.GaussUtil
    org.lixl.maths.GaussionBlur
        输入一个图片所在路径，比如  "D:\\butterfly.jpg"
        输出模糊后的图片路径，比如   "D:/test1.jpg"

爬虫爬百度图片

    JsoupFromBaiduPic.java
        依赖第三方包  jsoup-1.11.2.jar
        json-lib-2.4-jdk15.jar      解压json数据时使用，依赖于以下5个包
            commons-beanutils-1.8.0-BETA.jar
            commons-collections-3.2.1.jar
            commons-lang-2.5.jar
            commons-logging-1.1.1.jar
            ezmorph-1.0.6.jar
        commons-io-2.6.jar      用于从html元素提取属性