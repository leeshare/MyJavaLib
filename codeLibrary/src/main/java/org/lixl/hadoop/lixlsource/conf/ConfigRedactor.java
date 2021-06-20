package org.lixl.hadoop.lixlsource.conf;

import org.lixl.hadoop.lixlsource.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.lixl.hadoop.lixlsource.fs.CommonConfigurationKeysPublic.HADOOP_SECURITY_SENSITIVE_CONFIG_KEYS;
import static org.lixl.hadoop.lixlsource.fs.CommonConfigurationKeysPublic.HADOOP_SECURITY_SENSITIVE_CONFIG_KEYS_DEFAULT;

/**
 * 工具 用于编写 敏感信息 当显示配置参数时
 */
public class ConfigRedactor {
    private static final String REDACTED_TEXT = "<redacted>";
    private List<Pattern> compiledPatterns;

    public ConfigRedactor(Configuration conf) {
        String sensitiveRegexList = conf.get(
                HADOOP_SECURITY_SENSITIVE_CONFIG_KEYS,
                HADOOP_SECURITY_SENSITIVE_CONFIG_KEYS_DEFAULT);
        List<String> sensitiveRegexes = Arrays.asList(StringUtils.getTrimmedStrings(sensitiveRegexList));
        compiledPatterns = new ArrayList<Pattern>();
        for(String regex : sensitiveRegexes) {
            Pattern p = Pattern.compile(regex);
            compiledPatterns.add(p);
        }
    }

    public String redact(String key, String value) {
        if(configIsSensitive(key)) {
            return REDACTED_TEXT;
        }
        return value;
    }

    /**
     * 匹配给定配置键反对样例和决定是否
     * @param key
     * @return
     */
    private boolean configIsSensitive(String key) {
        for(Pattern regex : compiledPatterns) {
            if(regex.matcher(key).find()) {
                return true;
            }
        }
        return false;
    }

}
