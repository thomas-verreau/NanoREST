package nano.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class MicroRestServer extends NanoHTTPD {

    public MicroRestServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {

        String uri = String.valueOf(session.getUri());
        String method = String.valueOf(session.getMethod());

        if (uri.equals("/favicon.ico")) {
            return null;
        }

        System.out.println(method + " " + uri);

        Object object = query(uri, method);

//            append(toString(session.getHeaders())).append("</blockquote></p>");
//            append(toString(session.getParms())).append("</blockquote></p>");
//            append(toString(decodedQueryParameters)).append("</blockquote></p>");
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MicroRestServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return newFixedLengthResponse(str);
    }

    public abstract Object query(String path, String method);
    
}
