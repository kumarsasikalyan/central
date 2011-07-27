
/**
 *
 * StateService.java 
 * @author echeng (table2type.pl)
 *  
 * the StateService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.State;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StateServiceImpl extends WaspServiceImpl<State> implements StateService {

  private StateDao stateDao;
  @Autowired
  public void setStateDao(StateDao stateDao) {
    this.stateDao = stateDao;
    this.setWaspDao(stateDao);
  }
  public StateDao getStateDao() {
    return this.stateDao;
  }

  // **

  
  public State getStateByStateId (final int stateId) {
    return this.getStateDao().getStateByStateId(stateId);
  }
}

