package com.dreamer.wanderer.data.neo;

import com.dreamer.wanderer.bo.Snap;
import com.dreamer.wanderer.data.DataBaseInstance;
import com.dreamer.wanderer.data.converters.SerialiserFactory;
import com.dreamer.wanderer.data.converters.SerialiserSchemaBasedBean;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rjain236 on 25/7/15.
 */
@Service("GraphDB")
public class GraphDB implements DataBaseInstance {

    private final static GraphDatabaseService database;

    @Autowired
    private SerialiserFactory serialiserFactory;

    @Autowired
    private SerialiserSchemaBasedBean serialiserSchemaBasedBean;

    static {
        database = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder("/Users/rjain236/Personal/Projects/Softwares/neo4j-community-2.2.3/data/graph.db")
                .newGraphDatabase();
        GraphDB.registerShutdownHook(database);
    }

    public static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }



    public static void main(String[] args) {
        GraphDatabaseService graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder("/Users/rjain236/Personal/Projects/Softwares/neo4j-community-2.2.3/data/graph.db")
                .newGraphDatabase();
        GraphDB.registerShutdownHook(graphDb);
        try ( Transaction tx = graphDb.beginTx() )
        {
            Node firstNode = graphDb.createNode();
            firstNode.setProperty( "message", "Hello, " );
            Node secondNode = graphDb.createNode();
            secondNode.setProperty( "message", "World!" );

            Relationship relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty( "message", "brave Neo4j " );
            // Database operations go here
            tx.success();
        }

        graphDb.shutdown();
    }


    @Override
    public <B extends Snap> Long save(B bean) {
        return null;
    }

    @Override
    public <B extends Snap> B saveOrUpdate(B bean) {
        return null;
    }

    @Override
    public <B extends Snap> void delete(B bean) {

    }
}
