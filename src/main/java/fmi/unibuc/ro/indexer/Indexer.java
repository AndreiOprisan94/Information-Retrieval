package fmi.unibuc.ro.indexer;

import lombok.AllArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static fmi.unibuc.ro.constants.LuceneConstants.*;

@AllArgsConstructor
public class Indexer {

    private String indexDir;
    private String dataDir;

    private static final Analyzer analyzer = new RomanianAnalyzer();

    public int index() throws Exception {
        System.out.println("Starting indexing process....");

        //Create Lucene IndewWriter
        IndexWriter indexWriter = createIndexWriter();

        File[] dataDirFiles = getFilteredDataDirFiles();

        for (File file : dataDirFiles)
            indexFile(file, indexWriter);

        int numberOfFiles = indexWriter.numDocs();
        indexWriter.close();

        return numberOfFiles;
    }

    private IndexWriter createIndexWriter() throws Exception{
        Directory directory = FSDirectory.open(Paths.get(indexDir));

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        return new IndexWriter(directory, config);
    }

    private File[] getFilteredDataDirFiles(){
        FileFilter fileFilter = new TextFileFilter();
        File dataDirFiles = new File(dataDir);

        return Stream.of(dataDirFiles.listFiles())
                     .filter(fileFilter::accept)
                     .toArray(File[] :: new);

    }

    private void indexFile(File file, IndexWriter writer) throws Exception{
        System.out.println("Indexing " + file.getCanonicalPath());
        Document doc = getDocument(file);
        writer.addDocument(doc);
    }

    private Document getDocument(File file) throws Exception {
        Document document = new Document();

        Field contentField = new TextField(CONTENTS, new FileReader(file));

        Field pathField = new StringField(FULL_PATH, file.getCanonicalPath(),Field.Store.YES);

        document.add(contentField);
        document.add(pathField);

        return document;
    }

}
