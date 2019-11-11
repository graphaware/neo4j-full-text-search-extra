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

public class SynonymAnalyzerTest {

    @ClassRule
    public static Neo4jRule neo4j = new Neo4jRule();

    private static GraphDatabaseService database;

    @BeforeClass
    public static void setUpClass() throws Exception {
        database = neo4j.getGraphDatabaseService();
        database.execute("CALL db.index.fulltext.createNodeIndex('syn', ['Post'], ['text'], {analyzer:'synonym-custom'})")
                .close();
    }

    @Before
    public void setUp() {
        database.execute("MATCH (n) DETACH DELETE n");
    }

    @Test
    public void testSearchQueryContainingSynonymReturnsResults() {
        database.execute("CREATE (n:Post {text: 'my hard disk has 10 GB total space space'})");

        Result result = database.execute("CALL db.index.fulltext.queryNodes('syn', 'gigabyte')");
        assertThat(newArrayList(result)).hasSize(1);
        result.close();
    }

    @Test
    public void testFuzzySearchQueryUsingSynonymReturnsResults() {
        database.execute("CREATE (n:Post {text: 'my hard disk has 10 GB total space space'})");

        Result result = database.execute("CALL db.index.fulltext.queryNodes('syn', 'gig*')");
        assertThat(newArrayList(result)).hasSize(1);
        result.close();
    }

    @Test
    public void testSynonymFuzzy() {
        database.execute("CREATE (n:Post {text: 'This is an article about full text search'})");

        Result result = database.execute("CALL db.index.fulltext.queryNodes('syn', 'fullt*')");
        assertThat(newArrayList(result)).hasSize(1);
        result.close();
    }

    @Test
    public void testSynonymFuzzy2() {
        database.execute("CREATE (n:Post {text: 'This is an article about fts'})");

        Result result = database.execute("CALL db.index.fulltext.queryNodes('syn', '\"fullt*\"')");
        assertThat(newArrayList(result)).hasSize(1);
        result.close();
    }


    @Test
    public void testSynonymMultiToken() {
        database.execute("CREATE (n:Post {text: 'This is an article about full text search'})");

        Result result = database.execute("CALL db.index.fulltext.queryNodes('syn', 'fulltextsearch')");
        assertThat(newArrayList(result)).hasSize(1);
        result.close();
    }

    @Test
    public void testSynonymMultiTokenOrigin() {
        database.execute("CREATE (n:Post {text: 'This is an article about the cypher query language'})");

        Result result = database.execute("CALL db.index.fulltext.queryNodes('syn', 'cql')");
        assertThat(newArrayList(result)).hasSize(1);
        result.close();
    }

    @Test
    public void testSynonymWithPhraseQuery() {
        database.execute("CREATE (n:Post {text: 'This is an article about the cypher query language in the industry'})");

        Result result = database.execute("CALL db.index.fulltext.queryNodes('syn', '\"cql in the industry\"~2')");
        assertThat(newArrayList(result)).hasSize(1);
        result.close();
    }
}
