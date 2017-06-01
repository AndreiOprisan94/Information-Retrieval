package fmi.unibuc.ro.searcher;

import fmi.unibuc.ro.analysis.CustomRomanianAnalyzer;
import fmi.unibuc.ro.factory.HighlighterFactory;
import fmi.unibuc.ro.queries.BoostingScoreQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

import static fmi.unibuc.ro.constants.LuceneConstants.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Searcher {

    private IndexSearcher indexSearcher;
    private QueryParser queryParser;

    private static final Analyzer analyzer = new CustomRomanianAnalyzer();

    public static Searcher newInstance(String indexDir) throws Exception {
        Searcher searcher = new Searcher();
        searcher.indexSearcher = createIndexSearcher(indexDir);
        searcher.queryParser = createQueryParser();

        return searcher;
    }

    private static IndexSearcher createIndexSearcher(String indexDir) throws Exception{
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        DirectoryReader reader = DirectoryReader.open(directory);

        return new IndexSearcher(reader);
    }

    private static QueryParser createQueryParser() {
        return  new QueryParser(CONTENTS, analyzer);
    }

    public void search(String queryString) throws Exception {
        Query query = queryParser.parse(queryString);
        query = new BoostingScoreQuery(query);
        TopDocs hits = indexSearcher.search(query, MAX_SEARCH);
        Highlighter highlighter = HighlighterFactory.buildHighlighter(query);

        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("Found in ==> " + doc.get(FULL_PATH));
            System.out.println("With score ==> " + scoreDoc.score);

            TokenStream tokenStream = analyzer.tokenStream(CONTENTS, doc.get(CONTENTS));
            String output = highlighter.getBestFragments(tokenStream, doc.get(CONTENTS), 10, "....");
            System.out.println(output + "\n");
        }
    }

    public void close() throws Exception{
        indexSearcher.getIndexReader().close();
    }

}
