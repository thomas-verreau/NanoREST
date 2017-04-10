
package nano.rest.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nicolas Enzweiler
 */
public class HttpDbMapping {
    
    private List<HttpDbNode> nodes = new ArrayList();

    public HttpDbNode findQueryByPathAndMethod(String path, String method) {
        for (HttpDbNode query : nodes) {
            
            if (query.matches(method, path))
                return query;
        }
        
        return null;
    }
    
    /**
     * @return the nodes
     */
    public List<HttpDbNode> getNodes() {
        return nodes;
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(List<HttpDbNode> nodes) {
        this.nodes = nodes;
    }
}
