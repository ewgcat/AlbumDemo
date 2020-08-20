# 拍照或选择图片裁剪为正方形

### 依赖方式一：

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral(
                url: "https://ewgcat.bintray.com/utils/"
        )
    }
}

api 'com.lsh.album:album:1.0.0'

### 依赖方式二：直接依赖组件
    api project(path: ':album')
