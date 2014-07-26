package net.cloudengine.web.crud.support;
 
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * Helper class to allow URL path's to be parsed into
 * EL Expression type tokens.
 */
public class PathUtil {
    public static final Map<String, String> getPathVariables(String patternStr,
            String[] names, String path) {
        String regExPattern = patternStr.replace("*", "(.*)");
        Map<String, String> tokenMap = new HashMap<String, String>();
        Pattern p = Pattern.compile(regExPattern);
        Matcher matcher = p.matcher(path);
 
        if (matcher.find()) {
            // Get all groups for this match
            for (int i = 0; i <= matcher.groupCount(); i++) {
                String groupStr = matcher.group(i);
                if (i != 0) {
                    tokenMap.put(names[i - 1], groupStr);
                }
            }
        }
        return tokenMap;
    }
}