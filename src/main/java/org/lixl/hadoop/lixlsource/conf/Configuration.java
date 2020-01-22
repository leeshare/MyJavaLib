package org.lixl.hadoop.lixlsource.conf;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.io.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class Configuration implements Iterable<Map.Entry<String, String>>, Writable {
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private static final Logger LOG_DEPRECATION = LoggerFactory.getLogger("org.lixl.hadoop.lixlsource.conf.Configuration.deprecation");
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
            if(resource instanceof String || !UserGroupInformation.)
        }

    }
}
