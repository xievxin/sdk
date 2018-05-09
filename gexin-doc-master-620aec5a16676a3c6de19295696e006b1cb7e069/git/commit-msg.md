# Git Commit Msg

## 背景

- 规范 commit log
- 提高 review 效率


## 格式:

```
	<type>(<scope>): <subject>
```

##### Allowed "type" values:

- **feature** (new feature for the user, not a new feature for build script)
- **update** (update feature code,not a new feature)
- **fix** (bug fix for the user, not a fix to a build script)
- **style** (formatting, missing semi colons, etc; no production code change)
- **refactor** (refactoring production code, eg. renaming a variable)
- **docs** (changes to the documentation)
- **test** (adding missing tests, refactoring tests; no production code change)
- **chore** (updating grunt tasks etc; no production code change)


##### Example "scope" values:

- init
- runner
- watcher
- config
- web-server
- proxy



[参考](http://karma-runner.github.io/0.13/dev/git-commit-msg.html)