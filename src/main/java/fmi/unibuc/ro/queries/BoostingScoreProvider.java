package fmi.unibuc.ro.queries;

import fmi.unibuc.ro.constants.LuceneConstants;
import fmi.unibuc.ro.util.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.CustomScoreProvider;

import java.io.IOException;

public class BoostingScoreProvider extends CustomScoreProvider {

    String terms;

    public BoostingScoreProvider(String terms, LeafReaderContext context) {
        super(context);
        this.terms = terms;
    }

    @Override
    public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
        Document document = context.reader().document(doc);
        String field = document.get(LuceneConstants.CONTENTS);
        boolean isInTopTen = StringUtils.verifyInTopTen(field, terms);

        if (isInTopTen)
            return  1.5f * subQueryScore;

        return subQueryScore;
    }
}
