package com.graphaware.neo4j;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.neo4j.graphdb.index.fulltext.AnalyzerProvider;
import org.neo4j.helpers.Service;

@Service.Implementation(AnalyzerProvider.class)
public class SynonymAnalyzer extends AnalyzerProvider {

    public static final String ANALYZER_NAME = "synonym-custom";

    public SynonymAnalyzer() {
        super(ANALYZER_NAME);
    }

    @Override
    public Analyzer createAnalyzer() {
        try {
            String synFile = "synonyms.txt";
            Analyzer analyzer = CustomAnalyzer.builder()
                    .withTokenizer(StandardTokenizerFactory.class)
                    .addTokenFilter(StandardFilterFactory.class)
                    .addTokenFilter(SynonymFilterFactory.class, "synonyms", synFile, "expand", "true")
                    .addTokenFilter(LowerCaseFilterFactory.class)
                    .addTokenFilter(EdgeNGramFilterFactory.class, "minGramSize", "1", "maxGramSize", "5")
                    .build();

            return analyzer;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create analyzer", e);
        }
    }

    @Override
    public String description() {
        return "The default, standard analyzer with a synonyms file. This is an example analyzer for educational purposes.";
    }
}
