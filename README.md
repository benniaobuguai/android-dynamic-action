## Android Dynamic Action(动态Action)
Android Dynamic Action，简称DA，是一种简便、可变Action的实现方案。DA框架的初衷是为了取代Context.startActivity的调用方式，使用建造者模式(Builder Pattern)构建交互参数，使程序更优美。DA框架能够对任何一个已经存在的Action修改，动态改变原有的跳转逻辑。值得一提的是，DA框架不仅友好地实现了与H5间的跳转交互，也解决了Activity在插件化项目的交互问题。


### DA的URI基本结构
在DA框架下，Activity是一个有趣的概念实体，每一个Activity都可视作DA框架下的一种资源。对于一个客户端而言，每个Activity都是全局唯一可访问的资源，因此每个Activity都有统一资源标识符(URI)。

URI的基本结构：
``` html
scheme://com.example.project:8888/path/etc?id=1024
\-----/  \------------------/\--/ \------/\-------/
scheme            host       port   path     query parameter
         \---------------------/
                authority
```

DA框架基于标准的URI，定制了更符合Android Activity交互的URI结构。
定制后的URI基本结构：
``` html
scheme://packageId$ActionName?data={"id":"1024"}
\-----/  \------------------/\-----------------/
scheme            host         query parameter
```

基于以上协议，定义属于自己的scheme，每个Activity将具有一个可被访问的URI，你就能够像访问网页一样访问Activity啦！！！


### DA的集成只需三步
+ 添加依赖
``` java
dependencies {
    compile 'com.opencdk:dynamicaction:1.0.0'
}
```
+ 将demo中的配置文件assets/dynamic_action.cfg拷贝至你的项目assets目录下
+ 修改配置文件dynamic_action.cfg的scheme、包名映射关系


### DA的配置文件
DA框架的“动态可变性”体现在配置文件上，DA框架遵循“约定优于配置”的原则，使用更少的配置达到目的。配置文件非常简单，仅包含scheme以及包名的映射关系。
配置文件示例：
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<DynamicAction xmlns:opencdk="http://www.opencdk.com/dynamicaction"
    opencdk:version="1.0.0" >

    <constant name="DA.devMode" value="true" />
    <constant name="DA.scheme" value="opencdk" />
    <constant name="DA.appscheme" value="opencdkexample" />

    <package id="0" name="com.opencdk.da.ui" >
    </package>

    <package id="1" name="com.opencdk.da.ui.user" >
    </package>

    <package id="2" name="com.opencdk.da.ui.video" >
        <!-- 用H5登录界面来修复有BUG的原生登录 -->
        <action name="Login" from="home_login_click"  to="0$Browser?title=H5登录&amp;url=http://www.opencdk.com/login.html" />
    </package>

</DynamicAction>
```

参数说明：
- DA.devMode：开发模式，开关打开后，后台将可看到更多日志
- DA.scheme：DA框架内部使用的scheme，建议根据自己的业务定义唯一的scheme
- DA.appscheme：非DA框架直接使用，是App提供给第三方App的scheme
- package：包名的映射关系，文章最后有完整配置
- package>id：包id，与包名一一对应
- package>name：包名
- action：动态修复的Action
- action>name：Action的名称
- action>from：事件源，触发此事件的源头
- action>to：目标地址(DA框架标准的URI数据结构)
- action>to>title：Activity标题
- action>to>url：网址


### DA的代码实现
假设在com.opencdk.da.ui包下有LoginActivity，访问的scheme可表示为：
``` html
opencdk://1$Login
```

使用DA框架后，启动LoginActivity则变得非常容易：
``` java
new DA.Builder(Context)
	.setHost("1$Login")
	.go();
```

或：

``` java
new DA.Builder(Context)
	.setUriString("opencdk://1$Login")
	.go();
```

或：

``` java
new DA.Builder(Context)
	.setPackageId("1")
	.setActionName("Login")
	.go();
```

非常简单的实现方式，为动态运营奠定了基础。基于DA框架，开发同学可以轻松构建项目规范，编写更优雅的代码；测试同学编写测试用例也变得更容易了。


### DA的数据交互
DA框架不但支持原生Activity间的数据交互，而且也支持Activity与H5间的数据交互。保证数据协议的一致性，DA框架统一使用JSON进行数据交互（推荐使用fastjson）。
URI表示如下：
``` html
opencdk://1$Login?data={"username":"benniaobuguai"}
```

代码调用：
``` java
new DA.Builder(Context)
	.setHost("1$Login")
	.setData("{\"username\":\"benniaobuguai\"}")
	.go();
```


### DA支持Activity回调(Context.startActivityForResult)
URI表示如下：
``` html
opencdk://1$Login?data={"username":"benniaobuguai"}&requestCode=10000
```

代码调用：
``` java
new DA.Builder(Context)
	.setHost("1$Login")
	.setData("{\"username\":\"benniaobuguai\"}")
  .setRequestCode(10000)
	.go();
```

**PS：** 建议在Activity间交互时使用事件总线，如：[EventBus](https://github.com/greenrobot/EventBus)，[otto](https://github.com/square/otto)等。


### DA动态修改默认跳转
- 用一个Activity替换另外一个Activity
``` xml
<package id="2" name="com.opencdk.da.ui.video" >
    <!--- 随机推荐视频列表界面修改成免费视频列表界面 -->
    <action name="VideoRandomList" from="home_video_random_click" to="2$VideoFreeList" />
</package>
```

- 用一个H5界面替换一个Activity
``` xml
<package id="2" name="com.opencdk.da.ui.video" >
    <!-- 随机推荐视频列表界面修改成H5的免费视频推荐界面 -->
    <action name="VideoRandomList" from="home_video_random_click" to="0$Browser?title=精彩免费视频推荐&amp;url=http%3a%2f%2fwww.iqiyi.com%2fdianying%2ffree.html" />
</package>
```

注意：
1. xml文件不支持直接使用&、<、>等特殊字符，应该使用其对应的转义字符。具体可参考：[XML和HTML常用转义字符](http://www.opencdk.com/xml%E5%92%8Chtml%E5%B8%B8%E7%94%A8%E8%BD%AC%E4%B9%89%E5%AD%97%E7%AC%A6/)
2. url地址需要进行URL编码，避免特殊字符对URI的解析造成影响。[在线URL编码](http://tool.chinaz.com/tools/urlencode.aspx)


### DA启用拦截器功能
往往有些时候，我们需要对某些界面进行访问控制。

已经发布的版本，任何用户都可访问VIP视频列表。临时需求变更，只有登录的用户才能访问VIP视频列表界面，可修改配置下发：
``` xml
<interceptors>
    <interceptor
        name="LoginInterceptor"
        class="com.opencdk.da.interceptor.LoginInterceptor" >
    </interceptor>
    <interceptor
        name="TestInterceptor"
        class="com.opencdk.da.interceptor.TestInterceptor" >
    </interceptor>

    <actionInterceptor>
        <accept name="2$VideoVIPList" >
            <interceptor-ref>LoginInterceptor</interceptor-ref>
        </accept>
    </actionInterceptor>

</interceptors>
```


### H5与原生程序的交互
引入DA框架后，在H5界面也可前往任意的原生界面，代码也非常简单。
H5代码片断：
``` html
<p><a href="opencdk://1$Login">Login</a></p>
<p><a href="opencdk://2$VideoPlay">Play Video</a></p>
<p><a href="opencdk://0$Browser?url=http://www.opencdk.com">http://www.opencdk.com</a></p>
```

重写WebView WebViewClient的方法shouldOverrideUrlLoading(WebView view, String url)，支持自定义的scheme。

``` java
mWebView.setWebViewClient(new WebViewClient() {

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    if (url != null && url.startsWith(DALoader.getScheme())) {
      new DA.Builder(mContext)
        .setUriString(url)
        .go();

      return true;
    }

    return super.shouldOverrideUrlLoading(view, url);
  }
});
```


### DA框架解决的核心问题
- 一个地址可达任意Activity
- 任意一个Activity可被动态修改为另一个Activity
- DA框架数据交互扁平化，使用JSON便于与H5交互
- 任意Activity可替换成H5，快速修复原生突发BUG
- 插件化项目中也能满足以上需求


### DA框架所遵循的一些约定
DA框架默认遵循以下规则：

- 配置优先级>Java代码优先级，保证通过配置可修复代码编码的缺陷。
- 查找动态配置时，先根据name+from进行精确查找，其次根据name去查找。


### 示例说明
为了更好地体现动态Action，示例中在assets目录下放了多个配置文件，加载不同的配置文件就相当于网络下发了新的配置文件。

- dynamic_action.cfg，默认的配置文件，仅包含包结构映射关系
- dynamic_action_transfer.cfg，配置一个动态Action
- dynamic_action_interceptor.cfg，配置一个拦截器


### 进阶与思考
- 客户端与客户端交互
  客户端与客户端之间的交互是不安全的，对于暴露给第三方的入口都需要进行校验。就DA框架而言，主要是为了解决内部跳转的统一协议而确定的scheme(opencdk://)，提供外部使用的scheme(opencdkexample://)。opencdk://仅供内部使用，认为是可信任的、安全的。opencdkexample://是外部协议，必须经过校验方可拉起我们的客户端。

需要提供外部入口，必须要在AndroidManifest.xml里面定义，
``` xml
<activity
    android:name="com.opencdk.da.ui.SplashActivity"
    android:exported="true"
    android:label="@string/app_name"
    android:theme="@style/DA.Theme.NoTitleBar" >
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <!-- 定义给外部调用 的scheme -->
        <data android:scheme="opencdkexample" />
    </intent-filter>
</activity>
```


新建一个项目，执行外部调用代码片断：
``` java
Intent intent = new Intent();
intent.setAction(Intent.ACTION_VIEW);
intent.addCategory(Intent.CATEGORY_DEFAULT);
Uri data = Uri.parse("opencdkexample://0$Browser?url=http://v.qq.com/cover/r/rm3tmmat4li8uul/w0019k37ecc.html");
intent.setData(data);
startActivity(intent);
```

- iOS对本协议的支持
  iOS并无包名概念，如果希望使用同一个URI来跳转至同一个界面，iOS在对URI的处理时，应当直接过滤包名后再使用。
  登录配置如下：
``` html
opencdk://1$Login?data={"username":"benniaobuguai"}
```
解析【host】协议时，直接取【ActionName】，忽略前面的 "1$" 即可。

- 对于简单的项目，Android把所有Activity放在一个包名下(不建议这么做)，也可与iOS保持URI同一处理逻辑。
  URI表示如下：
``` html
opencdk://Login?data={"username":"benniaobuguai"}
```
**PS：** DA框架当前不支持无包名的实现方式。


### 注意点
- 避免循环对Action进行中转
- 通过反射访问目标Activity，ActionName的大小写敏感
- 避免过多的包名映射，Activity所在的包不宜过多，包越多维护成本越大。
- Activity不能被混淆


### 其他
- 运营的灵活性，增强运营配置的自由度
- 界面可替换性，任意Activity可替换成H5，提供快速使用H5修复BUG的能力
- 插件访问简单化，宿主程序是无法直接获取插件的Activity对象，DA框架的作用尤其明显。
- DA框架最大的问题是全局可访问任意Activity，如何保证被访问者的安全，业务不受到影响就显示尤为重要了
- DA框架与传统的Context.startActivity最大的区别在于：交互协议标准化、灵活、运营能力强，动态修复能力强


### Q&A：
1.为什么需要制定两个scheme?

答：隔离

2.配置from to过多时, 性能如何?

答：配置from和to时，属于修复BUG的行为，原则上并不会太多。通过HashMap查找，速度很快。

3.配置文件过大时, 性能如何?

答：未验证。


### 完整的配置文件
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<DynamicAction xmlns:opencdk="http://www.opencdk.com/dynamicaction"
    opencdk:version="1.0.0" >

    <constant name="DA.devMode" value="true" />
    <constant name="DA.scheme" value="opencdk" />
    <constant name="DA.appscheme" value="opencdkexample" />

    <package id="0" name="com.opencdk.da.ui" >
    </package>

    <package id="1" name="com.opencdk.da.ui.user" >
    </package>

    <package id="2" name="com.opencdk.da.ui.video" >
    	<action name="VideoRandomList" from="home_video_random_click" to="2$VideoFreeList" />
    </package>

    <interceptors>
        <interceptor
            name="LoginInterceptor"
            class="com.opencdk.da.interceptor.LoginInterceptor" >
        </interceptor>
        <interceptor
            name="TestInterceptor"
            class="com.opencdk.da.interceptor.TestInterceptor" >
        </interceptor>

        <actionInterceptor>
            <accept name="2$VideoVIPList" >
                <interceptor-ref>LoginInterceptor</interceptor-ref>
            </accept>
        </actionInterceptor>

    </interceptors>

</DynamicAction>
```

博客原文：[http://www.opencdk.com/android-dynamic-action%E5%8A%A8%E6%80%81action/](http://www.opencdk.com/android-dynamic-action%E5%8A%A8%E6%80%81action/)