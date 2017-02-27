SpringBoot的单元测试
==================
[TOC]

### Springboot集成Junit单元测试规范

#### Junit的使用
>   本文主要描述引入Spring-Test框架支持
>
###主要注解及使用说明
>
#### @Test  该注解作用于测试方法之前，用于引用测试方法，该注解有几个值
>  
expected : 指定的期望抛出的异常类型，如果测试结果抛出的异常和期望值一致则通过测试，否则报Failures，测试不通过
timeout ： 指定当前测试耗时，如果超过了设置的期望耗时，则报Failures，测试不通过
>
#### 示例:

``` java
@Test(expected  = IllegalArgumentException.class)
public void testExpected(){
        List list = null;
        list.get(1);
}
```
在以上示例中，期望抛出异常 IllegalArgumentException ，而实际抛出了 NullPointerException 所以报了Failures trace，测试不通过

而
``` java
@Test(timeout  = 1000)
    public void testExpected2(){
        for(int i = 0; i< 100000; i++){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
```
在上例中 期望该方法是在100ms内操作完成，而实际超过了期望时间，则报Failures ，测试不通过trace

@BeforeClass  在当前类中所有方法执行之前最先被调用，并且只被调用一次

@AfterClass   在当前类中所有方法执行之后最后被调用，并且只被调用一次

@Before  在每个测试方法执行之前都会先执行

@After  在每个测试方法执完之后都会执行

@Ignore  在测试方法上，该方法就会被忽略掉，不加入测试

一般的顺序是
@BeforeClass -> @Before -> @Test -> @After -> @Before -> @Test -> @After -> @AfterClass
在实践测试中我们一般需要把某些需要预先加载的资源放到 @BeforeClass注解的方法中，把需要最终关闭资源的操作放到 @AfterClass中

如果我们只是针对单个test方法测试的话也可以把资源操作放到@Before 和 @After中
但是如果是需要多个test方法一起运行的话那么资源操作类的只能放到@BeforeClass 和 
@AfterClass 中了

在junit中，核心的是 test测试类 、suite测试集 和 runner测试运行器，三个核心对象来支撑
其中测试类我们已经知道了是什么东西了，那么什么是 suite测试集 ？
测试集（TestSuite）：测试集是把多个相关测试归入一个组的表达方式，在Junit中，如果我们没有明确的定义一个测试集，那么Juint会自动的提供一个测试集，一个测试集一般将同一个包的测试类归入一组；具体可见:
http://blog.csdn.net/kewen1989/article/details/19815843
和
http://blog.csdn.net/lyliyongblue/article/details/44708075
runner的使用
 @RunWith
　　当类被@RunWith注解修饰，或者类继承了一个被该注解修饰的类，JUnit将会使用这个注解所指明的运行器（runner）来运行测试，而不是JUnit默认的运行器
详细请参照:
http://blog.csdn.net/zen99t/article/details/50572373
http://blog.csdn.net/qqHJQS/article/details/46523721
### 断言的使用
#### asserrtXXX系列方法的使用
assertSame("错误描述信息", A, B)  如果A 和B 不是同一引用时，
等价于 !=  则会在failurestrace里面输出错误描述
示例:
```java
@Test
public void testReference(){
        
        List list = new ArrayList();
        List list2 = new ArrayList();
        List list3 = list;
//      List list3 = new ArrayList();
        /** 断言 */
        assertSame("list和list3两者不是同一个引用", list, list3); //如果list和list3不是同一引用时则 这行就抛failures trace 不会往下执行了
        
        System.out.println("======= continue run =======>");
        
        assertSame("list和list2两者不是同一个引用", list, list2);
        
}

```

assertEquals("错误描述信息", A, B)  如果A 和B 的值不相等时等价于，!equals
 则会在failurestrace里面输出错误描述
 示例:
```java
@Test
public void testValue(){
        String S1 = new String("S1");
        String S2 = new String("S2");
        String S3 = new String("S1");
//      String S3 = new String("S3");
        /** 断言 */
        assertEquals("S1和S3两者不相等", S1, S3); //如果S1和S3不是同一引用时则 这行就抛failures trace 不会往下执行了
        
        System.out.println("======= continue run =======>");
        
        assertEquals("S1和S2两者不相等", S1, S2);
    }
```

相类似的还有
assertNotNull("错误描述信息", A);
assertTrue("错误描述信息", A);
等等之类方法，无非就是在错误描述参数这里写上发生错误时给出的提示，以便于后续的参数当不满足该方法的要求时给出自定义的错误描述
>
等等，以上就是Junit的一些组件的简单介绍，下面我们来结合公司的使用情况基于SpringBoot给出一套测试模板；
>
###基于SpringBoot的测试模板
所有的Test类都需要继承BaseTest才可以编写单元测试

####示例:
先创建了一个加载所需配置文件的Junit基类(BaseTest)
```java
@RunWith(SpringRunner.class) // 告诉JUnit运行使用Spring的测试支持,即引入Spring-Test框架支持[其中 SpringRunner是SpringJUnit4ClassRunner的新名字]
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)// 表示:带有Spring Boot支持的引导程序（例如，加载应用程序、属性等等)[其中webEnvironment允许为测试配置特定的“网络环境”。你可以利用一个MOCK小服务程序环境开始你的测试，或者使用一个运行在RANDOM_PORT 或者 DEFINED_PORT上的真正的HTTP服务器,另外还有MOCK 及NONE]
@ActiveProfiles("xxx")//表示加载哪一个application-xxx.properties或者application-xxx.yaml
public abstract class BaseTest {

}
```
#####webEnvironment属性有以下4种
1、Mock —— 加载WebApplicationContext并提供Mock Servlet环境
2、RANDOOM_PORT —— 加载EmbeddedWebApplicationContext并提供servlet环境，内嵌服务的监听端口是随机的
3、DEFINED_PORT —— 加载EmbeddedWebApplicationContext并提供servlet环境，内容服务的监听端口是定义好的。默认端口是8080
4、NONE —— 加载ApplicationContext，启动SpringApplication时，不支持Servlet环境
>

所有测试类都继承该基类，并各自编写测试方法
在本项目中使用MockMvc来对接口进行Http请求，其中MockMvc的基类为MockMvcBaseTest
```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class MockMvcBaseTest {
    @Autowired
    private WebApplicationContext context;

    public MockMvc mvc;

    @Before
    public void setUp() {
        //初始化 MockMvc 对象
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }
    //其他代码
}
```
该基类中使用了application-local.yaml配置
所有需要测试http接口的测试类都需要继承该基类
并在各自的@Test方法中组装好参数和url[相对路径即可]并调用相应的该基类中操作http接口的方法即可得到相应接口返回值
如：
需要测试本项目的  /testPostJson 接口，我们需要做下列动作
#####1、继承基类 ：
```java
public class SimpleMockMvcTest extends MockMvcBaseTest {
}
```
#####2、构造参数并调用对应方法  ： postByJosn(String url, JSONObject json)
```java
@Test
public void postByJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", 12);
        json.put("name", "gavin");
        json.put("password", "gavin520");
        String result = this.postByJosn("/testPostJson", json);
        System.out.println("POST JSON return ==> \n" + result);
}
```
即可和在postman或restClient上测试接口一样效果
#####3、@MockBean 声明需要模拟的服务
如需要对Service的方法进行mock打桩
```java
public class MockServiceTest extends MockMvcBaseTest {

    //使用Mock的Service对象
    @MockBean
    private LoginService loginService;

    @Before
    public void setup() throws MaizuoException {

        User user1 = new User();
        user1.setName("harvey");
        user1.setPassword("123456");
        user1.setId(1);
        user1.setSex((byte) 0);
        user1.setNickName("黄老司机");

        //模拟Service对应的方法入参及返参,对数据打桩
        given(this.loginService.login("harvey", "123456"))
                .willReturn(user1);

        User user2 = new User();
        user2.setName("gavin");
        user2.setPassword("123");
        user2.setId(2);
        user2.setSex((byte) 1);
        user2.setNickName("陌路人丁");
        given(this.loginService.login("gavin", "123"))
                .willReturn(user2);
    }

    @Test
    public void test() throws Exception{
        String result = this.getByUrl("/api/user/login?userName=harvey&password=123456");
        System.out.println("GET return ==> \n" + result);
    }

}
```
同理Dao的方法也可以这样做
```java
public class SimpleMockLoginDaoTest extends SimpleBaseTest {

    //声明需要模拟的Dao
    @MockBean
    private LoginDao loginDao;

    @Autowired
    private LoginService loginService;

    @Before
    public void setUp() {
        User user2 = new User();
        user2.setName("gavin");
        user2.setPassword("123");
        user2.setId(2);
        user2.setSex((byte) 1);
        user2.setNickName("陌路人丁");
        //模拟Dao的方法入参及返参,对数据打桩
        given(this.loginDao.Login("gavin", "123"))
                .willReturn(user2);
    }

    @Test
    public void test() throws Exception{
        User user = loginService.login("gavin","123");
        System.out.println("return User Object ==> \n" + JsonUtils.toJSON(user));
    }
}

```

#####@Mock、@Spy、@InjectMocks 的搭配使用
首先我们继承普通测试基类SimpleBaseTest【这里为什么不继承具备容器及读取application-local.yaml的MockMvcBaseTest基类呢？下面我会说明】；具体如下
```java
public class SimpleMockLoginDaoTest2 extends SimpleBaseTest {
    //创建一个实例
    @InjectMocks
    private LoginService loginService;

    //创建一个Mock
    //使用@Mock生成的类，所有方法都不是真实的方法，而且返回值都是NULL，需要做Mock打桩
    @Mock
    private LoginDao loginDao;

    @Before
    public void setUp(){

        //初始化,将@Mock、@Spy等对象注入 @InjectMocks对象中
        MockitoAnnotations.initMocks(this);

        //这个是不会走的，因为代码块包含了这个代码块
        User user1 = new User();
        user1.setName("harvey");
        user1.setPassword("123456");
        user1.setId(1);
        user1.setSex((byte) 0);
        user1.setNickName("黄老司机");
        when(loginDao.Login("harvey", "123456")).thenReturn(user1);

        User user2 = new User();
        user2.setName("gavin");
        user2.setPassword("123");
        user2.setId(2);
        user2.setSex((byte) 1);
        user2.setNickName("陌路人丁");
        when(loginDao.Login(any(String.class), any(String.class))).thenReturn(user2);
    }

    @Test
    public void test() throws Exception{
        User user = loginService.login("harvey", "123456");
        System.out.println("Service return ==> \n" + JsonUtils.toJSON(user));

    }
}
```
在本例子中我们吗、并没用到@Spy因为它是一个真实的类，比如@Spy private LoginDao loginDao;这里就算我们把基类换成了具备容器的MockMvcBaseTest还是会读不到JDBCTemplate的配置，原因可能就在MockitoAnnotations.initMocks(this);这行代码
另外关于Mockito之类的用法可以参考
http://blog.csdn.net/sdyy321/article/details/38757135/
【不建议使用@Spy】
####其他常规的测试  
建议使用MockMvcBaseTest ；否则将读不到application-local.yaml的配置
需要侧Service就用@Autowired注入即可
```java
public class SimpleTestDemo extends MockMvcBaseTest {

    @Autowired
    private LoginService loginService;

    @Test
    public void test() throws Exception{
        User user = loginService.login("harvey", "123456");
        System.out.println("Service return ==> \n" + JsonUtils.toJSON(user));
    }
}
```
至此Junit+mockito的使用及案例已完结    
###或者  git@gogs.miz.so:rose/springboot_demo.git



