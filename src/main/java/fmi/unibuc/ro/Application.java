package fmi.unibuc.ro;

import fmi.unibuc.ro.indexer.Indexer;
import fmi.unibuc.ro.searcher.Searcher;

import java.util.Scanner;

public class Application {

    public static void main(String args[]) throws Exception{
        String indexDir = System.getProperty("indexDir");
        String dataDir = System.getProperty("dataDir");

        System.out.println("indexDir = " + indexDir + "\ndataDir = " + dataDir);

        Indexer indexer = new Indexer(indexDir, dataDir);
        int numberOfFiles = indexer.index();

        System.out.println("Indexing process finished....");
        System.out.println("Number of files processed = " + numberOfFiles);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type in what you want to search:");

        while (scanner.hasNext()) {
            String query = scanner.nextLine();
            System.out.println("Searching for " + query + "....");

            Searcher searcher = new Searcher(indexDir, query);
            searcher.search();

            System.out.println("Please type in what you want to search:");
        }
        scanner.close();
    }
}