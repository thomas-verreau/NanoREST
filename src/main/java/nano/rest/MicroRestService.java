package nano.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import nano.rest.data.HttpDbMapping;
import nano.rest.data.HttpDbNode;
import fi.iki.elonen.util.ServerRunner;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

/**
 *
 * @author Nicolas ENZWEILER
 */
@Service
public class MicroRestService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${mrest.port}")
    String mrestPort;
    
    @Value("${mrest.file}")
    String mrestFile;

    private HttpDbMapping mapping = null;
    private long lastUpdate = 0;

    public MicroRestService() {
        
    }

    public void start() {
        // System.out.println("mrestPort=" + mrestPort);
        // System.out.println("mrestFile=" + mrestFile);
        
        loadMapping();
        
        MicroRestServer server = new MicroRestServer(Integer.parseInt(mrestPort)) {
            @Override
            public Object query(String path, String method) {
                return MicroRestService.this.query(path, method);
            }
        };

        ServerRunner.executeInstance(server);
    }

    private void loadMapping() {
        File file = new File(mrestFile);
        
        if (file.lastModified() <= lastUpdate) {
            return;
        }

        lastUpdate = file.lastModified();

        // System.out.println("Reloading mapping.");

        try {
            ObjectMapper om = new ObjectMapper();
            mapping = om.readValue(file, HttpDbMapping.class);
        } catch (IOException ex) {
            Logger.getLogger(MicroRestService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Object query(String path, String method) {
        loadMapping();

        List<Object> list = new ArrayList();

        HttpDbNode query = mapping.findQueryByPathAndMethod(path, method);
        if (query == null) {
            return null;
        }

        jdbcTemplate.query(query.getTemplate(), query.extract(path), new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    Map<String, Object> map = new HashMap();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String column = rsmd.getColumnName(i);
                        map.put(column, rs.getObject(column));
                        //System.out.println(column);
                    }
                    list.add(map);
                }

                return rsmd.getColumnCount();
            }
        });

        return list;
    }

}
