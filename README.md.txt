Yodo 开发文档整理

1.基础功能模块（ydlibrary）
 a.YuedongSdk 是操作计步，跑步和广告初始化的基础模块，在application中初始化需要根据功能和进程进行实例化。
 b.Buyly只在主进程实例化；
 c.语言配置初始化（PhilologyRepositoryFactory）；
 d.RunAim 数据操作类（数据的上报和拉取）；
 e.保活功能；
 f.基础控件封装；
 g.分享模块的封装（ShareIntent）；
 h.网络请求模块。
.需要开发过程中，通常不涉及到该模块的改动。

2.用户系统（Account,UserInfo）
 a.Account 类包含用户的登录操作和用户币种、利率、国际码、币种单位和个人用户信息（UserInfo）；
 b.对已经登录的用户，再次同步用户信息（UserInfo），并且缓存；
 c.在应用中获取用户信息通过UserInfo获取，如果修改用户信息再成功后，需要同步用户信息，保证用户信息，在当前启动情况下的信息正确性；
 d.新用户国家选择，优先通过sim卡确定。

3.首页（RunAim）与启动广告
 a.计步，跑步，红包信息；
 b.当前计步和跑步在2个Frangment中，禁止在Frangment拉取数据更新接口和注册数据更新接口--禁止频繁调用接口。在Activity实现更新和拉取功能。（保证只有一个地方同步数据）
 c.数据获取失败后，15s 再次获取数据；
 d.启动广告为缓存广告，一天只展示一次并有服务器控制。（WelcomeActivity）


4.网络请求（okhttp）
 a.网络请求加载了语言码到服务器；
 b.替换默认DNS解析，自定义DNS解析策略；
 b.错误码(NetResult)封装了网络请求的各种错误信息；
 d.接口解析需要解析到data层才是真实的数据包；
 e.对需要数据操作的接口，需要继承JSONCacheAble，进行数据解析处理；
 f.对计不数据上报和拉取同步数据库，该逻辑比较复杂，请谨慎修改，禁止同一点频繁操作（不停更新计步信息），保证计步的稳定；

5.服务器控制配置（AppServiceConfig）
 a.应用来自服务器的基础配置类；
 b.开关控制，语言包更新，币种，广告类型，登录方式，利率换算等；
 c.币种和利率划算加载成功后，再次同步到用户信息（Account）中；
 注意：该类一天最多加载4次并且只在主进程中加载，不满足条件的情况下加载缓存。

6.Google广告（GoogleBannerView、GoogleInterstitial）和统计(FirebaseAnalytics)
 a.GoogleBannerView---Banner广告，注意类型设置，加载为动态更新，有可能加载不到广告资源,具体看接口返回日志；
 b.GoogleInterstitial---插屏广告，实例后全局使用，禁止接入为开屏广告（加载慢）

7.Facebook广告(FbInterstitial,FacebookBannerView)和统计 (AppEventsLogger)
 a.FacebookBannerView---Banner广告，注意类型设置，加载为动态更新，有可能加载不到广告资源,具体看接口返回日志；
 b.FbInterstitial---插屏广告，禁止接入为开屏广告（加载慢）


8.广告加载逻辑（InterstContainer,InterstFactory,YodoAdView,AdViewFactory）
  a.AdViewFactory Banner广告加载工厂（YodoAdView 为GoogleBannerView，FacebookBannerView 抽象类）有系统控制加载那种类型的广告；
  b.InterstFactory 插屏广告加载工厂（InterstContainer 为GoogleInterstitial ，FbInterstitial抽象类）有系统控制加载；
  c.广告的加载展示上报需要的对应的参数。

9.Google定位功能（LocationMgrGoogle,GoogleLocaitonService,GMapView,GoogleApiHelper）
 a.GoogleApiHelper GooglePlay 连接服务器操作类；
 b.GoogleLocaitonService 定位更新前台通知（保活）---传入地位通知；
 c.LocationMgrGoogle 定位结果处理和返回类；
 d.GMapView 地图展示类；
  
 注意：google 功能使用需要Google Play Sevice 支持，国内需要翻墙。

10.Google推送（MyFirebaseInstanceIDService,MyFirebaseInstanceIDService）
 a.当前推送还没有正式的启动，ID更新需要上报服务器，收到通知需要启动后台服务（保活），通知的展示有自定义和沿用系统2种；
 b.如果应用运行在后台或为启动，收到通知，需要特殊处理。

11.语言国际化（MultiLanguageUtil，LocalLanguageRepository，ResourceReader）
  a.逻辑：应用优先加载本地下载文件，在无的情况下加载asset中的对应文件，如果还没有加载默认语言en;
  b.MultiLanguageUtil 应用语言操作类，针对本地语言缓存语言的处理；
  c.ResourceReader 语言包解析类；
  d.LocalLanguageRepository 语言获取展示类---通过resource获取到当前控件为文本控件测获取去ID，替换对应语言,语言包自始至终维护一个唯一的Map集合。

 注意：在部分场景语言适配失败的情况：传入的Context是否重写了attachBaseContext()。


12.权限操作类(PermissionUtil,PermissionRequestCode)
  a.PermissionUtil 权限判断和授权类；
  b.PermissionRequestCode 授权对应码。使用过程中保证对应权限和Code一直，防止滥用Code导致不必要的错误（ActivityBase 中已经对部分授权处理了）

13.文件操作（PathMgr,FileEx）
  a.PathMgr 文件目录的创建和路径的使用；
  b.FileEx 文件操作类。


14.应用内跳转（JumpAction,JumpParam）
 a.JumpParam 应用内跳转URL的解析类；
 b.JumpAction 通过解析后的JumpParam跳转对应界面操作类；
 c.非请求应用内跳转的情况，可以使用JumpAction,JumpParam实现复杂跳转；
 d.JumpAction中URL需要特殊处理，不然解析不出来（看源码）；

15.应用保活（com.yuedong.sport.alive，LibMarsdaemon模块）详见介绍省略，具体内容请代码。
16.埋点上报（EventReportGraylog）
  参考服务配置设置参数上报；

17.AI体验相关在全部在com.yuedong.sporti.health

18.Cms相关信息
http://api.yodorun.com/cms/index
http://api.yodorun.com/cms/get_user_info
公用账号yodo
密码yuedong888

19.CMS 或graylog 相关查询信息
message:global_event AND biz:107 AND eid:1 AND app_version:"4.3.2.8.20"(app_config)
message:global_event AND biz:1000 AND eid:100  AND request_path:\/sport\/v2\/main_page(接口调用情况)
message:g_event_log AND biz:108 AND ver:4.3.3.2.3(查询用户)

20.开发事项：
1.图片需要压缩；
2.接口请求涉及到数据解析的需要继承JSONCacheAble,禁止在UI界面操作类直接解析数据；
3.针对9.0手机的适配，调用隐藏API的情况禁止使用反射；manifest禁止横竖屏设置；调用apache网络请求的需设置权限（已经设置）
4.业务逻辑、数据操作与界面间尽量解耦；
5.日志上报和日志打印尽量用户已经封装的L类，无用类尽量不引入。
6.开发打包命令：  gradle clean  :app:assembleDebug（gradle clean  :app:assembleRelease），正式上线打包命令通过python(python  do_package_gradlew.py)打出所有渠道包；
7.多国语言打包功能：python translate.py 生成对应的语言包文件包含MD5，MD5文件需要给营运配置到服务器上去；




 