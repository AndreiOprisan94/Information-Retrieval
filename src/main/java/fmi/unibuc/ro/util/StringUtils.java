package fmi.unibuc.ro.util;

import fmi.unibuc.ro.analysis.CustomRomanianAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import static fmi.unibuc.ro.constants.LuceneConstants.*;

public class StringUtils {

    public static boolean verifyInTopTen(String fieldValue, String queryTerms) {
        Analyzer analyzer= new CustomRomanianAnalyzer();
        QueryParser queryParser = new QueryParser(CONTENTS, analyzer);
        Query analyzedField = null;

        try {
            analyzedField = queryParser.parse(fieldValue);
        }catch (ParseException pe){
            return false;
        }

        String[] fieldWords = analyzedField.toString().replace("contents:","").split("\\W+");
        String[] queryWords = queryTerms.replace("contents:","").split("\\W+");

        boolean isInTopTen = false;
        int length = fieldWords.length > 10 ? 10 : fieldWords.length;

        for (int i = 0 ; i < queryWords.length ; i++)
            for ( int j = 0; j < length; j++)
                if (queryWords[i].equalsIgnoreCase(fieldWords[j]))
                    isInTopTen = true;

        return isInTopTen;
    }
}
