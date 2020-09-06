# AndroidUtil

------

封装了一些Android常用的代码块 ，后续会持续整理更新  
[![](https://jitpack.io/v/LongAgoLong/AndroidUtil.svg)](https://jitpack.io/#LongAgoLong/AndroidUtil)  

### gradle依赖

```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
### 全部依赖

```java
def version = "1.6.7"
implementation "com.github.LongAgoLong:AndroidUtil:$version"
```
### 单独依赖

```java
def version = "1.6.7"
implementation "com.github.LongAgoLong.AndroidUtil:system:$version"
implementation "com.github.LongAgoLong.AndroidUtil:common:$version"
implementation 'com.github.LongAgoLong.AndroidUtil:safety:$version"
implementation "com.github.LongAgoLong.AndroidUtil:imageproxy:$version"
implementation "com.github.LongAgoLong.AndroidUtil:imageproxy-ext-glide:$version"
implementation "com.github.LongAgoLong.AndroidUtil:recyclerview-help:$version"
implementation "com.github.LongAgoLong.AndroidUtil:pinyinlib:$version"
```
### maven依赖

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
```xml
<dependency>
	<groupId>com.github.LongAgoLong</groupId>
	<artifactId>AndroidUtil</artifactId>
	<version>$JitPack-Version$</version>
</dependency>
```
