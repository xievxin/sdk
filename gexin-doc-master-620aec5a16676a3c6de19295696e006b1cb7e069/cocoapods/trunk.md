*
Title: trunk使用

Description: trunk使用

Author: xz

Date: 20151119

*

# trunk使用


<div id="a1"></div>

## 简介
2014年5月20日，CocoaPods不再接受向CocoaPods/Specs的pull request，官方的说法是为了安全考虑，防止每个人的pod被其他人修改，于是CocoaPods团队开发了trunk服务，这样每个人都是其发布的pod的owner，没有权限的人无法修改，这样更安全。

更改后的提交架构是这样的：

![trunk](trunk.png)

以前的podspec文件都是ruby格式，而trunk为我们带来了更轻便的json格式，你后你可以用json来配置你的pod了。不用担心过去我们发布的pod，他们依然健在，并且trunk会将podspec文件转化为podspec.json文件。

[官方介绍](http://blog.cocoapods.org/CocoaPods-Trunk/#transition)


### 注册
```
 pod trunk register lvgx@getui.com 'lvgx'  --verbose
```

### 查询自己注册信息

```
xz@xzdeMacBook-Pro:~$ pod trunk me
  - Name:     lvgx
  - Email:    lvgx@getui.com
  - Since:    August 23rd, 19:37
  - Pods:
    - GTSDK
  - Sessions:
    - August 23rd, 19:37   -   December 30th, 21:48. IP: 115.236.59.162
    Description: macbook xz
    - November 19th, 07:44 - March 26th, 2016 08:29. IP: 115.195.62.113
	
```

### 配置podspec

参考[cocoapod.md](cocoapods.md)

### 提交

```
 pod trunk push GTSDK.podspec
```

### 将其他人加入到你的Pod

```
 pod trunk add-owner GTSDK xxx@getui.com
```

### 接入cocoapods

```
platform :ios, "7.0"
pod 'GTSDK'

```




 















	 

