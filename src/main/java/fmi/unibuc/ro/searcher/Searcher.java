package fmi.unibuc.ro.searcher;

import lombok.AllArgsConstructor;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;

import static fmi.unibuc.ro.constants.LuceneConstants.*;

@AllArgsConstructor
public class Searcher {

    private String indexDir;
    private String queryString;

    public void search() throws Exception {
        IndexSearcher indexSearcher = createIndexSearcher();

        QueryParser queryParser = createQueryParser();
        Query query = queryParser.parse(queryString);

        TopDocs hits = indexSearcher.search(query, MAX_SEARCH);

        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("Found in ==> " + doc.get(FULL_PATH));
        }

        indexSearcher.close();
    }

    private IndexSearcher createIndexSearcher() throws Exception{
        File indexDirFile = new File(indexDir);
        Directory directory = FSDirectory.open(indexDirFile);
        return new IndexSearcher(directory);
    }

    private QueryParser createQueryParser() {
        return new QueryParser(Version.LUCENE_30,
                               CONTENTS,
                               new StandardAnalyzer(Version.LUCENE_30));
    }
}
