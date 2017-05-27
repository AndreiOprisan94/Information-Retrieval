package fmi.unibuc.ro.factory;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;

public class HighlighterFactory {
    public static Highlighter buildHighlighter(Query query){
        Formatter formatter = new SimpleHTMLFormatter("<<", ">>");
        QueryScorer queryScorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer,10);
        Highlighter highlighter = new Highlighter(formatter, queryScorer);
        highlighter.setTextFragmenter(fragmenter);

        return highlighter;
    }
}
