# Maven 配置 sonarqube

## sonar 地址

[sonar:http://192.168.14.130:9000/](http://192.168.14.130:9000/)

## 配置 sonar

### 添加 plugin

#### 1. 在 `pom.xml` 中添加 plugin

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

#### 2. 配置 profile

```xml
<profiles>
		<profile>
			<id>sonar</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<properties>
				<!--相关属性介绍: https://docs.sonarqube.org/display/SONAR/Analysis+Parameters-->
				<sonar.jdbc.url>jdbc:mysql://192.168.14.130:3306/sonar</sonar.jdbc.url>
				<sonar.jdbc.driver>com.mysql.jdbc.Driver</sonar.jdbc.driver>
				<sonar.jdbc.username>sonar</sonar.jdbc.username>
				<sonar.jdbc.password>sonar</sonar.jdbc.password>
				<sonar.login>${sonar.login}</sonar.login>
				<sonar.password>${sonar.password}</sonar.password>
				<sonar.host.url>http://192.168.14.130:9000</sonar.host.url>
			</properties>
		</profile>
	</profiles>
``` 

[sonar 配置](https://docs.sonarqube.org/display/SONAR/Analysis+Parameters)

#### 3. 配置 settings.xml

`sonar.login` 和 `sonar.password` 属于个人账号，配置在本地 `settings.xml` 中。
    
路径： `~/.m2`, 如果不存在就创建一个

    
```xml
        <profiles>
        <profile>
            <id>sonar-custom</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <sonar.login>*****</sonar.login>
                <sonar.password>*****</sonar.password>
            </properties>
        </profile>
    </profiles>
```
    
#### 4. 运行

`mvn sonar:sonar`


    

