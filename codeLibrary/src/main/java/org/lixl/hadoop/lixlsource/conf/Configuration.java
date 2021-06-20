package org.lixl.hadoop.lixlsource.conf;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.collections.map.UnmodifiableMap;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.fs.CommonConfigurationKeys;
import org.lixl.hadoop.lixlsource.io.Writable;
import org.lixl.hadoop.lixlsource.security.UserGroupInformation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class Configuration implements Iterable<Map.Entry<String, String>>, Writable {
    //private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    //private static final Logger LOG_DEPRECATION = LoggerFactory.getLogger("org.lixl.hadoop.lixlsource.conf.Configuration.deprecation");
    private static final Set<String> TAGS = new HashSet<>();
    private boolean quietmode = true;
    private static final String DEFAULT_STRING_CHECK = "testing for empty default value";
    private static boolean restrictSystemPropsDefault = false;
    private boolean restrictSystemProps = restrictSystemPropsDefault;
    private boolean allowNullValueProperties = false;

    private static class Resource {
        private final Object resource;
        private final String name;
        private final boolean restrictParser;

        public Resource(Object resource) {
            this(resource, resource.toString());
        }
        public Resource(Object resource, boolean useRestrictedParser) {
            this(resource, resource.toString(), useRestrictedParser);
        }
        public Resource(Object resource, String name) {
            this(resource, name, getRestrictParserDefault(resource));
        }
        public Resource(Object resource, String name, boolean restrictParser) {
            this.resource = resource;
            this.name = name;
            this.restrictParser = restrictParser;
        }
        public String getName() {
            return name;
        }
        public Object getResource() {
            return resource;
        }
        public boolean isParserRestricted() {
            return restrictParser;
        }
        @Override
        public String toString() {
            return name;
        }
        private static boolean getRestrictParserDefault(Object resource) {
            if(resource instanceof String || !UserGroupInformation.isInitialized()) {
                return false;
            }
            UserGroupInformation user;
            try {
                user = UserGroupInformation.getCurrentUser();
            } catch (IOException e) {
                throw new RuntimeException("Unable to determine current user", e);
            }
            //return user.getRealUser() != null;
            return false;
        }
    }


    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        Map<String, String> result = new HashMap<>();
        for(Map.Entry<Object, Object> item : getProps().entrySet()) {
            //...
        }
        return result.entrySet().iterator();
    }

    @Override
    public void write(DataOutput out) throws IOException {

    }

    @Override
    public void readFields(DataInput in) throws IOException {

    }

    private static class DeprecatedKeyInfo {
        private final String[] newKeys;
        private final String customMessage;
        private final AtomicBoolean accessed = new AtomicBoolean(false);

        DeprecatedKeyInfo(String[] newKeys, String customMessage) {
            this.newKeys = newKeys;
            this.customMessage = customMessage;
        }
    }

    private static DeprecationDelta[] defaultDeprecations = new DeprecationDelta[] {
            new DeprecationDelta("topology.script.file.name", CommonConfigurationKeys.NET_TOPOLOGY_SCRIPT_FILE_NAME_KEY),
            new DeprecationDelta("topology.script.number.args", CommonConfigurationKeys.NET_TOPOLOGY_SCRIPT_NUMBER_ARGS_KEY),
            new DeprecationDelta("hadoop.configured.node.mapping", CommonConfigurationKeys.NET_TOPOLOGY_CONFIGURED_NODE_MAPPING_KEY),
            new DeprecationDelta("topology.node.switch.mapping.impl", CommonConfigurationKeys.NET_TOPOLOGY_NODE_SWITCH_MAPPING_IMPL_KEY),
            new DeprecationDelta("dfs.df.interval", CommonConfigurationKeys.FS_DF_INTERVAL_KEY),
            new DeprecationDelta("fs.default.name", CommonConfigurationKeys.FS_DEFAULT_NAME_KEY),
            new DeprecationDelta("dfs.unmaskmode", CommonConfigurationKeys.FS_PERMISSIONS_UMASK_KEY),
            new DeprecationDelta("dfs.nfs.exports.allowed.host", CommonConfigurationKeys.NFS_EXPORTS_ALLOWED_HOSTS_KEY)
    };

    public static class DeprecationDelta {
        private final String key;
        private final String[] newKeys;
        private final String customMessage;

        DeprecationDelta(String key, String[] newKeys, String customMessage) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(newKeys);
            Preconditions.checkArgument(newKeys.length > 0);
            this.key = key;
            this.newKeys = newKeys;
            this.customMessage = customMessage;
        }

        public DeprecationDelta(String key, String newKey, String customMessage) {
            this(key, new String[]{newKey}, customMessage);
        }

        public DeprecationDelta(String key, String newKey) {
            this(key, new String[]{newKey}, null);
        }

        public String getKey() {
            return key;
        }

        public String[] getNewKeys() {
            return newKeys;
        }

        public String getCustomMessage() {
            return customMessage;
        }
    }

    private static class DeprecationContext {
        private final Map<String, DeprecatedKeyInfo> deprecatedKeyMap;
        private final Map<String, String> reverseDeprecatedKeyMap;

        @SuppressWarnings("unchecked")
        DeprecationContext(DeprecationContext other, DeprecationDelta[] deltas) {
            HashMap<String, DeprecatedKeyInfo> newDeprecatedKeyMap = new HashMap<String, DeprecatedKeyInfo>();
            HashMap<String, String> newReverseDeprecatedKeyMap = new HashMap<String, String>();
            if(other != null) {
                for(Map.Entry<String, DeprecatedKeyInfo> entry : other.deprecatedKeyMap.entrySet()) {
                    newDeprecatedKeyMap.put(entry.getKey(), entry.getValue());
                }
                for(Map.Entry<String, String> entry: other.reverseDeprecatedKeyMap.entrySet()) {
                    newReverseDeprecatedKeyMap.put(entry.getKey(), entry.getValue());
                }
            }
            for(DeprecationDelta delta : deltas) {
                if(!newDeprecatedKeyMap.containsKey(delta.getKey())) {
                    DeprecatedKeyInfo newKeyInfo = new DeprecatedKeyInfo(delta.getNewKeys(), delta.getCustomMessage());
                    newDeprecatedKeyMap.put(delta.key, newKeyInfo);
                    for(String newKey : delta.getNewKeys()) {
                        newReverseDeprecatedKeyMap.put(newKey, delta.key);
                    }
                }
            }
            this.deprecatedKeyMap = UnmodifiableMap.decorate(newDeprecatedKeyMap);
            this.reverseDeprecatedKeyMap = UnmodifiableMap.decorate(newReverseDeprecatedKeyMap);
        }

        Map<String, DeprecatedKeyInfo> getDeprecatedKeyMap() {
            return deprecatedKeyMap;
        }
        Map<String, String> getReverseDeprecatedKeyMap() {
            return reverseDeprecatedKeyMap;
        }
    }

    private static AtomicReference<DeprecationContext> deprecationContext =
            new AtomicReference<DeprecationContext>(new DeprecationContext(null, defaultDeprecations));




    public String get(String name) {
        String[] names = handleDeprecation(deprecationContext.get(), name);
        String result = null;
        for(String n : names) {
            result = substituteVars(getProps().getProperty(n));
        }
        return result;
    }


    public String getTrimmed(String name) {
        String value = get(name);
        if(null == value) {
            return null;
        } else {
            return value.trim();
        }
    }


    public String get(String name, String defaultValue) {
        String[] names = handleDeprecation(deprecationContext.get(), name);
        String result = null;
        for(String n : names) {
            result = substituteVars(getProps().getProperty(n, defaultValue));
        }
        return result;
    }
    /**
     * 获取name属性作为一个int的值。
     * @param name
     * @param defaultValue
     * @return
     */
    public int getInt(String name, int defaultValue) {
        String valueString = getTrimmed(name);
        if(valueString == null)
            return defaultValue;
        String hexString = getHexDigits(valueString);
        if(hexString != null)
            return Integer.parseInt(hexString, 16);
        return Integer.parseInt(valueString);
    }

    private String getHexDigits(String value) {
        boolean negative = false;
        String str = value;
        String hexString = null;
        if(value.startsWith("-")) {
            negative = true;
            str = value.substring(1);
        }
        if(str.startsWith("0x") || str.startsWith("0x")) {
            hexString = str.substring(2);
            if(negative) {
                hexString = "-" + hexString;
            }
            return hexString;
        }
        return null;
    }


    public static class IntegerRanges implements Iterable<Integer> {
        private static class Range {
            int start;
            int end;
        }

        private static class RangeNumberIterator implements Iterator<Integer> {
            Iterator<Range> internal;
            int at;
            int end;

            public RangeNumberIterator(List<Range> ranges) {
                if(ranges != null) {
                    internal = ranges.iterator();
                }
                at = -1;
                end = -2;
            }

            @Override
            public boolean hasNext() {
                if(at <= end) {
                    return true;
                } else if(internal != null) {
                    return internal.hasNext();
                }
                return false;
            }

            @Override
            public Integer next() {
                if(at <= end) {
                    at++;
                    return at - 1;
                } else if(internal != null) {
                    Range found = internal.next();
                    if(found != null) {
                        at = found.start;
                        end = found.end;
                        at++;
                        return at - 1;
                    }
                }
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        List<Range> ranges = new ArrayList<>();

        public IntegerRanges() {}

        public IntegerRanges(String newValue) {
            StringTokenizer itr = new StringTokenizer(newValue, ",");
            while(itr.hasMoreTokens()) {
                String rng = itr.nextToken().trim();
                String[] parts =rng.split("-", 3);
                if(parts.length < 1 || parts.length > 2) {
                    throw new IllegalArgumentException("整形范围形式错误：" + rng);
                }
                Range r = new Range();
                r.start = convertToInt(parts[0], 0);
                if(parts.length == 2) {
                    r.end = convertToInt(parts[1], Integer.MAX_VALUE);
                } else {
                    r.end = r.start;
                }
                if(r.start > r.end){
                    throw new IllegalArgumentException("整形范围从" + r.start + "到" + r.end + "不合法");
                }
                ranges.add(r);
            }
        }

        private static int convertToInt(String value, int defaultValue) {
            String trim = value.trim();
            if(trim.length() == 0) {
                return defaultValue;
            }
            return Integer.parseInt(trim);
        }


        //未完 待续


        @Override
        public Iterator<Integer> iterator() {
            return new RangeNumberIterator(ranges);
        }

    }

    List<IntegerRanges.Range> ranges = new ArrayList<IntegerRanges.Range>();

    private volatile Map<String, String[]> updatingResource;

    private static final CopyOnWriteArrayList<String> defaultResources = new CopyOnWriteArrayList<>();

    private ArrayList<Resource> resources = new ArrayList<Resource>();

    private Properties properties;
    private Properties overlay;
    private ClassLoader classLoader;
    {
        classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader == null) {
            classLoader = Configuration.class.getClassLoader();
        }
    }

    protected synchronized Properties getProps() {
        if(properties == null) {
            properties = new Properties();
            Map<String, String[]> backup = updatingResource != null ? new ConcurrentHashMap<>(updatingResource) : null;
            loadResources(properties, resources, quietmode);
        }

        return null;
    }


    private void loadResources(Properties properties, ArrayList<Resource> resources, boolean quiet) {
        if(loadDefaults) {
            for(String resource : defaultResources) {
                loadResource(properties, new Resource(resource, false), quiet);
            }
        }
        for(int i = 0; i < resources.size(); i++) {
            Resource ret = loadResource(properties, resources.get(i), quiet);
            if(ret != null) {
                resources.set(i, ret);
            }
        }
        this.removeUndeclaredTags(properties);
    }

    private Resource loadResource(Properties properties, Resource wrapper, boolean quiet) {
        return null;
    }

    private final Map<String, Properties> propertyTagsMap = new ConcurrentHashMap<>();

    private void removeUndeclaredTags(Properties prop) {
        if(prop.contains(CommonConfigurationKeys.HADOOP_SYSTEM_TAGS)) {
            String systemTags = getProps().getProperty(CommonConfigurationKeys.HADOOP_SYSTEM_TAGS);
            Arrays.stream(systemTags.split(",")).forEach(tag -> TAGS.add(tag));
        }

        if(prop.containsKey(CommonConfigurationKeys.HADOOP_CUSTOM_TAGS)) {
            String customTags = prop.getProperty(CommonConfigurationKeys.HADOOP_CUSTOM_TAGS);
            Arrays.stream(customTags.split(",")).forEach(tag -> TAGS.add(tag));
        }
        Set undeclaredTags = propertyTagsMap.keySet();
        if(undeclaredTags.retainAll(TAGS)){
            //LOG.info("移除未申报的标签:");
        }
    }

    /**
     * 读取传入的值作为标签保存在map中晚些时检索
     * @param attributeValue
     * @param confName
     * @param confValue
     * @param confSource
     */
    private void readTagFromConfig(String attributeValue, String confName, String confValue, String[] confSource) {
        for(String tagStr : attributeValue.split(",")) {
            tagStr = tagStr.trim();
            try {
                if(confValue == null) {
                    confValue = "";
                }
                if(propertyTagsMap.containsKey(tagStr)) {
                    propertyTagsMap.get(tagStr).setProperty(confName, confValue);
                } else {
                    Properties props = new Properties();
                    props.setProperty(confName, confValue);
                    propertyTagsMap.put(tagStr, props);
                }
            } catch (Exception ex) {
                //LOG.trace("Tag '{}' for property:{} Source:{}", tagStr, confName, confSource, ex);
            }
        }
    }

    private void overlay(Properties to, Properties from) {
        for(Map.Entry<Object, Object> entry : from.entrySet()) {
            to.put(entry.getKey(), entry.getValue());
        }
    }

    private Set<String> finalParameters = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    private boolean loadDefaults = true;

    private void loadProperty(Properties properties, String name, String attr, String value,
                              boolean finalParameter, String[] source) {
        if(value != null || allowNullValueProperties) {
            if(value == null) {
                value = DEFAULT_STRING_CHECK;
            }
            if(!finalParameters.contains(attr)) {
                properties.setProperty(attr, value);
                if(source != null) {
                    putIntoUpdatingResource(attr, source);
                }
            }else {
                checkForOverride(this.properties, name, attr, value);
                if(this.properties != properties) {
                    checkForOverride(properties, name, attr, value);
                }
            }
        }
        if(finalParameter && attr != null) {
            finalParameters.add(attr);
        }
    }

    private void checkForOverride(Properties properties, String name, String attr, String value) {
        String propertyValue = properties.getProperty(attr);
        if(propertyValue != null && !propertyValue.equals(value)) {
            //LOG.warn(name + ":一个试图推翻final变量：" + attr + "; 忽视。");
        }
    }

    public void writeXml(OutputStream out) throws IOException {
        writeXml(new OutputStreamWriter(out, "UTF-8"));
    }
    public void writeXml(Writer out) throws IOException {
        writeXml(null, out);
    }

    public void writeXml(String propertyName, Writer out) throws IOException, IllegalArgumentException {
        Document doc = asXmlDocument(propertyName);
        try {
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(out);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();

            transformer.transform(source, result);
        } catch (TransformerException te) {
            throw new IOException(te);
        }
    }

    private synchronized Document asXmlDocument(String propertyName) throws IOException, IllegalArgumentException {
        Document doc;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException pe) {
            throw new IOException(pe);
        }

        Element conf = doc.createElement("configuration");
        doc.appendChild(conf);
        conf.appendChild(doc.createTextNode("\n"));
        handleDeprecation();

        if(!Strings.isNullOrEmpty(propertyName)) {
            if(!properties.containsKey(propertyName)) {
                throw new IllegalArgumentException("Property " + propertyName + " not found");
            } else {
                appendXMLProperty(doc, conf, propertyName);
                conf.appendChild(doc.createTextNode("\n"));
            }
        } else {
            for(Enumeration<Object> e = properties.keys(); e.hasMoreElements();) {
                appendXMLProperty(doc, conf, (String)e.nextElement());
                conf.appendChild(doc.createTextNode("\n"));
            }
        }
        return doc;
    }

    private synchronized void appendXMLProperty(Document doc, Element conf, String propertyName) {
        if(!Strings.isNullOrEmpty(propertyName)) {
            String value = properties.getProperty(propertyName);
            if(value != null) {
                Element propNode = doc.createElement("name");
                conf.appendChild(propNode);

                Element nameNode = doc.createElement("name");
                nameNode.appendChild(doc.createTextNode(propertyName));
                propNode.appendChild(nameNode);

                Element valueNode = doc.createElement("value");
                valueNode.appendChild(doc.createTextNode(properties.getProperty(propertyName)));
                propNode.appendChild(valueNode);

                Element finalNode = doc.createElement("final");
                finalNode.appendChild(doc.createTextNode(String.valueOf(finalParameters.contains(propertyName))));
                propNode.appendChild(finalNode);

                if(updatingResource != null) {
                    String[] sources = updatingResource.get(propertyName);
                    if(sources != null) {
                        for(String s : sources) {
                            Element sourceNode = doc.createElement("source");
                            sourceNode.appendChild(doc.createTextNode(s));
                            propNode.appendChild(sourceNode);
                        }
                    }
                }
            }
        }
    }

    /**
     * 写特性和他们的属性（final和resource）到给定的Writer
     * 当peopertyName不为空，且属性在configuration中存在，则输出形式将是：
     *      property: { key: key1, value: value1, isFinal: key1.isFinal, resource: key1.resource } //都带双引号
     * @param config
     * @param propertyName
     * @param out
     * @throws IOException
     */
    public static void dumpConfiguration(Configuration config, String propertyName, Writer out) throws IOException {
        if(Strings.isNullOrEmpty(propertyName)) {
            dumpConfiguration(config, out);
        } else if(Strings.isNullOrEmpty(config.get(propertyName))){
            throw new IllegalArgumentException("Property " + propertyName + " not found");
        } else {
            JsonFactory dumpFactory = new JsonFactory();
            JsonGenerator dumpGenerator = dumpFactory.createGenerator(out);
            dumpGenerator.writeStartObject();
            dumpGenerator.writeFieldName("property");
            appendJSONProperty(dumpGenerator, config, propertyName, new ConfigRedactor(config));
        }

    }

    public static void dumpConfiguration(Configuration config, Writer out) throws IOException {
        //...
    }


    private static void appendJSONProperty(JsonGenerator jsonGen, Configuration config, String name, ConfigRedactor redactor) throws IOException {
        //...
    }

    private void handleDeprecation() {
        //...
    }

    private String[] handleDeprecation(DeprecationContext deprecations, String name) {
        //...
        return null;
    }

    private void putIntoUpdatingResource(String key, String[] value) {
        //...
    }

    private String substituteVars(String expr) {
        if (expr == null) {
            return null;
        }
        //...
        return "";
    }

}
