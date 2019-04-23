
import java.io.*;
import java.util.HashSet;

/**
 * Author:liujinyong
 * Date:2019/4/20
 * Time:11:53
 */
public class CustomCl extends ClassLoader {
    private String basedir;
    private HashSet<String> dynaclazns;
    public CustomCl(String basedir, String[] clazns) throws Exception {
        super(null);//指定父类加载器为null
        this.basedir = basedir;
        dynaclazns = new HashSet<>();
        loadClassByMe(clazns);
    }

    private void loadClassByMe(String[] clazns) throws Exception {
        for(int i=0;i<clazns.length;++i){
            loadDirectly(clazns[i]);
            dynaclazns.add(clazns[i]);
        }
    }

    private Class loadDirectly(String name) throws Exception {
        Class cls = null;
        StringBuilder sb = new StringBuilder(basedir);
        String className = name.replace(".", File.separator)+".class";
        sb.append(File.separator+className);
        File classF = new File(sb.toString());
        cls = instanteClass(name, new FileInputStream(classF), classF.length());
        return cls;
    }

    private Class instanteClass(String name, InputStream in, long len) throws IOException {
        byte[] raw = new byte[(int) len];
        in.read(raw);
        in.close();
        return defineClass(raw, 0, raw.length);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class cls = null;
        cls = findLoadedClass(name);
        if(!this.dynaclazns.contains(name)&&cls==null){
            cls = getSystemClassLoader().loadClass(name);
        }
        if(cls == null){
            throw new ClassNotFoundException();
        }
        if(resolve){
            resolveClass(cls);
        }
        return cls;
    }

}
