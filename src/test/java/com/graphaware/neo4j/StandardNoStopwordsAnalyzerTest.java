package com.graphaware.neo4j;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.harness.junit.Neo4jRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

public class StandardNoStopwordsAnalyzerTest {

    @ClassRule
    public static Neo4jRule neo4j = new Neo4jRule();

    private static GraphDatabaseService database;

    @BeforeClass
    public static void setUpClass() throws Exception {
        database = neo4j.getGraphDatabaseService();
        database.execute("CALL db.index.fulltext.createNodeIndex('no-stopwords', ['Post'], ['text'], {analyzer:'standard-no-stopwords'})")
                .close();
    }

    @Before
    public void setUp() {
        database.execute("MATCH (n) DETACH DELETE n");
    }

    @Test
    public void testSearchQueryContainingStopwordsReturnsResults() {
        database.execute("CREATE (n:Post {text: 'To be or not to be'})");

        Result result = database.execute("CALL db.index.fulltext.queryNodes('no-stopwords', 'to be or not to be')");
        assertThat(newArrayList(result)).hasSize(1);
        result.close();
    }
}
