## 1.什么是模块化、组件化和插件化
随着业务的积累，产品的迭代，我们写的工程会越来越大，也越来越臃肿，更加难以维护，那有没有一种方法，能够使得每个人专门负责自己的业务模块，使用的时候把每个人做的模块直接拼装组合起来就行，这样代码也更加灵活，相互之间的耦合性也更低，重用性也能够更大。那么模块化的概念就来了。

[可以关注我的csdn](https://blog.csdn.net/u010302765)
[稀土掘金](https://juejin.im/post/5d8732b8f265da03eb1406b6)


**简单来说, 模块化就是将一个程序按照其功能做拆分，分成相互独立的模块，以便于每个模块只包含与其功能相关的内容。模块我们相对熟悉,比如登录功能可以是一个模块, 搜索功能可以是一个模块, 汽车的发送机也可是一个模块。**

当然从个人的理解上，模块化只是一种思想，就是大化小，分开治理，在实际项目中如何具体实施，目前有两种方案，一个是组件化，一个是插件化
<img src="https://img-blog.csdnimg.cn/20190919135524870.png" width="500" hegiht="100" align=center />  
在网上找到了一张很形象的图

- **组件化方案就是：由若干独立的子模块，组合成一个整体，降低模块间的耦合，这些子模块在补足一定的条件下，都可独立运行。主模块也不会因为缺少任意子模块而无法运行。组件之间可以灵活的组建。** 

类似于积木，拼装组合，易维护

- **插件化方案就是：一个程序的辅助或者扩展功能模块，对程序来说插件可有可无，但它能给予程序一定的额外功能。** 

打个比方，就像现在的应用程序，更多的需要依赖一些第三方库，比如地图sdk、分享sdK、支付sdk等等，导致安装包变得越来越大，单是依赖这些sdk，安装包可能就会额外的增加10-20M的大小；

当需要新增功能时，不得不重新更新整个安装包。再熟读一下上面的定义，就知道它的用途和作用了，那就是有些附加功能，需要时，可灵活的添加，动态的加载。插件化主要是解决的是减少应用程序大小、免安装扩展功能，当需要使用到相应的功能时再去加载相应的模块
<br/></br>
## 2.和插件化的区别
区别根据他们使用的用途，就很好理解了：**组件化在运行时不具备动态添加或修改组件的功能，但是插件化是可以的** 
<br/></br>

## 3.组件化的实践方案
说起组件化的实践方案，只有一首小诗形容，
走遍了各种论坛，看遍了地老天荒，原来最适合的方案啊，就在身旁
<img src="https://img-blog.csdnimg.cn/20190919144029589.png" width="100" hegiht="100" align=center />  
总而言之一句话：各种方案都有，也不缺乏很多写的不错的，但是秉持着商用开发为主，接下来介绍一个最合适的，那就是阿里巴巴出的一套[ARouter](https://github.com/alibaba/ARouter)，它简单易用、它支持多模块项目、它定制性较强、它支持拦截逻辑等诸多优点，接下来会写阿里这套框架的使用方便日后开发。如果有兴趣的小伙伴，可以等我下一篇博客，介绍它的实践原理。
<br/></br>

## 4.开始撸码

### 1.首先，看下工程

<img src="https://img-blog.csdnimg.cn/20190919152515944.png" width="200" hegiht="100" align=center />  

就是一个电商，有3个组件，一个是首页，一个是购物车，一个是个人中心，3个独立的模块
<br/></br>
### 2.做些准备
因为每一个模块都是要能够单独调试的，所以我们先定义每个模块的开关，设置这个模块是否要进行单独调试运行

1. 在工程目录中的build.gradle 中，定义3个变量

```java
buildscript { 
ext.kotlin_version = '1.3.31'

ext {
isRunHome = true // true是Home模块可单独运行
isRunPersonalcenter = true
isRunShopingcar = true
}
```


2. 在子模块中，比如Home模块，设置build.gradle

```java
if(isRunHome.toBoolean()){ // 1.根据之前设定的isRunHome，判断是否需要独立运行
apply plugin: 'com.android.application'
}else {
apply plugin: 'com.android.library'
}

android {
android {
compileSdkVersion 29
buildToolsVersion "29.0.0"

defaultConfig {

if(isRunHome.toBoolean()){ // 2.这里也设置一下，可运行的话，添加applicationId

applicationId "com.bj.home"
}
```

3. 在主模块（app模块）中设置它的build.gradle
```java
dependencies {

if(!isRunHome.toBoolean()){ // 1.如果要独立运行，那么主工程不加载它
implementation project(path: ':home')
}
implementation project(path: ':personalcenter')
implementation project(path: ':shoppingcar')
```
编译一下就是这样
<img src="https://img-blog.csdnimg.cn/20190919160838876.png" width="260" hegiht="100" align=center />  

4.当然还差一步，设置AndroidManifest.xml文件，因为一般来说，一个APP只有一个启动页，在组件单独调试时也需要一个启动页，所以我们需要设置两个文件。就这样

<img src="https://img-blog.csdnimg.cn/20190921164102628.png" width="260" hegiht="100" align=center />  

AndroidManifest文件和ApplicationId 一样都是可以在 build.gradle 文件中进行配置的，所以我们同样通过动态配置组件工程类型时定义的 boolean变量的值来动态修改。需要我们修改子模块（如home）的build.gradle文件。

```java
android {
...

sourceSets {

main {
// 1.单独调试与集成调试时使用不同的 AndroidManifest.xml 文件
// 我们还可以根据不同工程配置不同的 Java 源代码、不同的 resource 资源文件等的
if(isRunHome.toBoolean()) {

manifest.srcFile 'src/main/manifest/AndroidManifest.xml'
} else{
manifest.srcFile 'src/main/AndroidManifest.xml'
}
}
}
}
```

大功告成，使用时只需要修改根目录build.gradle文件中的那3个变量，就可以一键开启该模块的单独运行模式了，亲测有效，好了，我们已经完成了，模块独立化了，子模块可单独运行了，但是，怎么通讯，传递数据呀？组件与组件之间都是不可以直接使用类的相互引用来进行数据传递的!
<img src="https://img-blog.csdnimg.cn/2019091916121357.png" width="100" hegiht="100" align=center />  

<br/></br>
### 3.集成阿里的路由框架ARouter
解决办法就是集成集成阿里的路由框架ARouter，一个用于帮助 Android App 进行组件化改造的框架 —— 支持模块间的路由、通信、解耦
来我们集成一下

#### 3.1 添加依赖

1.在各个模块中添加了对 ARouter 的依赖，当然自己新建一个base模块，依赖添加到base里，其他模块引用它也可以。
```java
android {
...
defaultConfig {
...
javaCompileOptions {
annotationProcessorOptions {
arguments = [moduleName: project.getName()]
}
}
}
```

```java
dependencies {
compile 'com.alibaba:arouter-api:1.2.1.1'
annotationProcessor 'com.alibaba:arouter-compiler:1.1.2.1'
...
}
```
好了，配置完成
<br/></br>
#### 3.2 初始化SDK
我们在自定义的MyApplication中，初始化它

```java
@Override
public void onCreate() {
super.onCreate();

// 这两行必须写在init之前，否则这些配置在init过程中将无效
if(isDebug()) {

// 打印日志
ARouter.openLog();

// 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
ARouter.openDebug();
}
// 初始化ARouter
ARouter.init(this);
}

private boolean isDebug() {

return BuildConfig.DEBUG;
}
```
<br/></br>
#### 3.3 Activity跳转
<img src="https://img-blog.csdnimg.cn/20190921181930537.png" width="500" hegiht="100" align=center />  

1.在目标Activity添加注解 Route 
(home : HomeAty)

```java
/**
* 首页模块
* 
* 其中 path 是跳转的路径，这里的路径需要注意的是至少需要有两级，/xx/xx
* */
@Route(path = "/home/HomeAty")
public class HomeAty extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.aty_home);
}
}
```

2 页面跳转(app : MainActivity)

```java
@Override
public void onClick(View view) {

switch (view.getId()){

// 跳转Activity页面
case R.id.btn_go_home:

ARouter.getInstance().build("/home/HomeAty").navigation();
break;
}
}
```
<br/></br>
#### 3.4 跳转ForResult
<img src="https://img-blog.csdnimg.cn/20190922113249981.png" width="500" hegiht="100" align=center />  

1. 页面跳转及返回(app : MainActivity)

```java
@Override
public void onClick(View view) {

switch (view.getId()){

...

// 跳转Activity页面, 并且返回数据
case R.id.btn_go_aty_forresult:

ARouter.getInstance().build("/home/HomeResultAty").navigation(this, 897);
break;
}
}

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
super.onActivityResult(requestCode, resultCode, data);

if(requestCode == 897 && resultCode == 999){

String msg = data.getStringExtra("msg");
tv_msg.setText(msg);
}
}
```

2. 目标Activity(home : HomeResultAty)
```java
@Route(path = "/home/HomeResultAty")
public class HomeResultAty extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.aty_home_result);

findViewById(R.id.btn_goback).setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View view) {

Intent in = new Intent();
in.putExtra("msg", "从home模块返回的数据");
setResult(999, in);
finish();
}
});
}
}
```

<br/></br>
#### 3.5 获取Fragment
<img src="https://img-blog.csdnimg.cn/20190922131314446.png" width="500" hegiht="100" align=center />  

1. 获取fragment(app : MainActivity)
```java
Fragment mFragment = (Fragment) ARouter.getInstance().build("/home/HomeFragment").navigation();
getSupportFragmentManager().beginTransaction().replace(R.id.fl, mFragment).commit();
```

2.当然fragment也要加注解(home : HomeFrag)
```java
@Route(path = "/home/HomeFragment")
public class HomeFrag extends Fragment {...}
```
<br/></br>
#### 3.6 携带参数的应用内跳转
<img src="https://img-blog.csdnimg.cn/20190922140709315.png" width="500" hegiht="100" align=center />  
1. 主工程(app : MainActivity)

```java
// 携参数跳转
case R.id.btn_go_home_byArgs:

ARouter.getInstance().build("/home/arg")
.withString("msg", "5")
.withDouble("msg2", 6.0)
.navigation();
break;
```

2.目标Activity(home: HomeByArgAty)

```java
@Route(path = "/home/arg")
public class HomeByArgAty extends Activity {

@Autowired(name = "msg")
String arg1;

@Autowired
String arg2;

private TextView tv_msg;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.aty_home_arg);

// 如果使用Autowired注解，需要加入底下的代码
// 当然也可以用 getIntent().getStringExtra("")
ARouter.getInstance().inject(this);

tv_msg = findViewById(R.id.tv_msg);
tv_msg.setText("从主工程传递过来的参数："+arg1);
}
}
```
<br/></br>
---------------------------------进阶用法------------------------------
#### 3.7 拦截器
ARouter也添加了拦截器模式，拦截器有很多用处，比如路由到目标页面时，检查用户是否登录，检查用户权限是否满足，如果不满足，则路由到相应的登录界面或者相应的路由界面。ARouter的拦截器比较奇葩，只需要实现IInterceptor接口，并使用@Interceptor注解即可，并不需要注册就能使用。当然这也有了它的坏处，就是每一次路由之后，都会经过拦截器进行拦截，显然这样程序的运行效率就会降低。Interceptor可以定义多个，比如定义登录检查拦截器，权限检查拦截器等等，拦截器的优先级使用priority定义，优先级越大，越先执行。拦截器内部使用callback.onContiune()/callback.onInterrupt(),前者表示拦截器任务完成，继续路由;后者表示终止路由。例子：
<img src="https://img-blog.csdnimg.cn/20190922160548108.png" width="500" hegiht="100" align=center />  

1.实现IInterceptor接口，自定义拦截器，检测所有跳转中，只要uri为空就拦截，也可以在这请求中再加内容
```java
@Interceptor(priority = 4)
public class LoginInterceptor implements IInterceptor {

@Override
public void process(Postcard postcard, InterceptorCallback callback) {

String uri = postcard.getExtras().getString("uri");
if(TextUtils.isEmpty(uri)){

Log.i("lybj", "uri为空，中断路由");
callback.onInterrupt(null);
}else {

Log.i("lybj", "拦截器执行，uri不为空，继续执行吧");
postcard.withString("msg", "可以随意加内容");
callback.onContinue(postcard);
}
}

@Override
public void init(Context context) {
}
}
```
2. 一个网页正常的跳转
```java
// 拦截器测试
case R.id.btn_test_interceptor:

ARouter.getInstance().build("/home/web")
.withString("uri", "file:///android_asset/schame-test.html")
.navigation();
break;
```
3.目标界面

```java
@Route(path = "/home/web")
public class WebAty extends Activity {

private WebView wv_web;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.aty_web);

Log.i("lybj", getIntent().getStringExtra("msg"));
String uri = getIntent().getStringExtra("uri");
wv_web = findViewById(R.id.wv_web);
wv_web.loadUrl(uri);
}
}
```
<br/></br>
### 4.ARouter踩坑
###### 1. 异常：ARouter::Compiler >>> No module name, for more information, look at gradle log.

这个很坑，翻了翻文档，说是要在所引用的所有的model的build.gradle里面都要加上下面的代码

```java
defaultConfig{
...
javaCompileOptions {
annotationProcessorOptions {
arguments = [moduleName: project.getName()]
}
}
}

```
或者
```java
defaultConfig{
...
javaCompileOptions {
annotationProcessorOptions {
arguments = [AROUTER_MODULE_NAME: project.getName()]
}
}
}
```
AROUTER_MODULE_NAME和moduleName根据不同的版本，选择不同的名字，上面的代码要保证所有模块都要添加，为的是做区分

###### 2.资源名字相同
作者就做了个蠢事，两个model的layout名字一样，主工程加载的时候，总是出问题，所以尽可能的保证每个model的资源名加前缀。

