<h1 align="center"><img src="logo.jpg" alt="主日敬拜"/></h1>

一个用于自动制作敬拜 PPT 文件的 Java 应用程序。

由马克约瑟弟兄制作。

![Version](https://img.shields.io/badge/version-1.0.1-blue)
[![GitHub license](https://img.shields.io/github/license/ClayGminx/worship-ppt)](https://github.com/ClayGminx/worship-ppt/blob/master/LICENSE)
![poi-ooxml dependency](https://img.shields.io/badge/poi--ooxml-v5.2.2-9cf)
![sqlite-jdbc dependency](https://img.shields.io/badge/sqlite--jdbc-3.39.2.0-9cf)

## 简介

为了更加快速、方便、准确地制作用于主日敬拜的 PPT 文件，此应用程序应用而生。

此应用程序可以满足以下三种敬拜模式：

1. 无圣餐
2. 有圣餐
3. 有入会

## 使用方式

该应用程序必须要在 Java 虚拟机上运行。但是，为了减少繁琐的安装操作，此应用程序已经自带了 Java 虚拟机，以及启动脚本。此应用程序分为Windows版和Mac OS版。
Windows版的启动脚本是`worship-ppt.bat`，Mac OS版的启动脚本是`worship-ppt.sh`。你只需要打开启动脚本，按照提示去操作就行了。

该应用程序要求使用 ini 文件来作为输入数据。编辑 ini 文件是使用此应用程序最重要的一个环节。你只需要用 Windows 操作系统的记事本就可以打开编辑了，
不过我更加推荐用 Notepad++ 来编辑，它更加智能。此应用程序已经提供了 ini 文件的模板`input.ini`，该文件已经告诉了你怎么编辑，相信你一定能一看就懂。

## 高级用法

用压缩软件，如WinRAR，打开此应用程序的jar包`worship-ppt.jar`，可以对其进行高级的配置。

### PPT模板

此应用程序制作出来的PPT，全部都是按照幻灯片母版来制作的，此应用程序无非就是把输入的敬拜数据填进去。

如果你想要修改模板的话，打开`worship-ppt.jar`，进入`assets/ppt`，里面有两个PPT文件。`worship.pptx`是常规PPT模板文件，三种敬拜模式都会使用
它。`initiation.pptx`是入会模板文件。制作带有入会环节的PPT文件时，程序会先使用`worship.pptx`，中间将`initiation.pptx`里的幻灯片拷贝进来。

### 敬拜流程

三种敬拜模式都有自己的流程，它们的流程定义在`worship-procedure.xml`。

在该文件里，`model`定义了敬拜模式，而`worship-step`定义了敬拜阶段，你可以在该文件删除一个`worship-step`，但是若要增加一个此应用程序尚未支持的
新阶段，就因不被支持而出错。

### 圣经

`worship-ppt.jar`里`assets/sqlite`，存在着“新标点和合本”和“新译本”的`.db`文件，其实它们都是 SQLite 数据库文件，你可以往里面增加更多版本的
圣经。

此应用程序默认使用“新标点和合本”，若要使用其它版本的圣经，则要打开`worship-ppt.jar`里`worship-ppt.properties`核心配置文件，找到`scripture.version`，
修改为你想要的版本。