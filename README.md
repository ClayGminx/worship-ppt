<h1 align="center"><img src="logo.jpg" alt="主日敬拜"/></h1>

一个用于自动制作敬拜 PPT 文件的 Java 应用程序。

由马克约瑟弟兄制作。

![Version](https://img.shields.io/badge/version-1.0.1-blue)
[![GitHub license](https://img.shields.io/github/license/ClayGminx/worship-ppt)](https://github.com/ClayGminx/worship-ppt/blob/master/LICENSE)
![poi-ooxml dependency](https://img.shields.io/badge/poi--ooxml-v5.2.2-9cf)
![sqlite-jdbc dependency](https://img.shields.io/badge/sqlite--jdbc-3.39.2.0-9cf)

## 简介

为了更加快速、方便、准确地制作用于主日敬拜的 PPT 文件，此应用程序 __敬拜PPT文件工具__ 为此而生。

此应用程序可以满足以下三种敬拜模式：

1. 无圣餐
2. 有圣餐
3. 有入会

## 使用方式

该应用程序 __敬拜PPT文件工具__ 必须要在 Java 虚拟机上运行。但是，为了减少繁琐的安装操作，敬拜PPT文件工具已经自带了 Java 虚拟机。
敬拜PPT文件工具分为Windows版和Mac OS版。

推荐你在Windows上使用敬拜PPT文件工具。在Windows上，你只需要打开`敬拜PPT文件工具.exe`。
在Mac上，你需要打开执行脚本`敬拜PPT文件工具.sh`，便打开了操作界面。

注意，在Mac上第一次使用敬拜PPT文件工具时，你需要给`敬拜PPT文件工具.sh`赋予执行权限。首先按下快捷键`command + space`，输入`terminal`，
按下回车键，这时候就打开了终端，然后，在终端里先输入`chmod u+x `，这时候还不能按下回车键，还要注意最后有一个空格，不能省略掉，接着，
把`敬拜PPT文件工具.sh`拖入终端里，这时你会发现`chmod u+x `右边多了`敬拜PPT文件工具.sh`的完全路径，按下回车键，发现没有任何提示，其实这就说明
`敬拜PPT文件工具.sh`已经得到执行权限了。

以后在Mac上使用敬拜PPT文件工具，你应该要先打开终端，然后把`敬拜PPT文件工具.sh`拖入终端里，最后按下回车键，就打开了。

## 高级用法

你可以修改预设好的配置文件、PPT模板、圣经数据库。

### PPT模板

敬拜PPT文件工具制作出来的PPT，全部都是按照幻灯片母版来制作的，敬拜PPT文件工具无非就是把输入的敬拜数据填进去。

预设的PPT模板文件放在“PPT模板”文件夹里，里面有两个PPT文件。`worship.pptx`是常规PPT模板文件，三种敬拜模式都会使用它。
`initiation.pptx`是入会模板文件。制作带有入会环节的PPT文件时，程序会先使用`worship.pptx`，若有入会，则将`initiation.pptx`里的幻灯片拷贝进来。

重要的事情再说一遍！`worship.pptx`用的是幻灯片母版，所以，如果你要修改的话，则依次点击视图->幻灯片母版，然后你会看到很多幻灯片版式。
你若修改一张幻灯片版式，那么使用该版式的所有幻灯片会全都变化。

### 配置文件

敬拜PPT文件工具支持自定义配置。配置文件`worship-ppt.properties`位于“配置”文件夹里。

`worship-ppt.properties`有大量的配置选项。你可以修改制作PPT文件的参数、更换圣经数据，以及更多配置选项。
`worship-ppt.properties`有大量注释教你怎么修改，相信你看得懂。

### 敬拜流程

三种敬拜模式都有自己的流程，它们的流程定义在`worship-procedure.xml`。

`worship-ppt.properties`可以配置`worship-procedure.xml`的位置，默认放在“配置”文件夹里。

在该文件里，`model`定义了敬拜模式，而`worship-step`定义了敬拜阶段，你可以在该文件删除一个`worship-step`，
但是若要增加一个worship-ppt尚未支持的新阶段，就会因不被支持而出错。

### 圣经

敬拜PPT文件工具用 SQLite 来存储圣经，以扩展名`.db`的文件格式默认放在“数据库”文件夹里。默认使用“新标点和合本”。
若要使用其它版本的圣经，你可以在`worship-ppt.properties`中修改`SQLite.path`值。
注意，圣经数据库要有`Bible`和`BookNames`两张表，不妨参考“新标点和合本.db”来制作。