package test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xinnet.entity.Book;
import com.xinnet.queue.producer.MessageProducer;


/**
 * @Description 
 * @author zhenping.zhou
 * @CreateTime 2015年12月7日 上午10:42:15
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {  
        "classpath:spring-appContext.xml"  
}) 
public class RabbitMqTest {

	@Resource
	private MessageProducer messageProducer;
	
	/*@Resource
	private OrderDaoImpl orderMapper;*/
	
    /*@Before
    public void setUp() throws Exception {
    	
		RemoteServiceFactory.init();

//        ApplicationContext ctx = ApplicationContextUtil.getInstence().getApplicationContext();
        //activityCouponFacadeImpl = (ActivityCouponFacade) ctx.getBean("activityCouponFacadeImpl");
//        activityCouponFacadeImpl = RemoteServiceFactory.getService(ActivityCouponFacade.class);
    }*/

    /**
     * 测试保存优惠券信息
     * @throws Exception
     */
	@Test  
    public void should_send_a_amq_message() throws Exception {  
        int a = 100;  
        while (a > 0) {  
        	Book book = new Book(a,"name--"+a,"author--"+a);
            messageProducer.sendMessage(book);  
            try {  
                //暂停一下，好让消息消费者去取消息打印出来  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
  
        }  
    } 
	
  
}
