package fmi.unibuc.ro;

import fmi.unibuc.ro.indexer.Indexer;

public class Application {

    public static void main(String args[]) throws Exception{
        String indexDir = System.getProperty("indexDir");
        String dataDir = System.getProperty("dataDir");

        System.out.println("indexDir = " + indexDir + "\ndataDir = " + dataDir);

        Indexer indexer = new Indexer(indexDir, dataDir);
        int numberOfFiles = indexer.index();

        System.out.println("Indexing process finished....");
        System.out.println("Number of files processed = " + numberOfFiles);
    }
}