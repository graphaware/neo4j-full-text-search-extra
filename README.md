# Neo4j Full Text Search Extra

## Installation

- Copy the `fts-extra-1.0.0.jar` in the plugins directory of Neo4j.
- Restart the Neo4j database

## Custom Analyzers

### Standard No Stopwords

The default, standard analyzer but without stopwords. Tokenizes on non-letter and filters out English stop words and punctuation.
Does no stemming, but takes care to keep likely product names, URLs and email addresses as single terms.

#### When applicable ?

The following text : `To be or not to be` will have no tokens indexed, because they're all stopwords and thus will never be returned
by any search query.

#### Usage

Create a FTS index using the analyzer :

```
CALL db.index.fulltext.createNodeIndex('no-stopwords', ['Post'], ['text'], {analyzer:'standard-no-stopwords'})
```

Create some data :

```
CREATE (n:Post {text: 'To be or not to be'})
```

Query data :

```
CALL db.index.fulltext.queryNodes('no-stopwords', 'to be or not to be')
```


### TODO

Move Czech Analyzer here