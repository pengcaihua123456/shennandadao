[![](https://jitpack.io/v/liqinew/slidenavigation.svg)](https://jitpack.io/#liqinew/slidenavigation)
[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E6%9D%8E%E5%A5%87-orange.svg)](https://github.com/LiqiNew)
# SlideNavigation
类似‘今日头条顶部导航栏跟手势滑动’效果

### 效果预览
<image src="./image/demo.gif" width="400px" height="700px"/>

### 内部远程依赖Library（已经远程依赖的Library，切勿重复依赖。）

**BaseLogger：'com.github.liqinew:baselogger:V.1.0.0'**<br>

**BaseFragment：'com.github.liqinew:basefragment:V.1.0.0'**<br>

### 如何使用（效果封装进Fragment）

#### Gradle远程依赖 ####
**1：在项目根目录build.gradley**	<br>

```gradle
allprojects {
　　repositories {
  　　//依赖仓库
　　　maven { url 'https://jitpack.io' }
　　}
}
```

**2：依赖SlideNavigation**<br>

```gradle
compile 'com.github.liqinew:slidenavigation:V.1.0.1'
```
#### 操作参考方案（当前类继承AppCompatActivity）
```java
/**
* 把XML定义的FrameLayout替换成“导航栏跟手势滑动效果Fragment”
*/
getSupportFragmentManager()
.beginTransaction()
.replace(R.id.fragment, ExploreViewPagerFragment.newInstance())
.commit();
```
#### ExploreViewPagerFragment 静态操作A P I
* **通过ExploreViewPagerFragment.newInstance()静态方法获取ExploreViewPagerFragment对象**

#### ExploreViewPagerFragment 非静态操作A P I
```java
/**
* 设置当前那个页面显示
*
* @param index 页面索引值
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setViewpagerIndxe(int index)

/**
* 设置viewpager一次性加载几个对象
*
* @param cacheLimit 一次性加载数量
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setViewpagerCacheLimit(int cacheLimit);

/**
* 设置滑块默认图片和滑动时的图片
*
* @param backgroundId 默认图片
* @param slidingBlock 滑动图片
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setSlidingTabStripImage(int backgroundId,int slidingBlock);

/**
* 设置滑块字体颜色选择器
*
* @param colorSelect 滑块字体颜色选择器
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setTextColorSelect(int colorSelect);

/**
* 设置标题滑块是否需要跟着手势滑动效果
*
* @param slidingTag true是滑动，false为不滑动。默认为true
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setSlidingTag(boolean slidingTag);

/**
* 设置是否title字体选中后是否需要变大效果
*
* @param textSizeTag true是变大false为不变大。默认为false
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setTextTitleSizeTag(boolean textSizeTag);

/**
* 设置选中的title字体是否变粗
*
* @param textTitleSizeCoarsening true是变粗false为不变粗。默认为false
*
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setTextTitleSizeCoarsening(boolean textTitleSizeCoarsening);

/**
* 设置标题字体尺寸
*
* @param textSize 字体尺寸
*
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setTextTitleSize(int textSize);

/**
* 设置标题放大字体尺寸
*
* @param textZoomInSize 标题放大字体尺寸
*
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setTextTitleZoomInSize(int textZoomInSize);

/**
* 单个添加要显示的fragment界面和fragment标题名字
*<p>
*提示"setFragmentObjList()赋值",请调用此方法赋值。
*</p>
* @param titleName fragment标题名字
* @param fragmentClass 要显示的fragment界面class
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.addFragment(String titleName,Class<? extends BaseFragment> fragmentClass);

/**
* 批量添加要显示的fragment界面和fragment标题名字
*<p>
*提示"setFragmentObjList()赋值",请调用此方法赋值。
*</p>
* @param titleName fragment标题名字数组
* @param fragmentClassList 要显示的fragment界面class集合
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.addFragment(String[] titleName, List<Class<? extends BaseFragment>> fragmentClassList);

/**
* 设置滑动控件显示控件方位枚举。默认顶部
*
* @param showOrientationEnum 滑动控件显示控件方位枚举
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setSlidingShowOrientation(SlidingShowOrientationEnum showOrientationEnum);

/**
* 设置滑块独立控件内边距。
* <p>
* 设置的值不能为负数,设置值单位为px.
* <p>
*
* @param left   左边内边距.默认值-1
* @param top    顶部内边距.默认值-1
* @param right  右边内边距.默认值-1
* @param bottom 底部内边距.默认值-1
* @return ExploreViewPagerFragment
*/
ExploreViewPagerFragment.setPadding(int left, int top, int right, int bottom);

/**
* 运行当前显示界面捆绑的fragment显示方法
*/
ExploreViewPagerFragment.onShow();
```
### 如有问题，请下载Demo查看或者查看我的博客文档
[我的博客](http://www.jianshu.com/p/739759fa36fd) 
