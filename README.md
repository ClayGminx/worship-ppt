# ![主日敬拜](logo.jpg#pic_center)

一个用于自动制作敬拜 PPT 文件的 Java 应用程序。

由马克约瑟弟兄制作。

![Version](https://img.shields.io/badge/version-v1.4.0-blue)
[![GitHub license](https://img.shields.io/github/license/ClayGminx/worship-ppt)](https://github.com/ClayGminx/worship-ppt/blob/master/LICENSE)
![poi-ooxml dependency](https://img.shields.io/badge/poi--ooxml-v5.2.2-9cf)
![sqlite-jdbc dependency](https://img.shields.io/badge/sqlite--jdbc-3.39.2.0-9cf)

## 简介

为了更加快速、方便、准确地制作用于主日敬拜的 PPT 文件，此应用程序 __Worship PPT__ 为此而生。

此应用程序可以满足以下三种敬拜模式：

1. 无圣餐
2. 有圣餐
3. 有入会

## 使用方式

该应用程序 __Worship PPT__ 必须要在 Java 虚拟机上运行。但是，为了减少繁琐的安装操作，敬拜PPT文件工具已经自带了 Java 虚拟机。
敬拜PPT文件工具分为 Windows 版和 Mac OS 版。

在 Windows 上，你只需要打开`Worship PPT.exe`。在 Mac 上，你应先把`Worship PPT.app`拖入到应用程序中，这也是自 v1.3.0 开始以来的新特性，
你不再需要用终端打开它了。

## 高级用法

你可以修改预设好的配置文件、PPT模板、圣经数据库。

### 配置文件

你可以从菜单栏中依次点击选项->自定义配置，输入你的配置文件的完全路径或相对路径。默认配置文件位于“config”文件夹里。

`worship-ppt.properties`有大量的配置选项。你可以修改制作PPT文件的参数、更换圣经数据，以及更多配置选项。

`worship-ppt.properties`有大量注释教你怎么修改，相信你看得懂。

### PPT模板

敬拜PPT文件工具制作出来的PPT，全部都是按照幻灯片母版来制作的，敬拜PPT文件工具无非就是把输入的敬拜数据填进去。

预设的PPT模板文件放在“PPT模板”文件夹里，里面有两个PPT文件。`worship.pptx`是常规PPT模板文件，三种敬拜模式都会使用它。
`initiation.pptx`是入会模板文件。制作带有入会环节的PPT文件时，程序会先使用`worship.pptx`，若有入会，则将`initiation.pptx`里的幻灯片拷贝进来。

重要的事情再说一遍！`worship.pptx`用的是幻灯片母版，所以，如果你要修改的话，则依次点击视图->幻灯片母版，然后你会看到很多幻灯片版式。
你若修改一张幻灯片版式，那么使用该版式的所有幻灯片会全都变化。

### 敬拜流程

三种敬拜模式都有自己的流程，它们的流程定义在`worship-procedure.xml`。

`worship-ppt.properties`可以配置`worship-procedure.xml`的位置，默认放在“config”文件夹里。

在该文件里，`model`定义了敬拜模式，而`worship-step`定义了敬拜阶段，你可以在该文件删除一个`worship-step`，
但是若要增加一个worship-ppt尚未支持的新阶段，就会因不被支持而出错。（不妨多为软件作者祈祷，制作出一个更加通用的软件来。）

### 圣经

敬拜PPT文件工具用 SQLite 来存储圣经，以扩展名`.db`的文件格式默认放在“数据库”文件夹里。默认使用“和合本”。
若要使用其它版本的圣经，你可以在`worship-ppt.properties`中修改`SQLite.path`值。
注意，圣经数据库要有`Bible`和`BookNames`两张表，不妨参考“和合本.db”来制作。