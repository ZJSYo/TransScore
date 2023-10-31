# TransScore
一款能够实现听曲写谱，写文生音，图像转文的音频，文本，图像多模态转化App。

#### 环境配置

* 客户端修改服务器端地址在MyApplication/app/src/main/java/com/example/myapplication/logic/network/ServiceCreator.kt中修改BASE_URL即可

* 服务器要求显存>14GB,建议至少16GB显存

* Conda环境路径以及转换脚本路径在TransferController中配置，以及各类文件存放的位置在FileUtils中配置

#### 目录说明

-TransScore

|--Readme

|--script 脚本路径，其中包含转换的py脚本，具体使用见T ransferController中的使用，根据环境配置修改相应路径即可

|--src_client：安卓客户端代码

|--src_server：服务器端代码

