package org.lixl.hadoop.lixlsource.util;

import com.google.common.base.Preconditions;
import com.google.common.net.InetAddresses;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.fs.Path;
import org.lixl.hadoop.lixlsource.net.NetUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@InterfaceAudience.Private
@InterfaceStability.Unstable
public class StringUtils {
    /**
     * 优先级 of StringUtils 关闭钩子
     */
    public static final int SHUTDOWN_HOOK_PRIORITY = 0;
    /**
     * 壳环境变量
     */
    public static final Pattern SHELL_ENV_VAR_PATTERN = Pattern.compile("\\$([A-Za-z_]{1}[A-Za-z0-9_]*)");
    /**
     * Windows环境变量
     */
    public static final Pattern WIN_ENV_VAR_PATTERN = Pattern.compile("%(.*?)%");

    public static final Pattern ENV_VAR_PATTERN = Shell.WINDOWS ? WIN_ENV_VAR_PATTERN : SHELL_ENV_VAR_PATTERN;

    public static String stringifyExecption(Throwable e) {
        StringWriter stm = new StringWriter();
        PrintWriter wrt = new PrintWriter(stm);
        e.printStackTrace(wrt);
        wrt.close();
        return stm.toString();
    }

    public static String simpleHostname(String fullHostname) {
        if(InetAddresses.isInetAddress(fullHostname)) {
            return fullHostname;
        }
        int offset = fullHostname.indexOf('.');
        if(offset != -1) {
            return fullHostname.substring(0, offset);
        }
        return fullHostname;
    }

    @Deprecated
    public static String humanReadableInt(long number) {
        return TraditionalBinaryPrefix.long2String(number, "", 1);
    }

    public static String format(final String format, final Object... objects) {
        return String.format(Locale.ENGLISH, format, objects);
    }

    public static String formatPercent(double fraction, int decimalPlaces) {
        return format("%." + decimalPlaces + "f%%", fraction*100);
    }

    public static String arrayToString(String[] strs) {
        if(strs.length == 0) {
            return "";
        }
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(strs[0]);
        for(int idx = 1; idx < strs.length; idx++) {
            sbuf.append(",");
            sbuf.append(strs[idx]);
        }
        return sbuf.toString();
    }

    public static String byteToHexString(byte[] bytes, int start, int end) {
        if(bytes == null) {
            throw new IllegalArgumentException("bytes == null");
        }
        StringBuilder s = new StringBuilder();
        for(int i = start; i < end; i++) {
            s.append(format("%02x", bytes[i]));
        }
        return s.toString();
    }

    public static String byteToHexString(byte bytes[]) {
        return byteToHexString(bytes, 0, bytes.length);
    }

    public static String byteToHexString(byte b) {
        return byteToHexString(new byte[]{b});
    }

    public static byte[] hexStringToByte(String hex) {
        byte[] bts = new byte[hex.length() / 2];
        for(int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }

    public static String uriToString(URI[] uris) {
        if(uris == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder(uris[0].toString());
        for(int i = 1; i < uris.length; i++) {
            ret.append(",");
            ret.append(uris[i].toString());
        }
        return ret.toString();
    }

    public static URI[] stringToURI(String[] str) {
        if(str == null) {
            return null;
        }
        URI[] uris = new URI[str.length];
        for(int i = 0; i < str.length; i++) {
            try {
                uris[i] = new URI(str[i]);
            } catch (URISyntaxException ur) {
                throw new IllegalArgumentException("失败建立uri " + str[i], ur);
            }
        }
        return uris;
    }

    public static Path[] stringToPath(String[] str) {
        if(str == null) {
            return null;
        }
        Path[] p = new Path[str.length];
        for(int i = 0; i < str.length; i++) {
            p[i] = new Path(str[i]);
        }
        return p;

    }

    public static String formatTimeDiff(long finishTime, long startTime) {
        long timeDiff = finishTime - startTime;
        return formatTime(timeDiff);
    }

    public static String formatTime(long timeDiff) {
        StringBuilder buf = new StringBuilder();
        long hours = timeDiff / (60*60*1000);
        long rem = (timeDiff % (60*60*1000));
        long minutes = rem / (60*1000);
        rem = rem % (60*1000);
        long seconds = rem / 1000;

        if(hours != 0) {
            buf.append(hours);
            buf.append("hrs, ");
        }
        if(minutes != 0) {
            buf.append(minutes);
            buf.append("mins, ");
        }
        buf.append(seconds);
        buf.append("sec");
        return buf.toString();
    }

    public static String formatTimeSortable(long timeDiff) {
        StringBuilder buf = new StringBuilder();
        long hours = timeDiff / (60*60*1000);
        long rem = (timeDiff / (60*60*1000));
        long minutes = rem / (60 * 1000);
        rem = rem % (60*1000);
        long seconds = rem / 1000;

        if(hours > 99) {
            hours = 99;
            minutes = 59;
            seconds = 59;
        }

        buf.append(String.format("%02d", hours));
        buf.append("hrs, ");
        buf.append(String.format("%02d", minutes));
        buf.append("min, ");
        buf.append(String.format("%02d", seconds));
        buf.append("sec");
        return buf.toString();
    }

    public static String getFormattedTimeWithDiff(FastDateFormat dateFormat, long finishTime, long startTime) {
        String formattedFinishTime = dateFormat.format(finishTime);
        return getFormattedTimeWithDiff(formattedFinishTime, finishTime, startTime);
    }

    public static String getFormattedTimeWithDiff(String formattedFinishTime, long finishTime, long startTime) {
        StringBuilder buf = new StringBuilder();
        if(0 != finishTime) {
            buf.append(formattedFinishTime);
            if(0 != startTime) {
                buf.append(" (" + formatTimeDiff(finishTime, startTime) + ")");
            }
        }
        return buf.toString();
    }

    public static String[] getStrings(String str) {
        String delim = ",";
        return getStrings(str, delim);
    }

    public static String[] getStrings(String str, String delim) {
        Collection<String> values = getStringCollection(str, delim);
        if(values.size() == 0) {
            return null;
        }
        return values.toArray(new String[values.size()]);
    }

    public static Collection<String> getStringCollection(String str) {
        String delim = ",";
        return getStringCollection(str, delim);
    }

    public static Collection<String> getStringCollection(String str, String delim) {
        List<String> values = new ArrayList<String>();
        if(str == null) {
            return values;
        }
        StringTokenizer tokenizer = new StringTokenizer(str, delim);
        while(tokenizer.hasMoreTokens()) {
            values.add(tokenizer.nextToken());
        }
        return values;
    }

    public static Collection<String> getTrimmedStringCollection(String str) {
        Set<String> set = new LinkedHashSet<String>(
                Arrays.asList(getTrimmedStrings(str)));
        set.remove("");
        return set;
    }

    public static String[] getTrimmedStrings(String str) {
        if(null == str || str.trim().isEmpty()) {
            return emptyStringArray;
        }
        return str.trim().split("\\s*[,\n]\\s*");
    }

    final public static String[] emptyStringArray = {};
    final public static char COMMA = ',';
    final public static String COMMA_STR = ",";
    final public static char ESCAPE_CHAR = '\\';

    public static String[] split(String str) {
        return split(str, ESCAPE_CHAR, COMMA);
    }

    public static String[] split(String str, char escapeChar, char separator) {
        if(str == null) {
            return null;
        }
        ArrayList<String> strList = new ArrayList<String>();
        StringBuilder split = new StringBuilder();
        int index = 0;
        while((index = findNext(str, separator, escapeChar, index, split)) >= 0) {
            ++index;
            strList.add(split.toString());
            split.setLength(0);
        }
        strList.add(split.toString());
        int last = strList.size();
        while(--last >= 0 && "".equals(strList.get(last))) {
            strList.remove(last);
        }
        return strList.toArray(new String[strList.size()]);
    }

    public static String[] split(String str, char separator) {
        if(str.isEmpty()) {
            return new String[]{""};
        }
        ArrayList<String> strList = new ArrayList<String>();
        int startIndex = 0;
        int nextIndex = 0;
        while((nextIndex = str.indexOf(separator, startIndex)) != -1) {
            strList.add(str.substring(startIndex, nextIndex));
            startIndex = nextIndex + 1;
        }
        strList.add(str.substring(startIndex));
        int last = strList.size();
        while(--last >= 0 && "".equals(strList.get(last))) {
            strList.remove(last);
        }
        return strList.toArray(new String[strList.size()]);
    }

    public static int findNext(String str, char separator, char escapeChar, int start, StringBuilder split) {
        int numPreEscapes = 0;
        for(int i = start; i < str.length(); i++) {
            char curChar = str.charAt(i);
            if(numPreEscapes == 0 && curChar == separator) {
                return i;
            } else {
                split.append(curChar);
                numPreEscapes = (curChar == escapeChar) ? (++numPreEscapes) % 2 : 0;
            }
        }
        return -1;
    }

    public static String escapeString(String str) {
        return escapeString(str, ESCAPE_CHAR, COMMA);
    }

    private static boolean hasChar(char[] chars, char character) {
        for(char target : chars) {
            if(character == target) {
                return true;
            }
        }
        return false;
    }

    public static String escapeString(String str, char escapeChar, char charToEscape) {
        return escapeString(str, escapeChar, new char[]{charToEscape});
    }

    public static String escapeString(String str, char escapeChar, char[] charsToEscape) {
        if(str == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            char curChar = str.charAt(i);
            if(curChar == escapeChar || hasChar(charsToEscape, curChar)) {
                result.append(escapeChar);
            }
            result.append(curChar);
        }
        return result.toString();
    }

    public static String unEscapeString(String str) {
        return unEscapeString(str, ESCAPE_CHAR, COMMA);
    }
    public static String unEscapeString(String str, char escapeChar, char charToEscape) {
        return unEscapeString(str, escapeChar, new char[]{charToEscape});
    }

    public static String unEscapeString(String str, char escapeChar, char[] charsToEscape) {
        if(str == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(str.length());
        boolean hasPreEscape = false;
        for(int i = 0; i < str.length(); i++) {
            char curChar = str.charAt(i);
            if(hasPreEscape) {
                if(curChar != escapeChar && !hasChar(charsToEscape, curChar)) {
                    throw new IllegalArgumentException("Illegal escaped string " + str + " unescaped " + escapeChar + " at " + (i-1));
                }
                result.append(curChar);
                hasPreEscape = false;
            } else {
                if(hasChar(charsToEscape, curChar)) {
                    throw new IllegalArgumentException("Illegal escaped string " + str + " unescaped " + escapeChar + " at " + i);
                } else if(curChar == escapeChar) {
                    hasPreEscape = true;
                } else {
                    result.append(curChar);
                }
            }
        }
        if(hasPreEscape) {
            throw new IllegalArgumentException("Illegal escaped string " + str + ", not expecting " + escapeChar + " in the end." );
        }
        return result.toString();
    }

    public static String toStartupShutdownString(String prefix, String[] msg) {
        StringBuilder b = new StringBuilder(prefix);
        b.append("\n/****************");
        for(String s : msg) {
            b.append("\n").append(prefix).append(s);
        }
        b.append("\n****************/");
        return b.toString();
    }

    public static void startupShutdownMessage(Class<?> clazz, String[] args, final Log LOG) {
        startupShutdownMessage(clazz, args, LogAdapter.create(LOG));
    }

    //public static void startupShutdownMessage(Class<?> clazz, String[] args, final org.slf4j.Logger LOG) {
        //startupShutdownMessage(clazz, args, LogAdapter.create(LOG));
    //}

    static void startupShutdownMessage(Class<?> clazz, String[] args, final LogAdapter LOG) {
        final String hostname = NetUtils.getHostname();
        final String classname = clazz.getSimpleName();
        LOG.info(createStartupShutdownMessage(classname, hostname, args));

        if(SystemUtils.IS_OS_UNIX) {
            try {
                SignalLogger.INSTANCE.register(LOG);
            } catch (Throwable t) {
                LOG.warn("失败 注册任何UNIX信号日志： ", t);
            }
        }
        ShutdownHookManager.get().addShutdownHook(
                new Runnable() {
                    @Override
                    public void run() {
                        LOG.info(toStartupShutdownString("SHUTDOWN_MSG: ", new String[]{"Shutting down " + classname + " at " + hostname}));
                    }
                }, SHUTDOWN_HOOK_PRIORITY
        );
    }

    public static String createStartupShutdownMessage(String classname, String hostname, String[] args) {
        return toStartupShutdownString("STARTUP_MSG: ", new String[] {
                "Starting " + classname,
                "  host = " + hostname,
                "  args = " + (args != null ? Arrays.asList(args) : new ArrayList<>()),
                "  version = " + VersionInfo.getVersion(),
                "  classpath = " + System.getProperty("java.class.path"),

                "  build = " + VersionInfo.getUrl() + " -r "
                    + VersionInfo.getRevision()
                    + "; compiled by '" + VersionInfo.getUser() + "' on " + VersionInfo.getDate(),

                "  java = " + System.getProperty("java.version")

        });
    }



    public enum TraditionalBinaryPrefix {
        KILO(10),
        MEGA(KILO.bitShift + 10),
        GIGA(MEGA.bitShift + 10),
        TERA(GIGA.bitShift + 10),
        PETA(TERA.bitShift + 10),
        EXA (PETA.bitShift + 10);

        public final long value;
        public final char symbol;
        public final int bitShift;
        public final long bitMask;

        private TraditionalBinaryPrefix(int bitShift) {
            this.bitShift = bitShift;
            this.value = 1L << bitShift;
            this.bitMask = this.value - 1L;
            this.symbol = toString().charAt(0);
        }

        public static TraditionalBinaryPrefix valueOf(char symbol) {
            symbol = Character.toUpperCase(symbol);
            for(TraditionalBinaryPrefix prefix : TraditionalBinaryPrefix.values()) {
                if(symbol == prefix.symbol) {
                    return prefix;
                }
            }
            throw new IllegalArgumentException("Unknown symbol '" + symbol + "'");
        }

        /**
         * 转化一个string为long。
         *
         * 例如： "-1230k"将转换为 -1230 * 1024 = -1259520;
         *       "891g"将转换为 891 * 1024^3 = 956703965184;
         * @param s
         * @return
         */
        public static long string2long(String s) {
            s = s.trim();
            final int lastpos = s.length() - 1;
            final char lastchar = s.charAt(lastpos);
            if(Character.isDigit(lastchar)) {
                return Long.parseLong(s);
            } else {
                long prefix;
                try {
                    prefix = TraditionalBinaryPrefix.valueOf(lastchar).value;
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("无效长度前缀 '" + lastchar + "'在'" + s + "'。允许前缀是k,m,g,t,p,e(不分大小写)");
                }
                long num = Long.parseLong(s.substring(0, lastpos));
                if(num > (Long.MAX_VALUE/prefix) || num < (Long.MIN_VALUE/prefix)) {
                    throw new IllegalArgumentException(s + "不能转成Long");
                }
                return num * prefix;
            }
        }

        /**
         * 转换一个long int为一个字符串，使用二进制前缀
         * @param n
         * @param unit
         * @param decimalPlaces
         * @return
         */
        public static String long2String(long n, String unit, int decimalPlaces) {
            if(unit == null) {
                unit = "";
            }
            if(n == Long.MIN_VALUE) {
                return "-8" + EXA.symbol + unit;
            }

            final StringBuilder b = new StringBuilder();
            if(n < 0) {
                b.append('-');
                n = -n;
            }
            if(n < KILO.value) {
                b.append(n);
                return (unit.isEmpty() ? b : b.append(" ").append(unit)).toString();
            } else {
                int i = 0;
                for(; i < values().length && n >= values()[i].value; i++);
                TraditionalBinaryPrefix prefix = values()[i - 1];

                if((n & prefix.bitMask) == 0) {
                    b.append(n >> prefix.bitShift);
                } else {
                    final String format = "%." + decimalPlaces + "f";
                    String s = format(format, n/(double)prefix.value);
                    if(s.startsWith("1024")) {
                        prefix = values()[i];
                        s = format(format, n/(double)prefix.value);
                    }
                    b.append(s);
                }
                return b.append(' ').append(prefix.symbol).append(unit).toString();
            }

        }
    }


    public static String escapeHTML(String string) {
        if(string == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean lastCharacterWasSpace = false;
        char[] chars = string.toCharArray();
        for(char c : chars) {
            if(c == ' ') {
                if(lastCharacterWasSpace) {
                    lastCharacterWasSpace = false;
                    sb.append("&nbsp;");
                }else {
                    lastCharacterWasSpace = true;
                    sb.append(" ");
                }
            } else {
                lastCharacterWasSpace = false;
                switch (c) {
                    case '<': sb.append("&lt;"); break;
                    case '>': sb.append("&gt;"); break;
                    case '&': sb.append("&amp;"); break;
                    case '"': sb.append("&quot;"); break;
                    default : sb.append(c); break;
                }
            }
        }
        return sb.toString();
    }


    public static String byteDesc(long len) {
        return TraditionalBinaryPrefix.long2String(len, "8", 2);
    }

    @Deprecated
    public static String limitDecimalTo2(double d) {
        return format("%.2f", d);
    }

    public static String join(CharSequence separator, Iterable<?> strings) {
        Iterator<?> i = strings.iterator();
        if(!i.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i.next().toString());
        while (i.hasNext()) {
            sb.append(separator);
            sb.append(i.next().toString());
        }
        return sb.toString();
    }

    public static String join(char separator, Iterable<?> strings) {
        return join(separator + "", strings);
    }

    public static String join(CharSequence separator, String[] strings) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(String s : strings) {
            if(first) {
                first = false;
            } else {
                sb.append(separator);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String join(char separator, String[] strings) {
        return join(separator + "", strings);
    }

    public static String camelize(String s) {
        StringBuilder sb = new StringBuilder();
        String[] words = split(StringUtils.toLowerCase(s), ESCAPE_CHAR, '_');
        for(String word : words) {
            sb.append(org.apache.commons.lang.StringUtils.capitalize(word));
        }
        return sb.toString();
    }

    public static String replaceTokens(String template, Pattern pattern, Map<String, String> replacements) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = pattern.matcher(template);
        while(matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            if(replacement == null) {
                replacement = "";
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String getStackTrace(Thread t) {
        final StackTraceElement[] stackTrace = t.getStackTrace();
        StringBuilder str = new StringBuilder();
        for(StackTraceElement e : stackTrace) {
            str.append(e.toString() + "\n");
        }
        return str.toString();
    }

    public static String popOptionWithArgument(String name, List<String> args) throws IllegalArgumentException {
        String val = null;
        for(Iterator<String> iter = args.iterator(); iter.hasNext();) {
            String cur = iter.next();
            if(cur.equals("--")) {
                break;
            } else if(cur.equals(name)) {
                iter.remove();
                if(!iter.hasNext()) {
                    throw new IllegalArgumentException("option " + name + " requires 1 argument.");
                }
                val = iter.next();
                iter.remove();
                break;
            }
        }
        return val;
    }

    public static boolean popOption(String name, List<String> args) {
        for(Iterator<String> iter = args.iterator(); iter.hasNext(); ) {
            String cur = iter.next();
            if(cur.equals("--")) {
                break;
            } else if(cur.equals(name)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    public static String popFirstNonOption(List<String> args) {
        for(Iterator<String> iter = args.iterator(); iter.hasNext(); ) {
            String cur = iter.next();
            if(cur.equals("--")) {
                if(!iter.hasNext()) {
                    return null;
                }
                cur = iter.next();
                iter.remove();
                return cur;
            } else if(!cur.startsWith("-")) {
                iter.remove();
                return cur;
            }
        }
        return null;
    }

    public static String toLowerCase(String str) {
        return str.toLowerCase(Locale.ENGLISH);
    }

    public static String toUpperCase(String str) {
        return str.toUpperCase(Locale.ENGLISH);
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        Preconditions.checkNotNull(s1);
        return s1.equalsIgnoreCase(s2);
    }

    public static boolean isAlpha(String str) {
        if(str == null) {
            return false;
        }
        int sz = str.length();
        for(int i = 0; i < sz; i++) {
            if(!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
