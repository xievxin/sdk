*
Title: Gerrit Workflow使用

Description: 

Author: xz

Date: 20151112

*

# 目录

[账户配置](#md1)

[ssh host 配置](#md2)

[安装钩子](#md3)

[CR-2 情况处理](#md4)

[Abandon 情况处理](#md5)

[reset 介绍](#md6)

<div id="md1"></div>
## 账户配置
提交gerrit的changes用户名和邮箱必须和gerrit用户信息一致

```java
	$ git config --global user.name "lvgx"
	$ git config --global user.email "lvgx@getui.com"
```


<div id="md2"></div>
## ssh host 配置

使用ssh配置
./ssh目录下添加 config 文件,添加内容

```java
Host gerritserver 
    Hostname 192.168.14.217
    User     lvgx
    Port     29418
```

可使用此方式clone project

```java
git clone ssh://gerritserver/matchbox-app-android
```

```java
xz@xzdeMacBook-Pro:$ git clone ssh://gerritserver/matchbox-app-android
Cloning into 'matchbox-app-android'...
remote: Counting objects: 16268, done
remote: Finding sources: 100% (16268/16268)
remote: Total 16268 (delta 11235), reused 16265 (delta 11235)
Receiving objects: 100% (16268/16268), 14.47 MiB | 4.74 MiB/s, done.
Resolving deltas: 100% (11235/11235), done.
Checking connectivity... done.

```


<div id="md3"></div>
## 安装钩子
Git 库的钩子脚本 hooks/commit-msg 为了保证已经提交审核的修订通过审核入库后，被别的分支 cherry-pick 后再推送至服务器时不会产生新的重复的评审任务，Gerrit 设计了一套方法，即要求每个提交包含唯一的 Change-Id，这个 Change-Id 因为出现在日志中，当执行 cherry-pick 时也会保持，Gerrit 一旦发现新的提交包含了已经处理过的 Change-Id ，就不再为该修订创建新的评审任务和 task-id，而直接将提交入库。 为了实现 Git 提交中包含唯一的 Change-Id，Gerrit 提供了一个钩子脚本，放在开发者本地 Git 库中（hooks/commit-msg）
 
```java
	scp gerritserver:hooks/commit-msg .git/hooks/
```

<div id="md4"></div>
## CR-2 情况处理

```
d794693 2015-11-12  (HEAD -> master) commit 7 [lvgx]
f1bbc51 2015-11-12  commit 6 [lvgx]
d813cd8 2015-11-12  commit 5 [lvgx]
cc00834 2015-11-12  commit 4 [lvgx]
ad1fdac 2015-11-12  commit 3 [lvgx] 
12baec9 2015-11-12  commit 2 [lvgx]
9c95373 2015-11-12  commit 1 [lvgx] (CR-2)
```
如果需要修改commit 2 提交点内容，重新提交；

1、rebase

```java
	git rebase -i HEAD~6 or git rebase -i 9c95373(目标点前一个点)

```
	pick 12baec9 commit 2
	pick ad1fdac commit 3
	pick cc00834 commit 4
	pick d813cd8 commit 5
	pick 2982c6d commit 6
	pick 264870e commit 7

	# Rebase 9c95373..264870e onto 9c95373 (6 command(s))
	# Commands:
	# p, pick = use commit
	# r, reword = use commit, but edit the commit message
	# e, edit = use commit, but stop for amending
	# s, squash = use commit, but meld into previous commit
	# f, fixup = like "squash", but discard this commit's log message
	# x, exec = run command (the rest of the line) using shell
	# These lines can be re-ordered; they are executed from top to bottom.
	# If you remove a line here THAT COMMIT WILL BE LOST.
	# However, if you remove everything, the rebase will be aborted.

	"~/Desktop/gerrit/test2/matchbox-app-android/.git/rebase-merge/git-rebase-todo" 24L, 747C

2、将commit2的pick修改为“edit”

	pick 12baec9 commit 2  为 edit 12baec9 commit 2
	
保存退出

	xz@xzdeMacBook-Pro:matchbox-app-android$ git rebase -i 9c95373
			Stopped at 12baec9f5a1f3611ba071c59548f20ab3279dbb9... commit 2
			You can amend the commit now, with

				git commit --amend 

			Once you are satisfied with your changes, run

				git rebase --continue
				
3、修改代码，执行

	git add .
	
	git commit --amend

4、git rebase --continue 

5、git review

<div id="md5"></div>
## Abandon 情况处理

commit一但出现abandon，这个changeid将不再允许commit

```
xz@xzdeMacBook-Pro:~/Desktop/gerrit/test3/matchbox-app-android$ git push riv
Counting objects: 3, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (3/3), 337 bytes | 0 bytes/s, done.
Total 3 (delta 2), reused 0 (delta 0)
remote: Resolving deltas: 100% (2/2)
remote: Processing changes: refs: 1, done    
To ssh://lvgx@192.168.14.217:29418/matchbox-app-android
 ! [remote rejected] HEAD -> refs/for/master (change http://192.168.14.217:8281/132 closed)
error: failed to push some refs to 'ssh://lvgx@192.168.14.217:29418/matchbox-app-android'
```
此时需要新建一个commit实现代码提交，执行reset操作

### 1、git reset

```
xz@xzdeMacBook-Pro:~/Desktop/gerrit/test3/matchbox-app-android$ git reset 1876441
Unstaged changes after reset:
M	commit3
xz@xzdeMacBook-Pro:~/Desktop/gerrit/test3/matchbox-app-android$ git st
On branch master
Your branch is ahead of 'origin/master' by 13 commits.
  (use "git push" to publish your local commits)
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)
	modified:   commit3
no changes added to commit (use "git add" and/or "git commit -a")

```

### 2、git commit

```
xz@xzdeMacBook-Pro:~/Desktop/gerrit/test3/matchbox-app-android$ git commit -am "commit52+"
[master 3cb427e] commit52+
 1 file changed, 1 insertion(+)
```

### 3、pull
	
<font color=#ff0000 size=4 face="黑体">建议每次review之前  运行 git pull --rebase </font>


```
git pull --rebase
```

### 4、git review



### 5、git alisa 配置

### 5、多branch情况建议配置config文件

```
	cd ~
	vim .gitconfig
```

添加

```
[alias]
    review = !sh -c 'git push origin HEAD:refs/for/$1' -
```

review 分支使用

```
	git review branch-name 
```



<div id="md6"></div>
## reset 介绍


|  type  |   Index | WorkDir  | WD safe|
|-----|-----|-----|-----|
|soft| NO|NO|YES|
|mixed|YES|NO|YES|
|hard|YES|YES|NO|

```
--mixed reset HEAD and index
--soft reset only HEAD
--hard reset HEAD, index and working tree
```

[参考1](https://gerrit-documentation.storage.googleapis.com/Documentation/2.8.1/user-upload.html)