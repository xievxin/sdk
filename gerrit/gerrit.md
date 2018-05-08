*
Title: Gerrit使用

Description: 

Author: xz

Date: 20151104

*

# Gerrit使用

# 目录

[Gerrit简介](#a1)

[ssh配置](#a2)

[clone代码](#a3)

[绑定别名](#a4)


<div id="a1"></div>

## Gerrit简介
Gerrit，一种免费、开放源代码的代码审查软件，使用网页界面。利用网页浏览器，同一个团队的软件程序员，可以相互审阅彼此修改后的程序代码，决定是否能够提交，退回或者继续修改。它使用Git作为底层版本控制系统。它分支自Rietveld，作者为Google公司的Shawn Pearce，原先是为了管理Android计划而产生。这个软件的名称，来自于荷兰设计师赫里特·里特费尔德（Gerrit Rietveld）。最早它是由Python写成，在第二版后，改成用Java与SQL。使用Google Web Toolkit来产生前端的JavaScript。[1] 

<div id="a2"></div>

# ssh配置 

### step1:确认ssh keys 是否已经存在

```java
total 64
drwx------   9 xz  staff   306  6 10 11:45 .
drwx------+ 95 xz  staff  3230 11  4 12:10 ..
-rw-r--r--@  1 xz  staff  6148  6 10 11:45 .DS_Store
-rw-------   1 xz  staff  1766 12 21  2013 github_rsa
-rw-r--r--   1 xz  staff   404 12 21  2013 github_rsa.pub
-rw-------   1 xz  staff  1675  8 29  2014 id_rsa
-rw-r--r--   1 xz  staff   407  8 29  2014 id_rsa.pub
drwxr-xr-x   4 xz  staff   136  8 29  2014 key_backup
-rw-r--r--   1 xz  staff  6586 11  4 11:45 known_hosts

```

### step2:生成一个新的ssh key

运行ssh-keygen

```java
	$ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
	# Creates a new ssh key, using the provided email as a label
	Generating public/private rsa key pair.
```

默认文件名

```java
Enter file in which to save the key (/Users/you/.ssh/id_rsa): [Press enter]
```

接着又会提示你输入两次密码（该密码是你push文件的时候要输入的密码），
当然，你也可以不输入密码，直接按回车。

```java
Enter passphrase (empty for no passphrase): 
# Enter same passphrase again:
```
接下来，就会显示如下代码提示，如：

```java
Your identification has been saved in /c/Users/you/.ssh/id_rsa.
# Your public key has been saved in /c/Users/you/.ssh/id_rsa.pub.
# The key fingerprint is:
# 01:0f:f4:3b:ca:85:d6:17:a1:7d:f0:68:9d:f0:a2:db your_email@example.com
```
当你看到上面这段代码的收，那就说明，你的 SSH key 已经创建成功;



### step3:添加ssh key到ssh-agent

确认ssh-agent 可用

```java
	xz@xzdeMacBook-Pro:~$ eval "$(ssh-agent -s)"
	Agent pid 35642
```

增加ssh key到ssh-agent
运行

```java
	xz@xzdeMacBook-Pro:~$ssh-add ~/.ssh/id_rsa
```

### step4:修改 gerrit  profile Username

	1.登录gerrit 地址：<http://192.168.14.217:8281/>
	2.点击右上角用户名选择settings
	3.点击profile
	4.添加username
	
![profile](profile.png)
	

### step5:增加ssh key到gerrit

	1.登录gerrit 地址：<http://192.168.14.217:8281/>
	2.点击右上角用户名选择settings
	3.点击ssh public keys
	4.add key；获取.ssh/id_rsa.pub 内容拷贝到这里
	
![ssh](ssh.png)

### step5:验证

```java
  $ssh -T -p 29418 lvgx@192.168.14.217

  ****    Welcome to Gerrit Code Review    ****

  Hi lvgx, you have successfully connected over SSH.

  Unfortunately, interactive shells are disabled.
  To clone a hosted Git repository, use:

  git clone ssh://lvgx@192.168.14.217:29418/REPOSITORY_NAME.git
```


<div id="a3"></div>

# clone

选择clone with commit-msg hook

``` java
git clone ssh://lvgx@192.168.14.217:29418/matchbox-app-android && scp -p -P 29418 lvgx@192.168.14.217:hooks/commit-msg matchbox-app-android/.git/hooks/ 

```


<div id="a4"></div>

# 绑定别名命令

```java
git config --global alias.review "push origin HEAD:refs/for/master"
```