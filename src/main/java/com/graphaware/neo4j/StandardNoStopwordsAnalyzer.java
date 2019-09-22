

package com.graphaware.neo4j;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.neo4j.graphdb.index.fulltext.AnalyzerProvider;
import org.neo4j.helpers.Service;

@Service.Implementation(AnalyzerProvider.class)
public class StandardNoStopwordsAnalyzer extends AnalyzerProvider {

    public static final String STANDARD_ANALYZER_NAME = "standard-no-stopwords";

    public StandardNoStopwordsAnalyzer() {
        super(STANDARD_ANALYZER_NAME);
    }

    @Override
    public Analyzer createAnalyzer() {
        return new StandardAnalyzer(CharArraySet.EMPTY_SET);
    }

    @Override
    public String description() {
        return "The default, standard analyzer but without stopwords. Tokenizes on non-letter and filters out English stop words and punctuation. " +
                "Does no stemming, but takes care to keep likely product names, URLs and email addresses as single terms.";
    }
}
