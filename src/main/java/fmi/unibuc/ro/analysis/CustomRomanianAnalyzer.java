package fmi.unibuc.ro.analysis;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.tartarus.snowball.ext.RomanianStemmer;

import java.io.IOException;

public final class CustomRomanianAnalyzer extends StopwordAnalyzerBase {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final CharArraySet stopwords;
        try {
            stopwords = loadStopwordSet(false, RomanianAnalyzer.class, "stopwords.txt", "#");
        } catch (IOException ex){
            throw new RuntimeException("Unable to load default stopword set");
        }
        final Tokenizer source = new StandardTokenizer();
        TokenStream result = new StandardFilter(source);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, stopwords);
        result = new SnowballFilter(result, new RomanianStemmer());
        result = new ASCIIFoldingFilter(result);
        return new TokenStreamComponents(source, result);
    }
}
