package fmi.unibuc.ro.queries;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;

import java.io.IOException;

public class BoostingScoreQuery extends CustomScoreQuery{

    public BoostingScoreQuery(Query query){
        super(query);
    }

    @Override
    protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
        return new BoostingScoreProvider(getSubQuery().toString(), context);
    }
}
