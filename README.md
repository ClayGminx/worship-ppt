<h1 align="center"><img src="logo.jpg" alt="主日敬拜"/></h1>

一个用于自动制作敬拜 PPT 文件的 Java 应用程序。

由马克约瑟弟兄制作。

![Version](https://img.shields.io/badge/version-1.0.1-blue)
[![GitHub license](https://img.shields.io/github/license/ClayGminx/worship-ppt)](https://github.com/ClayGminx/worship-ppt/blob/master/LICENSE)
![poi-ooxml dependency](https://img.shields.io/badge/poi--ooxml-v5.2.2-9cf)
![sqlite-jdbc dependency](https://img.shields.io/badge/sqlite--jdbc-3.39.2.0-9cf)

## 简介

为了更加快速、方便、准确地制作用于主日敬拜的 PPT 文件，此应用程序应用 __worship-ppt__ 而生。

此应用程序可以满足以下三种敬拜模式：

1. 无圣餐
2. 有圣餐
3. 有入会

## 使用方式

该应用程序 __worship-ppt__ 必须要在 Java 虚拟机上运行。但是，为了减少繁琐的安装操作，worship-ppt已经自带了 Java 虚拟机，以及启动脚本。
worship-ppt分为Windows版和Mac OS版。
Windows版的启动脚本是`worship-ppt.bat`，Mac OS版的启动脚本是`worship-ppt.sh`。你只需要打开启动脚本，按照提示去操作就行了。

worship-ppt要求使用 ini 文件来作为输入数据。编辑 ini 文件是使用worship-ppt最重要的一个环节。
你只需要用 Windows 操作系统的记事本就可以打开编辑了，不过我更加推荐用 Notepad++ 来编辑，它更加智能。
worship-ppt已经提供了 ini 文件的模板`input.ini`，该文件已经告诉了你怎么编辑，相信你一定能一看就懂。

## 高级用法

你可以修改预设好的配置文件、PPT模板、圣经数据库。

### PPT模板

worship-ppt制作出来的PPT，全部都是按照幻灯片母版来制作的，worship-ppt无非就是把输入的敬拜数据填进去。

预设的PPT模板文件放在“PPT模板”文件夹里，里面有两个PPT文件。`worship.pptx`是常规PPT模板文件，三种敬拜模式都会使用它。
`initiation.pptx`是入会模板文件。制作带有入会环节的PPT文件时，程序会先使用`worship.pptx`，若有入会，则将`initiation.pptx`里的幻灯片拷贝进来。

重要的事情再说一遍！`worship.pptx`用的是幻灯片母版，所以，如果你要修改的话，则依次点击视图->幻灯片母版，然后你会看到很多幻灯片版式。
你若修改一张幻灯片版式，那么使用该版式的所有幻灯片会全都变化。

### 配置文件

worship-ppt支持自定义配置。配置文件`worship-ppt.properties`位于“配置”文件夹里。

`worship-ppt.properties`有大量的配置选项。你可以修改制作PPT文件的参数、更换圣经数据，以及更多配置选项。
`worship-ppt.properties`有大量注释教你怎么修改，相信你看得懂。

### 敬拜流程

三种敬拜模式都有自己的流程，它们的流程定义在`worship-procedure.xml`。

`worship-ppt.properties`可以配置`worship-procedure.xml`的位置，默认放在“配置”文件夹里。

在该文件里，`model`定义了敬拜模式，而`worship-step`定义了敬拜阶段，你可以在该文件删除一个`worship-step`，
但是若要增加一个worship-ppt尚未支持的新阶段，就会因不被支持而出错。

其中`model`值是在`worship-ppt.properties`里定义的，属性名前缀是`worship.model.`。比如，你在 ini 文件里输入的敬拜模式是“无圣餐”，那么，
worship-ppt会在`worship-ppt.properties`里根据`worship.model.无圣餐`找到映射值，默认是`min`，
最后再根据`min`在`worship-procedure.xml`里找到敬拜流程。

### 圣经

worship-ppt用 SQLite 来存储圣经，以扩展名`.db`的文件格式默认放在“数据库”文件夹里。默认使用“新标点和合本”。
若要使用其它版本的圣经，你可以在`worship-ppt.properties`中修改`SQLite.path`值。
注意，圣经数据库要有`Bible`和`BookNames`两张表，不妨参考“新标点和合本.db”来制作。