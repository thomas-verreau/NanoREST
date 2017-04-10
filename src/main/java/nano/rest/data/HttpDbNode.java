
package nano.rest.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nicolas Enzweiler
 */
public class HttpDbNode {

    private String path;
    private String method;
    private String template;
    private boolean hasResults;
    
    public boolean matches(String methodInstance, String pathInstance) {
        return pathInstance.matches(this.path) && methodInstance.equals(this.method);
    }
    
    public Object[] extract(String pathInstance) {
        Pattern pattern = Pattern.compile(this.path);
        Matcher matcher = pattern.matcher(pathInstance);
        
        Object[] objects = null;
        
        if (matcher.find()) {
            
            objects = new Object[ matcher.groupCount() ];
            
            for (int i = 1; i <= matcher.groupCount(); i++) {
                // System.out.println("");
                objects[i-1] = matcher.group(i);
            }
        }
        
        return objects;
    }
    
    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @return the hasResults
     */
    public boolean isHasResults() {
        return hasResults;
    }

    /**
     * @param hasResults the hasResults to set
     */
    public void setHasResults(boolean hasResults) {
        this.hasResults = hasResults;
    }


    
}
