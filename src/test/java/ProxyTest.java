import org.lixl.spring.MyInvocationHandler;
import test.UserService;
import test.UserServiceImpl;

/**
 * Created by Administrator on 7/4/2018.
 */
public class ProxyTest {

    public void testProxy() throws Throwable {
        UserService userService = new UserServiceImpl();

        MyInvocationHandler invocationHandler = new MyInvocationHandler(userService);
        UserService proxy = (UserService) invocationHandler.getProxy();
        proxy.add();
    }

    public static void main(String[] args) throws Throwable{
        (new ProxyTest()).testProxy();
    }
}
