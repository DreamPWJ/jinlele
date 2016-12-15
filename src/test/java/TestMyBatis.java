


import com.alibaba.fastjson.JSON;
import com.jinlele.service.interfaces.ITestService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.Resource;

/**
 * @author pwj on 2016/6/23 0023.
 *         Junit4单元测试
 */

@RunWith(SpringJUnit4ClassRunner.class)        //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TestMyBatis {
    //日志类
    private static Logger logger = Logger.getLogger(TestMyBatis.class);
    /* private ApplicationContext ac = null;*/
    @Resource
    private ITestService testService ;

    /**
     * 注释掉的部分是不使用 Spring 时，一般情况下的一种测试方法  ；
     * 如果使用了 Spring 那么就可以使用注解的方式来引入配置文件和类，
     * 然后再将 service 接口对象注入，就可以进行测试
     */
/*	@Before
    public void before() {
		ac = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		userService = (ITestService) ac.getBean("userService");
	}*/
/*   @Test
    public void dataSourceEntry() {
        //至于程序里如何变换数据源,你可以在切面上检测哪些方法加入before方法,或者在程序里直接使用DataSourceEntry调用set方法
        //dataSourceEntry.set(DataSourceEntry.INTRANET_SOURCE);
    }*/
    @Test
    public void getAdminUser() {
        logger.info("=======================" + JSON.toJSONString(testService.getAdminUser(1)));
    }

    @Test
    public void pageResultMap() {
        logger.info("=======================" + JSON.toJSONString(testService.getUser()));
    }
}