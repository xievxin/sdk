*
Title: CocoaPods使用

Description: CocoaPods使用，和仓库建立

Author: xz

Date: 20150908

*

# CocoaPods使用

# 目录
[CocoaPods简介](#a1)

[CocoaPods 的安装和使用介绍](#a2)

[建立私有仓库](#a3)


<div id="a1"></div>

## CocoaPods简介
每种语言发展到一个阶段，就会出现相应的依赖管理工具，例如 Java 语言的 Maven，nodejs 的 npm。随着 iOS 开发者的增多，业界也出现了为 iOS 程序提供依赖管理的工具，它的名字叫做：CocoaPods。

CocoaPods项目的源码 在 Github 上管理。该项目开始于 2011 年 8 月 12 日，经过多年发展，现在已经成为 iOS 开发事实上的依赖管理标准工具。开发 iOS 项目不可避免地要使用第三方开源库，CocoaPods 的出现使得我们可以节省设置和更新第三方开源库的时间。

我在开发猿题库客户端时，使用了 24 个第三方开源库。在没有使用 CocoaPods 以前，我需要:

把这些第三方开源库的源代码文件复制到项目中，或者设置成 git 的 submodule。
对于这些开源库通常需要依赖系统的一些 framework，我需要手工地将这些 framework 分别增加到项目依赖中，比如通常情况下，一个网络库就需要增加以下 framework: CFNetwork, SystemConfiguration, MobileCoreServices, CoreGraphics, zlib。
对于某些开源库，我还需要设置-licucore或者 -fno-objc-arc等编译参数
管理这些依赖包的更新。
这些体力活虽然简单，但毫无技术含量并且浪费时间。在使用 CocoaPods 之后，我只需要将用到的第三方开源库放到一个名为 Podfile 的文件中，然后执行pod install。 CocoaPods 就会自动将这些第三方开源库的源码下载下来，并且为我的工程设置好相应的系统依赖和编译参数。

<div id="a2"></div>

# CocoaPods 的安装和使用介绍

### 安装

安装方式异常简单 , Mac 下都自带 ruby，使用 ruby 的 gem 命令即可下载安装：

```java
$ sudo gem install cocoapods
$ pod setup
```
如果你的 gem 太老，可能也会有问题，可以尝试用如下命令升级 gem:

```java
sudo gem update --system
```
另外，ruby 的软件源 https://rubygems.org 因为使用的是亚马逊的云服务，所以被墙了，需要更新一下 ruby 的源，使用如下代码将官方的 ruby 源替换成国内淘宝的源：

```java
gem sources --remove https://rubygems.org/
gem sources -a http://ruby.taobao.org/
gem sources -l
```
还有一点需要注意，pod setup在执行时，会输出Setting up CocoaPods master repo，但是会等待比较久的时间。这步其实是 Cocoapods 在将它的信息下载到 ~/.cocoapods目录下，如果你等太久，可以试着 cd 到那个目录，用du -sh *来查看下载进度。你也可以参考本文接下来的使用 cocoapods 的镜像索引一节的内容来提高下载速度。

### 使用 CocoaPods 的镜像索引

所有的项目的 Podspec 文件都托管在https://github.com/CocoaPods/Specs。第一次执行pod setup时，CocoaPods 会将这些podspec索引文件更新到本地的 ~/.cocoapods/目录下，这个索引文件比较大，有 80M 左右。所以第一次更新时非常慢，笔者就更新了将近 1 个小时才完成。

一个叫 akinliu 的朋友在 gitcafe 和 oschina 上建立了 CocoaPods 索引库的镜像，因为 gitcafe 和 oschina 都是国内的服务器，所以在执行索引更新操作时，会快很多。如下操作可以将 CocoaPods 设置成使用 gitcafe 镜像：

```java
pod repo remove master
pod repo add master https://gitcafe.com/akuandev/Specs.git
pod repo update
```
将以上代码中的 https://gitcafe.com/akuandev/Specs.git 替换成 http://git.oschina.net/akuandev/Specs.git 即可使用 oschina 上的镜像。

### 使用 CocoaPods

使用时需要新建一个名为 Podfile 的文件，以如下格式，将依赖的库名字依次列在文件中即可

```java
platform :ios
pod 'JSONKit',       '~> 1.4'
pod 'Reachability',  '~> 3.0.0'
pod 'ASIHTTPRequest'
pod 'RegexKitLite'
```
然后你将编辑好的 Podfile 文件放到你的项目根目录中，执行如下命令即可：

```java
cd "your project home"
pod install
```
现在，你的所有第三方库都已经下载完成并且设置好了编译参数和依赖，你只需要记住如下 2 点即可：

使用 CocoaPods 生成的 .xcworkspace 文件来打开工程，而不是以前的 .xcodeproj 文件。
每次更改了 Podfile 文件，你需要重新执行一次pod update命令。
查找第三方库

你如果不知道 cocoaPods 管理的库中，是否有你想要的库，那么你可以通过 pod search 命令进行查找，以下是我用 pod search json 查找到的所有可用的库：

```java
$ pod search json

-> AnyJSON (0.0.1)
   Encode / Decode JSON by any means possible.
   - Homepage: https://github.com/mattt/AnyJSON
   - Source:   https://github.com/mattt/AnyJSON.git
   - Versions: 0.0.1 [master repo]


-> JSONKit (1.5pre)
   A Very High Performance Objective-C JSON Library.
   - Homepage: https://github.com/johnezang/JSONKit
   - Source:   git://github.com/johnezang/JSONKit.git
   - Versions: 1.5pre, 1.4 [master repo]

// ... 以下省略若干行

```
关于 Podfile.lock

当你执行pod install之后，除了 Podfile 外，CocoaPods 还会生成一个名为Podfile.lock的文件，Podfile.lock 应该加入到版本控制里面，不应该把这个文件加入到.gitignore中。因为Podfile.lock会锁定当前各依赖库的版本，之后如果多次执行pod install 不会更改版本，要pod update才会改Podfile.lock了。这样多人协作的时候，可以防止第三方库升级时造成大家各自的第三方库版本不一致。

CocoaPods 的这篇 官方文档 也在What is a Podfile.lock一节中介绍了Podfile.lock的作用，并且指出：

```java
This file should always be kept under version control.
```

### 不更新 podspec
```java
pod install --no-repo-update
pod update --no-repo-update
```

<div id="a3"></div>

## 建立私有仓库

### 1.创建代码仓库

**将自己写的代码推送到git服务器。如果代码可以开源的话，可以用github来托管**
	
[参考](https://github.com/GetuiLaboratory/getui-sdk-ios-cocoapods.git)

### 2.给稳定的代码打上版本tag，一般以版本号作为tag名

```java
	git tag -a ‘1.2.1’ -m ‘version 1.2.1’ 
```
将tag推送到git服务器

```java
	git push --tags
```

### 3.创建spec文件
```java
pod spec create https://github.com/GetuiLaboratory/getui-sdk-ios-cocoapods.git
```

会在当前目录创建.podspec文件，创建的文件是个完整的配置模板，根据字面意思以及注释，大体上都能弄明白。不清楚的地方可以[参考](https://guides.cocoapods.org/making/specs-and-specs-repo.html)，github上也有很多开源代码可以参考。

### 4.验证spec文件有效性
```java
pod spec lint .podspec
```

### 5.创建spec repository（spec 仓库）

	├─Specs  
    	├──getui-sdk-ios-cocoapods/  
    	├── 1.2.1  
    	  		└── getui-sdk-ios-cocoapods.podspec
    	  		
    	  		
这里的版本号要和代码仓库里的tag一一对应  [参考](https://github.com/GetuiLaboratory/Specs.git)


### 6.添加私有repo到CocoaPods中

```
pod repo add GetuiLaboratory https://github.com/GetuiLaboratory/Specs.git
```	  

### 7.验证私有repo安装无误

```
$ cd ~/.cocoapods/repos/REPO_NAME  
$ pod repo lint .
```
后面如果还要往REPO_NAME里添加新包，只需运行下面命令:

```java
pod repo push REPO_NAME SPEC_NAME.podspec
```

如要删除私有repo：

```java
pod repo remove [name]
```

### 8.添加包到工程的Podfile中如下：

``` java
source 'https://github.com/GetuiLaboratory/Specs.git'
source 'https://github.com/CocoaPods/Specs.git'

platform :ios, "7.0"
pod 'Reachability'
pod 'getui-sdk-ios-cocoapods'

```

### 9.运行

到工程目录下运行

```java
pod update
```