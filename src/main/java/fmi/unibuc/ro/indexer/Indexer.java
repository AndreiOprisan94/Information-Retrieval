package fmi.unibuc.ro.indexer;

import lombok.AllArgsConstructor;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.stream.Stream;

import static fmi.unibuc.ro.constants.LuceneConstants.*;

@AllArgsConstructor
public class Indexer {

    private String indexDir;
    private String dataDir;

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
        File indexDirFile = new File(indexDir);
        Directory directory = FSDirectory.open(indexDirFile);

        return new IndexWriter(directory,
                               new StandardAnalyzer(Version.LUCENE_30),
                               true,
                               IndexWriter.MaxFieldLength.UNLIMITED);
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

        Field contentField = new Field(CONTENTS, new FileReader(file));

        Field fileNameField = new Field(FILE_NAME, file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED);

        Field fullPathField = new Field(FULL_PATH, file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED);

        document.add(contentField);
        document.add(fileNameField);
        document.add(fullPathField);

        return document;
    }

}
