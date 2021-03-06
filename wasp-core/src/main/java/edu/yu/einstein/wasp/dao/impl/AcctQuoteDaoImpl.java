
/**
 *
 * AcctQuoteDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuote Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctQuote;


@Transactional("entityManager")
@Repository
public class AcctQuoteDaoImpl extends WaspDaoImpl<AcctQuote> implements edu.yu.einstein.wasp.dao.AcctQuoteDao {

	/**
	 * AcctQuoteDaoImpl() Constructor
	 *
	 *
	 */
	public AcctQuoteDaoImpl() {
		super();
		this.entityClass = AcctQuote.class;
	}


	/**
	 * getAcctQuoteByQuoteId(final int quoteId)
	 *
	 * @param final int quoteId
	 *
	 * @return acctQuote
	 */

	@Override
	@Transactional("entityManager")
	public AcctQuote getAcctQuoteByQuoteId (final int quoteId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", quoteId);

		List<AcctQuote> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctQuote rt = new AcctQuote();
			return rt;
		}
		return results.get(0);
	}



}

