# appium

## 介绍

Appium 是一个自动化测试开源工具，支持 iOS 平台和 Android 平台上的原生应用，web 应用和混合应用。

所谓的“移动原生应用”是指那些用 iOS 或者 Android SDK 写的应用。所谓的“移动 web 应用”是指使用移动浏览器访问的应用（Appium 支持 iOS 上的 Safari 和 Android 上的 Chrome）。所谓的“混合应用”是指原生代码封装网页视图——原生代码和 web 内容交互。比如，像 Phonegap，可以帮助开发者使用网页技术开发应用，然后用原生代码封装，这些就是混合应用。

重要的是，Appium 是一个跨平台的工具：它允许测试人员在不同的平台（iOS，Android）使用同一套API来写自动化测试脚本，这样大大增加了 iOS 和 Android 测试套件间代码的复用性。

语言/框架 | Github版本库以及安装指南 |
----- | ----- |
Ruby | [https://github.com/appium/ruby_lib](https://github.com/appium/ruby_lib)
Python | [https://github.com/appium/python-client](https://github.com/appium/python-client)
Java | [https://github.com/appium/java-client](https://github.com/appium/java-client)
JavaScript (Node.js) | [https://github.com/admc/wd](https://github.com/admc/wd)
Objective C | [https://github.com/appium/selenium-objective-c](https://github.com/appium/selenium-objective-c)
PHP | [https://github.com/appium/php-client](https://github.com/appium/php-client)
C# (.NET) | [https://github.com/appium/appium-dotnet-driver](https://github.com/appium/appium-dotnet-driver)
RobotFramework | [https://github.com/jollychang/robotframework-appiumlibrary](https://github.com/jollychang/robotframework-appiumlibrary)

### 设计

Appium 真正的工作引擎其实是第三方自动化框架。

iOS: 苹果的 UIAutomation

Android 4.2+: Google’s UiAutomator

Android 2.3+: Google’s Instrumentation. (Instrumentation由单独的项目Selendroid提供支持 )

我们把这些第三方框架封装成一套 API，WebDriver API.WebDriver（也就是 “Selenium WebDriver"）

## Getting Started

### appium for windows

##### 1.安装[nodejs](https://nodejs.org/download/)  地址：<https://nodejs.org/download/>(测试安装成功 node -v)

[Windows，NodeJS 环境配置](<http://willerce.com/post/windows-install-nodejs-and-npm/>)
    
    xz@xzdeMacBook-Pro:~$ node -v
	v0.12.3

##### 2.android sdk 
安装Level17(4.2.x)或以上的版本 api添加环境变量$ANDROID_HOME 设置为android sdk路径

[在Windows下搭建Android开发环境](<http://jingyan.baidu.com/article/bea41d437a41b6b4c51be6c1.html>)

##### 3.安装[ant](http://ant.apache.org/bindownload.cgi) 地址：<http://ant.apache.org/bindownload.cgi>

[Windows下 ANT的安装和配置](<http://blog.csdn.net/chow__zh/article/details/8362835>)

```
	设置环境变量 ANT_HOME
    xz@xzdeMacBook-Pro:~$ ant
	Buildfile: build.xml does not exist!
	Build failed
```

##### 4.安装Apache Maven [maven](http://maven.apache.org/download.cgi) 地址：<http://maven.apache.org/download.cgi>
[在Windows上安装Maven](<http://jingyan.baidu.com/article/1709ad808ad49f4634c4f00d.html>)

```
	设置环境变量 M2HOME和M2环境变量
 	xz@xzdeMacBook-Pro:~$ mvn -v
	Apache Maven 3.3.3 (7994120775791599e205a5524ec3e0dfe41d4a06; 	2015-04-22T19:57:37+08:00)
	Maven home: /Users/xz/Documents/android/apache-maven-3.3.3
	Java version: 1.8.0, vendor: Oracle Corporation
	Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/jre
	Default locale: zh_CN, platform encoding: UTF-8
	OS name: "mac os x", version: "10.10.3", arch: "x86_64", family: "mac"
```

##### 5.python  

建议下载python 2.7.*版本

[windows系统下Python环境的搭建](<http://www.cnblogs.com/windinsky/archive/2012/09/20/2695520.html>)

##### 6.Git

[git for windows 安装方法 图文详解](<http://blog.csdn.net/wfdtxz/article/details/7908832>)

##### 7.Appium Python client

[PyPi](https://pypi.python.org/pypi), 
    shell
   	pip install Appium-Python-Client
    
本地安装 下载 ['Appium-Python-Client'](https://pypi.python.org/pypi/Appium-Python-Client),(Appium-Python-Client-X.X.tar.gz).
解压后进入解压文件夹目录，执行：“python setup.py install” 命令

    shell
    tar -xvf Appium-Python-Client-X.X.tar.gz
    cd Appium-Python-Client-X.X
    python setup.py install
    

3. Install from source via [GitHub](https://github.com/appium/python-client).

		shell
    	git clone git@github.com:appium/python-client.git
    	cd python-client
    	python setup.py install
    

#####8.Appium客户端安装
[客户端下载](<http://pan.baidu.com/s/1jGvAISu>)


    
##元素定位与交互

***android***

使用android sdk自带的 uiautomatorviewer 工具来获得元素的位置

![icon](viewer_android.png)

***ios***

使用APPIUM INSPECTOR来定位元素

![icon](viewer_ios.png)



##### 安装Python的SSH模块【Windows】

Python中使用SSH需要用到OpenSSH，而OpenSSH依赖于paramiko模块，而paramiko模块又依赖于pycrypto模块，因此要在Python中使用SSH，则需要先安装模块顺序是：

pycrypto -> paramiko

一、pycrypto模块安装

<http://pypi.python.org/pypi/pycrypto/2.5> 下载，安装时候说缺少，vcvarsall.bat

	D:\Python27\soft\pycrypto-2.5>python setup.py install
	running install
	running build
	running build_py
	running build_ext
	warning: GMP or MPIR library not found; Not building Crypto.PublicKey._fastmath.
	
找个编译好的pycrypto版本 

[pycrypto]<http://www.voidspace.org.uk/python/modules.shtml#pycrypto>
 
下载对应的版本 直接安装即可

二、paramiko模块安装
[paramiko]<http://pypi.python.org/pypi/paramiko/1.7.7.1> 下载后直接安装即可


[客户端下载](<http://pan.baidu.com/s/1jGvAISu>) 
环境变量配置 Appium\node_modules\.bin

运行 appium-doctor

	D:\code\matchbox-app-android\build>appium-doctor
	Running Android Checks
		✔ ANDROID_HOME is set to "D:\android\sdk"
		✔ JAVA_HOME is set to "C:\Program Files\Java\jdk1.8.0_40."
		✔ ADB exists at D:\android\sdk\platform-tools\adb.exe
		✔ Android exists at D:\android\sdk\tools\android.bat
		✔ Emulator exists at D:\android\sdk\tools\emulator.exe
		✔ Android Checks were successful.

		✔ All Checks were successful

[Github](https://github.com/appium)

[Appium官方文档](<https://github.com/appium/appium/tree/master/docs/cn>)

[python 库地址](<https://github.com/appium/python-client>)

[pycharm3.4.1 for windows](<http://www.newasp.net/soft/67094.html>)

[pycharm4.0.1 for mac](<http://soft.macx.cn/4546.htm>)