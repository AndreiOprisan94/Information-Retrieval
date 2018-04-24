package fmi.unibuc.ro;

import fmi.unibuc.ro.indexer.Indexer;
import fmi.unibuc.ro.searcher.Searcher;

import java.util.Scanner;

public final class Application {

    public static void main(String args[]) throws Exception{
        String indexDir = System.getProperty("indexDir");
        String dataDir = System.getProperty("dataDir");
        System.out.println("indexDir = " + indexDir + "\ndataDir = " + dataDir);

        System.out.println("Starting indexing process....");
        Indexer indexer = Indexer.newInstance(indexDir);
        int numberOfFiles = indexer.index(dataDir);
        indexer.close();
        System.out.println("Indexing process finished....");
        System.out.println("Number of files processed = " + numberOfFiles);

        Searcher searcher = Searcher.newInstance(indexDir);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please type in what you want to search:");
        while (scanner.hasNext()) {
            String query = scanner.nextLine();
            System.out.println("Searching for " + query + "....");
            searcher.search(query);
            System.out.println("Please type in what you want to search:");
        }

        scanner.close();
        searcher.close();
    }
}
