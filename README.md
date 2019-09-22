## Neo4j Full Text Search Custom Analyzers

### Standard No Stopwords

The default, standard analyzer but without stopwords. Tokenizes on non-letter and filters out English stop words and punctuation.
Does no stemming, but takes care to keep likely product names, URLs and email addresses as single terms.

#### When applicable ?

The following text : `To be or not to be` will have no tokens indexed, because they're all stopwords and thus will never be returned
by any search query.


### TODO

Move Czech Analyzer here