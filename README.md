

api下载地址:

https://phone-love-piano-public-ro.oss-cn-shenzhen.aliyuncs.com/demo-apk/viewpager2_page_lifecycle_debug_0403.apk

或手机扫码下载:

![Image apk download qrcode](https://phone-love-piano-public-ro.oss-cn-shenzhen.aliyuncs.com/demo-apk/viewpager2_page_lifecycle_debug_apk_qrcode.png)

##  1.你真的清楚viewpager2一顿翻页+退出操作伴随的page的生命周期方法吗?
不清楚对吧?那不行!得让你清楚,快去看代码吧,很简单的!
什么?!你清楚??? 请听题?

##  2.你能保证你每次使用viewpager2还能非常清晰的记得题1所示问题吗?
记不住是吧?这个可以有!
直接用本库吧,使用超级简单!




##  使用姿势
###  1.项目根目录的gradle文件
buildscript.repositories{ maven { url "https://jitpack.io" } }

allprojects.repositories{ maven { url "https://jitpack.io" } }

###  2.module的gradle文件
implementation 'com.github.jj532655203:ViewPager2PageLifeCycle:1.0.0'

###  3.定义viewpager2的adapter

建议你直接使用默认adapter:DefaultViewPager2Adapter.java,如下:

viewPager2.setAdapter(new DefaultViewPager2Adapter<MathWriteAdapterItemBean, MathWritePageView>(R.layout.layout_adapter_page_math_write, itemBeans, new WeakReference<>(viewPager2)));

MathWriteAdapterItemBean:               泛型T,用于指定你将使用的携带数据的类,<此处请换成你自己的>
MathWritePageView:                      泛型K,用于指定你将在viewpager2中展示的page类,<此处请换成你自己的>
R.layout.layout_adapter_page_math_write:条目page的布局,其根元素必须是泛型K所指的类,如上便是<MathWritePageView layoutWidht="match_parent" layout_height="math_parent">....</MathWritePageView>
itemBeans:                              各条目page的数据
new WeakReference<>(viewPager2):        viewPager2的弱引用

###  4.定义viewpager2的条目page(view)

请继承抽象类BaseViewPager2Page,如下:
public class MathWritePageView extends BaseViewPager2Page
MathWriteAdapterItemBean:               泛型T,用于指定你将使用的携带数据的类,<此处请换成你自己的>
//泛型T和K用来干嘛的?为了方便道友使用的,你搞不错了,搞错了会lint会警告,所以知道使用多简单了吧? :)

重写如下方法:

    /**
     * 挂载新条目数据之前,先将旧页的数据清除
     */
    protected abstract void onClearOldPageData();

    /**
     * 挂载新条目数据
     */
    protected abstract void onConvertView(T bean, int adapterPosition);

    /**
     * 从预加载范围滑到可见条目
     * 此时动画/工作线程等耗费资源的工作应开启
     */
    public abstract void onResume();

    /**
     * 退到预加载范围之外
     * 此时该条目产生的用户数据应保存好,因为这之后,viewPager2.findViewByTag(int)将找不到本view了
     */
    public abstract void onSavePageData();

	
##  本库应该是全网第一个将viewpager2的条目page的生命周期方法总结出来的,觉得好请star,让我们共同为开源世界做贡献!