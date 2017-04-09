package fmi.unibuc.ro.searcher;

import lombok.AllArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

import static fmi.unibuc.ro.constants.LuceneConstants.*;

@AllArgsConstructor
public class Searcher {

    private String indexDir;
    private String queryString;

    private static final Analyzer analyzer = new RomanianAnalyzer();

    public void search() throws Exception {
        IndexSearcher indexSearcher = createIndexSearcher();

        QueryParser queryParser = createQueryParser();
        Query query = queryParser.parse(queryString);

        TopDocs hits = indexSearcher.search(query, MAX_SEARCH);

        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("Found in ==> " + doc.get(FULL_PATH));
        }

        indexSearcher.getIndexReader().close();
    }

    private IndexSearcher createIndexSearcher() throws Exception{
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        DirectoryReader reader = DirectoryReader.open(directory);

        return new IndexSearcher(reader);
    }

    private QueryParser createQueryParser() {
        return  new QueryParser(CONTENTS, analyzer);
    }
}
