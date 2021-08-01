package org.lixl.opensource.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.lixl.opensource.drools.pojo.Person;

public class RulesHello {
    static KieContainer kieContainer;
    static {
        //通过单例创建KieServices
        KieServices kieServices = KieServices.Factory.get();
        //创建一个KieContainer来读取路径中需要构建的文件，是KIE容器
        kieContainer = kieServices.getKieClasspathContainer();
    }
    public static void main(String[] args) {
        //由于 KieSession创建成本低，所以优于  KieModule 和 KieBase
        KieSession kieSession = kieContainer.newKieSession("testhelloworld");

        Person person = new Person();
        person.setName("mike");
        person.setAge("30");
        kieSession.insert(person);

        int count = kieSession.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        kieSession.dispose();
    }
}
