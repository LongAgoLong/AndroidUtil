# AndroidUtil
封装了一些Android常用的代码块  
会持续整理更新  
[![](https://jitpack.io/v/LongAgoLong/AndroidUtil.svg)](https://jitpack.io/#LongAgoLong/AndroidUtil)  
**gradle依赖**
```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
**全部依赖**
```java
implementation 'com.github.LongAgoLong:AndroidUtil:$JitPack-Version$'
```
**单独依赖**
```java
implementation 'com.github.LongAgoLong.AndroidUtil:common:$JitPack-Version$'
implementation 'com.github.LongAgoLong.AndroidUtil:safety:$JitPack-Version$'
implementation 'com.github.LongAgoLong.AndroidUtil:system:$JitPack-Version$'
```
**maven依赖**
```java
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
```java
<dependency>
	<groupId>com.github.LongAgoLong</groupId>
	<artifactId>AndroidUtil</artifactId>
	<version>$JitPack-Version$</version>
</dependency>
```
