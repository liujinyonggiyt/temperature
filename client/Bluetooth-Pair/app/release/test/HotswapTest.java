
import java.lang.reflect.Method;

/**
 * Author:liujinyong
 * Date:2019/4/20
 * Time:12:04
 */
public class HotswapTest {
    public static void main(String[] args) throws Exception {
        while (true){
            swap();
            Thread.sleep(2000);
        }
    }

    private static void swap() throws Exception {
        CustomCl cl = new CustomCl("/swap", new String[]{"Foo"});
        Class cls = cl.loadClass("Foo");
        Object foo = cls.newInstance();

        Method m = foo.getClass().getMethod("sayHello", new Class[]{});
        m.invoke(foo);
    }
}
