package org.lixl.opensource.drools;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.lixl.opensource.drools.pojo.Person;
import org.lixl.opensource.drools.pojo.RuleKey;
import org.lixl.opensource.drools.pojo.School;

public class RulesConstraint {
    static KieContainer kc;
    static {
        KieServices kss = KieServices.get();
        kc = kss.getKieClasspathContainer();
    }

    public static void main(String[] args) {
        KieSession ks = kc.newKieSession("contains");
        Person p = new Person();
        p.setName("mike");
        p.setAge("30");
        p.setClassName("一班");
        School school = new School();
        school.setClassName("一班");

        p.setPersonInfo("男");
        RuleKey rk = new RuleKey();
        rk.setKeyValue("男人,男,Man,man,men,男孩,boy");

        ks.insert(p);
        ks.insert(school);
        ks.insert(rk);

        int count = ks.fireAllRules();
        System.out.println("一共执行了" + count + "条规则");



    }
}
