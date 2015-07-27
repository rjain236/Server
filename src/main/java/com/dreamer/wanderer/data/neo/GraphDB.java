package com.dreamer.wanderer.data.neo;

import com.dreamer.wanderer.data.DataBaseInstance;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.stereotype.Service;

/**
 * Created by rjain236 on 25/7/15.
 */
@Service("GraphDB")
public class GraphDB implements DataBaseInstance<GraphDB,NeoBean> {

    private final static GraphDatabaseService database;

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
    public Long save(NeoBean bean) {
        return null;
    }

    @Override
    public Long saveOrUpdate(NeoBean bean) {
        return null;
    }

    @Override
    public void delete(NeoBean bean) {

    }
}
