# PC Web Monitor 用户使用手册 

## 介绍 

**PC Web Monitor**是基于开源项目**Jwebap 0.6.1版**二次开发出来的一个用于java web application 的profiler工具。主要致力于系统的**性能分析**和**优化**方面。目的是希望能够**安全高效**的部署于生产以及测试应用系统，及时地发现应用系统中存在的**性能瓶颈**，以及为一些**动态性很高**，**难于调试和维护**的应用系统**提供帮助**。它不采用JVMPI提供的特性实现监控， 是一个纯粹的JAVA应用，不依赖于OS，JVM，JDK1.4以上用户都可以使用。 同时，它还非常易于使用简单的部署好jar包以后，所有的配置都在 控制台 完成。下面是它的一些特性：

 * **高效:** 执行非常高效，几乎不给系统带来更多的开销，目前已经应用于中国电信数个省级大型业务系统。
 * **纯Java实现:** Jwebap是纯java应用，可以方便的部署于JDK1.5和以上，各种中间件环境。
 * **Plugin架构:** 基于plugin架构进行扩展，所有的功能都是通过plugin方式加入，方便按需使用和加载，默认提供Tracer监控插件，完成对J2ee应用的监控，包括，连接池，SQL，方法调用，业务请求，Memcached访问等方面。
 * **使用简单:** 部署非常简单，对系统没有任何侵入，只需部署完jar包和web.xml后，一切的配置交给控制台完成。

## 快速入门 

### 第一步：部署 
 * 把pc-web-monitor-1.1.jar和其依赖的jar包放到应用的ClassPath下。 如果你是EJB应用并且需要监控EJB的话，需要放到server lib下。 
 * 把pcwebmonitor.xml放到工程web module任意目录中，一般可以放在WebRoot/WEB-INF/目录下。

### 第二步：配置 
修改你应用的（如果没有的话可以新建一个空的WebModule和EJB放在一个EAR中）web.xml:
增加context-param指定pcwebmonitor.xml 的路径
```
<context-param>
	<param-name>jwebap-config</param-name>
	<param-value>/WEB-INF/pcwebmonitor.xml</param-value>
</context-param>
```
增加PCWebMonitor启动Listener 注意：配置在所有 listener 之前，以保证 PCWebMonitor 最先启动，这点对于类增强很重要。
```
<listener>	
	<listener-class>org.jwebap.startup.JwebapListener</listener-class>	
</listener>
```
为Tracer插件增加Filter
```
<filter>
	<filter-name>PageDetectFilter</filter-name>
	<filter-class>org.jwebap.plugin.tracer.http.DetectFilter</filter-class>
	<init-param>
		<param-name>excludeUrls</param-name>
		<param-value>/detect;/detect/*;*.js;*.jpg;*.htm;*.html;*.gif;*.png;*.css;*.swf</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>PageDetectFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```
增加Jwebap控制台Servlet
```
<servlet>
	<servlet-name>detect</servlet-name>
	<servlet-class>org.jwebap.ui.controler.JwebapServlet</servlet-class>	
</servlet>
<servlet-mapping>
	<servlet-name>detect</servlet-name>
	<url-pattern>/detect/*</url-pattern>
</servlet-mapping>
```

### 第三步：启动！

启动应用！如果部署正确的话，输入 PCWebMonitor Servlet 对应的地址，就可以看到PCWebMonitro控制台。

### 第四步：配置tracer插件参数 

进入 PCWebMonitor 控制台，进入Components菜单，已经在pcwebmonitor.xml中添加了Tracer插件，可以查看后台实际配置Tracer插件的具体组件(Component)的参数配置：

![](http://i.imgur.com/UqFwCW5.jpg)

 Http Component:: Http Component 在 Jwebap 中是个非常简单的组件，但是却非常有用。它可以抓取所有的 http 请求，进行分析，包括时间阀值，执行次数，平均执行时间，请求清单，对于不想记录的请求，可以配置 exclude url attern，进行滤除。
 同时，Http Component可以结合Jdbc Component和Memcached Component，监听请求内执行的sql及访问MemCached的操作，进行统计。
```
<component name="HttpComponent" type="org.jwebap.plugin.http.HttpComponent">
	<!--(ms) timings filter's over time -->
	<property name='trace-filter-active-time'>-1</property>
	<!-- max over-time trace size -->
	<property name='trace-max-size'>1000</property>
</component>
```
 以上是pcwebmonitor.xml中Http Component的配置代码，两个配置参数：
 trace-filter-active-time: (ms)超过该毫秒数的执行轨迹将被记录
 trace-max-size: 最大记录的超时轨迹数

 Method Component:: Method Component与其说是一项功能，不如说是一种机制。灵活的使用，可以达到更多的监控效果。组件本身可以通过配置java包名或类名，实现对这些包和类的方法进行监控。
 一般来说，应用系统实现，会采取分层结构，使用Method Component有针对性的对某个层次或模块进行监控，更能有效帮助找到系统瓶颈，同时又不会因为监控范围过大带来不必要的开销。一般可以使用它，对服务层（比如 EJB,WebService），或者业务层，甚至DAO层进行监控，根据系统实际情况，选取合适的粒度进行分析监控，比泛泛的全量监控，更有助于找到系统瓶颈。
 和Http Component一样，Method Component也可以结合Jdbc Component 及 Memcahced Component，实现对方法内执行的sql和访问MemCached的操作进行跟踪统计。
```
<component name="MethodComponent" type="org.jwebap.plugin.method.MethodComponent">
	<property name='trace-filter-active-time'>-1</property>
	<property name='trace-max-size'>1000</property>
	<!--
		package name and class name that monitored by MethodComponent,
		e.g.: 'test.*;test.Test' , divided by ';'
	-->
	<property name='detect-clazzs'>test.*;</property>
</component>
```
 以上是pcwebmonitor.xml中Method Component的配置代码，三个配置参数：[[BR]]
 trace-filter-active-time: (ms)超过该毫秒数的执行轨迹将被记录[[BR]]
 trace-max-size: 最大记录的超时轨迹数[[BR]]
 detect-clazzs: 要监控的类，可以配置包名和类名, 如test.*;test.Test,但不支持通配符,多个类名之间通过;分隔

 Jdbc Component:: Jdbc Component是Jwebap中，最常用的组件。对于一般的企业应用，数据库分析往往能够最直接找到瓶颈所在。Jdbc Component组件能够实现对连接的监控，设置时间阀值，监控连接泄漏，监控连接打开的Statement，和所有执行的sql，同时可以找出执行sql的代码行数，同时可以找出执行sql的代码行数，这点非常有用。
 组件对于jdbc的监控，是全范围的，可以支持多个数据源的应用，也可以支持非本地数据源(jndi 数据源)。
```
<component name="JdbcComponent" type="org.jwebap.plugin.jdbc.JdbcComponent">
	<property name='trace-filter-active-time'>-1</property>
	<property name='trace-max-size'>1300</property>
	<property name='connection-listener'><!--Connection Listener-->
	org.jwebap.plugin.http.ServletOpenedConnectionListener;
	org.jwebap.plugin.method.MethodOpenedConnectionListener
	</property>
	<!--
			1)Local datasource: set your ConnectionManagerClass,
			or the connection pool 's datasource.
 
 If you have 
			more than one class ,divided by ';'.
			c3p0:com.mchange.v2.c3p0.ComboPooledDataSource; 
			dbcp: org.apache.commons.dbcp.BasicDataSource
			Also,other class. Jwebap will inject driver-clazzs,and detect any 
			connection and datasource object it's method renturn.
			Note:  'driver-clazzs =jdbc driver' is deprecated. Beacause of connection
			pool, set 'driver-clazzs =jdbc driver', jwebap will find out all connection is leaked.
			2)JNDI datasource: If your application uses jndi datasource,
			you can set the class which 
manages connections in your 
			application as driver,e.g.: 'com.china.telecom.ConnectionManager'.
			Else if you use spring to 
get jndi datasource ,you also can set 
			driver-clazzs=org.springframework.jndi.JndiObjectFactoryBean.
			JdbcComponent will inject this class to proxy all connection the class's method return.
		
	-->
	<property name='driver-clazzs'></property>
</component>
```
 以上是pcwebmonitor.xml中Jdbc Component的配置代码，三个配置参数：
 trace-filter-active-time: (ms)超过该毫秒数的执行轨迹将被记录
 trace-max-size:最大记录的超时轨迹数
 connection-listener: Connection监听器，可配置多个,以;分隔
 driver-clazzs: 对于本地数据源的配置，driver-clazzs 参数可以配置自己应用的ConnectionManager类，也可以配置连接池的Datasource类，c3p0:com.mchange.v2.c3p0.ComboPooledDataSource;
 dbcp:org.apache.commons.dbcp.BasicDataSource。可以配置，任何用于管理连接的类，jwebap会对 driver-clazzs 方法中返回的所有Connection和Datasource对象进行监控。
 值得注意的是：本地数据源不建议直接配置数据库驱动作为driver-clazzs，由于连接池的缘故，配置数据库驱动作为 driver-clazzs 会造成jwebap误以为所有的连接都泄漏了。
 对于jndi数据源，driver-clazzs 参数配置（很多Jwebap的使用者无法正确配置这点，特此注意）需要配置本地用于获取连接或者数据源的类，举例，应用采用 spring 的 JndiObjectFactory获取jndi 数据源对象，那么 driver-clazzs 就可以设为org.springframework.jndi.JndiObjectFactoryBean；再比如，应用是自己封装获取jndi数据源的方法，那么也可以配置 driver-clazzs 为那个类。Jwebap 会在启动时，对 driver-clazzs 做字节码注入，对driver-clazzs 所有返回 Connection 或者 Datasource 对象的方法进行监控以获取 sql 信息。

 Memcached Component:: Memcached Component是PC Web Monitor 1.1版本中根据公司web项目的特点新加的一个组件，能够实现对访问MemCached动作的监控，并且把结果集成到Http Component和Method Component中清晰地展现出来。对于公司开发内部最近对web应用作出前台页面一次请求访问MemCached的次数不能超过100次的规定，可以简单方便地把结果显示出来。
```
<component name="MemCachedComponent" type="org.jwebap.plugin.memcached.MemCachedComponent">
	<property name='memcached-listener'>
	org.jwebap.plugin.tracer.http.ServletMemCachedListener;
	org.jwebap.plugin.tracer.method.MethodMemCachedListener
	</property>
</component>
```
 以上是pcwebmonitor.xml中MemCached Component的配置代码，只有一个配置参数：
 memcached-listener: MemCahced监听器，可配置多个,以;分隔

## 界面截图 
### Jdbc Traces 

![](http://i.imgur.com/GKdlMrw.jpg)

### Jdbc Stat 

![](http://i.imgur.com/wkEMZ2b.jpg)

### Http Traces 

![](http://i.imgur.com/tDNl2fr.jpg)

### Http Stat 

![](http://i.imgur.com/vqwYJIK.jpg)

