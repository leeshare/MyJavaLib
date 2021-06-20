package org.lixl.hadoop.lixlsource.security.authentication.util;

import com.google.common.annotations.VisibleForTesting;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 三头犬名
 * 此类实现解析并处理Kerberos主要的名字。特别的，它分割它们并转化它们为本地操作系统名。
 */
@SuppressWarnings("all")
@InterfaceAudience.LimitedPrivate({"HDFS", "MapReduce"})
@InterfaceStability.Evolving
public class KerberosName {

    public static final String MECHANISM_HADOOP = "hadoop";

    public static final String MECHANISM_MIT = "mit";

    public static final String DEFAULT_MECHANISM = MECHANISM_HADOOP;

    private final String serviceName;

    private final String hostName;

    private final String realm;

    private static final Pattern nameParser = Pattern.compile("([^/@]+)(/([^/@]+))?(@([^/@]+))?");

    private static Pattern parameterPattern = Pattern.compile("([^$]*)(\\$(\\d*))?");

    private static final Pattern ruleParser =
            Pattern.compile("\\s*((DEFAULT)|(RULE:\\[(\\d*):([^\\]]*)](\\(([^)]*)\\))?"+
                    "(s/([^/]*)/([^/]*)/(g)?)?))/?(L)?");

    private static final Pattern nonSimplePattern = Pattern.compile("[/@]]");

    private static List<Rule> rules;

    /**
     * 如何评估 auth_to_local 规则
     */
    private static String ruleMechanism = null;

    private static String defaultRealm = null;

    @VisibleForTesting
    public static void resetDefaultRealm() {
        try {
            defaultRealm = KerberosUtil.getDefaultRealm();
        } catch (Exception ke) {
            //LOG.debug("重置默认领域失败，当前默认领域将继续使用.", ke);
        }
    }

    public KerberosName(String name) {
        Matcher match = nameParser.matcher(name);
        if(!match.matches()){
            if(name.contains("@")) {
                throw new IllegalArgumentException("Malformed kerberos name: " + name);
            } else {
                serviceName = name;
                hostName = null;
                realm = null;
            }
        } else {
            serviceName = match.group(1);
            hostName = match.group(3);
            realm = match.group(5);
        }

    }

    public static synchronized String getDefaultRealm() {
        if(defaultRealm == null){
            try {
                defaultRealm = KerberosUtil.getDefaultRealm();
            } catch (Exception ke){
                //LOG.debug("Kerberos krb5 configuration not found, setting default realm to empty");
                defaultRealm = "";
            }
        }
        return defaultRealm;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(serviceName);
        if(hostName != null){
            result.append('/');
            result.append(hostName);
        }
        if(realm != null){
            result.append('@');
            result.append(realm);
        }
        return result.toString();
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getHostName() {
        return hostName;
    }

    public String getRealm() {
        return realm;
    }

    private static class Rule {
        private final boolean isDefault;
        private final int numOfComponents;
        private final String format;
        private final Pattern match;
        private final Pattern fromPattern;
        private final String toPattern;
        private final boolean repeat;
        private final boolean toLowerCase;

        Rule() {
            isDefault = true;
            numOfComponents = 0;
            format = null;
            match = null;
            fromPattern = null;
            toPattern = null;
            repeat = false;
            toLowerCase = false;
        }
        Rule(int numOfComponents, String format, String match, String fromPattern, String toPattern, boolean repeat, boolean toLowerCase) {
            isDefault = false;
            this.numOfComponents = numOfComponents;
            this.format = format;
            this.match = match == null ? null : Pattern.compile(match);
            this.fromPattern = fromPattern == null ? null : Pattern.compile(fromPattern);
            this.toPattern = toPattern;
            this.repeat = repeat;
            this.toLowerCase = toLowerCase;
        }
        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            if(isDefault) {
                buf.append("DEFAULT");
            } else {
                buf.append("RULE:[");
                buf.append(numOfComponents);
                buf.append(':');
                buf.append(format);
                buf.append(']');
                if(match != null){
                    buf.append('(');
                    buf.append(match);
                    buf.append(')');
                }
                if(fromPattern != null) {
                    buf.append("s/");
                    buf.append(fromPattern);
                    buf.append('/');
                    buf.append(toPattern);
                    buf.append('/');
                    if(repeat) {
                        buf.append('g');
                    }
                }
                if(toLowerCase) {
                    buf.append("/L");
                }
            }
            return buf.toString();
        }

        static String replaceParameters(String format, String[] params) throws BadFormatString {
            Matcher match = parameterPattern.matcher(format);
            int start = 0;
            StringBuilder result = new StringBuilder();
            while (start < format.length() && match.find(start)) {
                result.append(match.group(1));
                String paramNum = match.group(3);
                if(paramNum != null){
                    try {
                        int num = Integer.parseInt(paramNum);
                        if(num < 0 || num > params.length) {
                            throw new BadFormatString("index " + num + " from " + format + " is outside of the valid range 0 to " + (params.length - 1));
                        }
                        result.append(params[num]);
                    } catch (NumberFormatException nfe) {
                        throw new BadFormatString("bad format in username mapping in " + paramNum, nfe);
                    }
                }
                start = match.end();
            }
            return result.toString();
        }

        static String replaceSubstitution(String base, Pattern from, String to, boolean repeat) {
            Matcher match = from.matcher(base);
            if(repeat) {
                return match.replaceAll(to);
            } else {
                return match.replaceFirst(to);
            }
        }

        String apply(String[] params, String ruleMechanism) throws IOException {
            String result = null;
            if(isDefault) {
                if(getDefaultRealm().equals(params[0])) {
                    result = params[1];
                }
            } else if(params.length - 1 == numOfComponents) {
                String base = replaceParameters(format, params);
                if(match == null || match.matcher(base).matches()) {
                    if(fromPattern == null){
                        result = base;
                    } else {
                        result = replaceSubstitution(base, fromPattern, toPattern, repeat);
                    }
                }
            }
            if(result != null && nonSimplePattern.matcher(result).find()
                    && ruleMechanism.equalsIgnoreCase(MECHANISM_HADOOP)) {
                throw new NoMatchingRule("Non-simple name " + result + " after auth_to_local rule " + this);
            }
            if(toLowerCase && result != null) {
                result = result.toLowerCase(Locale.ENGLISH);
            }
            return result;
        }

    }

    static List<Rule> parseRules(String rules) {
        List<Rule> result = new ArrayList<>();
        String remaining = rules.trim();
        while(remaining.length() > 0) {
            Matcher matcher = ruleParser.matcher(remaining);
            if(!matcher.lookingAt()) {
                throw new IllegalArgumentException("Invalid rule: " + remaining);
            }
            if(matcher.group(2) != null) {
                result.add(new Rule());
            } else {
                result.add(new Rule(Integer.parseInt(matcher.group(4)), matcher.group(5),
                        matcher.group(7), matcher.group(9), matcher.group(10),
                        "g".equals(matcher.group(11)), "L".equals(matcher.group(12))));
            }
            remaining = remaining.substring(matcher.end());
        }
        return result;
    }

    public static class BadFormatString extends IOException {
        BadFormatString(String msg) {
            super(msg);
        }
        BadFormatString(String msg, Throwable err) {
            super(msg, err);
        }
    }

    public static class NoMatchingRule extends IOException {
        NoMatchingRule(String msg) {
            super(msg);
        }
    }

    public String getShortName() throws IOException {
        String[] params;
        if(hostName == null) {
            if(realm == null){
                return serviceName;
            }
            params = new String[]{realm, serviceName};
        } else {
            params = new String[]{realm, serviceName, hostName};
        }
        String ruleMechanism = this.ruleMechanism;
        if(ruleMechanism == null && rules != null) {
            //LOG.warn("auth_to_local rule mechanism not set. Using default of " + DEFAULT_MECHANISM);
            ruleMechanism = DEFAULT_MECHANISM;
        }
        for(Rule r : rules) {
            String result = r.apply(params, ruleMechanism);
            if(result != null) {
                return result;
            }
        }
        if(ruleMechanism.equalsIgnoreCase(MECHANISM_HADOOP)) {
            throw new NoMatchingRule("No rules applied to " + toString());
        }
        return toString();
    }

    public static String getRules() {
        String ruleString = null;
        if(rules != null) {
            StringBuilder sb = new StringBuilder();
            for(Rule rule : rules) {
                sb.append(rule.toString()).append("\n");
            }
            ruleString = sb.toString().trim();
        }
        return ruleString;
    }

    public static boolean hasRulesBeanSet() {
        return rules != null;
    }

    public static boolean hasRuleMechanismBeanSet() {
        return ruleMechanism != null;
    }

    public static void setRules(String ruleString) {
        rules = (ruleString != null) ? parseRules(ruleString) : null;
    }

    public static void setRuleMechanism(String ruleMech) {
        if(ruleMech != null && (!ruleMech.equalsIgnoreCase(MECHANISM_HADOOP) && !ruleMech.equalsIgnoreCase(MECHANISM_MIT))) {
            throw new IllegalArgumentException("Invalid rule mechanism: " + ruleMech);
        }
        ruleMechanism = ruleMech;
    }

    public static String getRuleMechanism() {
        return ruleMechanism;
    }

    static void printRules() throws IOException {
        int i = 0;
        for(Rule r : rules) {
            System.out.println(++i + " " + r);
        }
    }


}
