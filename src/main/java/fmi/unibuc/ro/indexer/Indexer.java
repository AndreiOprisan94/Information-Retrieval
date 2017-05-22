package fmi.unibuc.ro.indexer;

import fmi.unibuc.ro.analysis.CustomRomanianAnalyzer;
import fmi.unibuc.ro.util.DataDirectoryParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;


import java.io.File;
import java.nio.file.Paths;

import static fmi.unibuc.ro.constants.LuceneConstants.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Indexer {

    private IndexWriter indexWriter;

    private static final Analyzer analyzer = new CustomRomanianAnalyzer();
    private static final Tika tika = new Tika();

    public static Indexer newInstance(String indexDir) throws Exception{
        Indexer indexer = new Indexer();
        indexer.indexWriter = createIndexWriter(indexDir);

        return indexer;
    }

    private static IndexWriter createIndexWriter(String indexDir) throws Exception{
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        return new IndexWriter(directory, config);
    }

    public int index(String dataDir) throws Exception {
        File[] dataDirFiles = DataDirectoryParser.retrieveRegularFiles(dataDir);

        for (File file : dataDirFiles)
            indexFile(file);

        return indexWriter.numDocs();
    }

    public void close() throws Exception{
        indexWriter.close();
    }

    private void indexFile(File file) throws Exception{
        System.out.println("Indexing " + file.getCanonicalPath());
        Document doc = getDocument(file);
        indexWriter.addDocument(doc);
    }

    private Document getDocument(File file) throws Exception {
        Document document = new Document();

        Field contentField = new TextField(CONTENTS, tika.parseToString(file), Field.Store.YES);
        Field pathField = new StringField(FULL_PATH, file.getCanonicalPath(),Field.Store.YES);

        document.add(contentField);
        document.add(pathField);

        return document;
    }

}
