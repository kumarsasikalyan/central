package edu.yu.einstein.wasp.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao;
import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.dao.AcctQuoteMetaDao;
import edu.yu.einstein.wasp.dao.JobCellDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/job2quote")
public class Job2QuoteController extends WaspController {

	@Autowired
	private AcctQuoteDao	acctQuoteDao;
	@Autowired
	private AcctQuoteMetaDao	acctQuoteMetaDao;
	@Autowired
	private AcctJobquotecurrentDao acctJobquotecurrentDao;
	@Autowired
	private JobDao			jobDao;
	@Autowired
	private JobUserDao		jobUserDao;
	@Autowired
	private RoleDao			roleDao;
	@Autowired
	private TaskDao			taskDao;
	@Autowired
	private StateDao		stateDao;
	@Autowired
	private StatejobDao		statejobDao;
	@Autowired
	private TaskService		taskService;
	@Autowired
	private MessageService	messageService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("acctQuote", AcctQuoteMeta.class, request.getSession());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {

		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "job2quote/list";
	}

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	public String getListJSON(HttpServletResponse response) {

		String search = request.getParameter("_search");
		String searchStr = request.getParameter("searchString");

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		String userId = request.getParameter("userId");

		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Job> jobList = new ArrayList();
		List<State> stateList = taskService.getJob2QuoteStates();
		for (State st : stateList) {
			Map m = new HashMap();
			m.put("stateId", st.getStateId());
			List<Statejob> sjList = statejobDao.findByMap(m);
			for (Statejob sj : sjList) {
				jobList.add(sj.getJob());
			}
		}

		List<Job> job2quoteList;
		if (!search.equals("true") && StringUtils.isEmpty(userId)) {
			if (StringUtils.isEmpty(sidx)) {
				job2quoteList = jobList;
			} else {
				job2quoteList = this.jobDao.findAllOrderBy(sidx, sord);
				job2quoteList.retainAll(jobList);
			}
		} else {
			Map m = new HashMap();

			if (search.equals("true") && !searchStr.isEmpty())
				m.put(request.getParameter("searchField"), request.getParameter("searchString"));

			if (!StringUtils.isEmpty(userId))
				m.put("UserId", Integer.parseInt(userId));

			job2quoteList = this.jobDao.findByMap(m);
			job2quoteList.retainAll(jobList);
		}

		try {
			// index of page
			int pageIndex = Integer.parseInt(request.getParameter("page")); 
			// number of rows in one page
			int pageRowNum = Integer.parseInt(request.getParameter("rows")); 
			// total number of rows
			int rowNum = job2quoteList.size(); 
			// total number of pages
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum; 

			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");

			Map<String, String> userData = new HashMap<String, String>();
			userData.put("page", pageIndex + "");
			userData.put("selId", StringUtils.isEmpty(request.getParameter("selId")) ? "" : request.getParameter("selId"));
			jqgrid.put("userdata", userData);

			List<Map> rows = new ArrayList<Map>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/*
			 * if the selId is set, change the page index to the one contains
			 * the selId
			 */
			if (!StringUtils.isEmpty(request.getParameter("selId"))) {
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = job2quoteList.indexOf(jobDao.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}

			List<Job> page = job2quoteList.subList(frId, toId);
			for (Job item : page) {
				Map cell = new HashMap();
				cell.put("id", item.getJobId());

				User user = userDao.getById(item.getUserId());
				List<AcctJobquotecurrent> ajqcList = item.getAcctJobquotecurrent();
				float amount = ajqcList.isEmpty() ? 0 : ajqcList.get(0).getAcctQuote().getAmount();

				List<AcctQuoteMeta> itemMetaList = ajqcList.isEmpty() ? new ArrayList() : 
					getMetaHelperWebapp().syncWithMaster(ajqcList.get(0).getAcctQuote().getAcctQuoteMeta());

				List<String> cellList = new ArrayList<String>(
					Arrays.asList(new String[] { 
						item.getName(),
						String.format("%.2f", amount),
						user.getNameFstLst(), 
						item.getLab().getName(), 
						item.getLastUpdTs().toString() 
					}));

				for (AcctQuoteMeta meta : itemMetaList) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + job2quoteList, e);
		}
	}

	/**
	 * Creates/Updates user
	 * 
	 * @Author AJ Jing 
	 */	
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or User.login == principal.name")
	public String updateDetailJSON(@RequestParam("id") Integer jobId, AcctQuote quoteForm, ModelMap m, HttpServletResponse response) {

		List<AcctQuoteMeta> metaList = getMetaHelperWebapp().getFromJsonForm(request, AcctQuoteMeta.class);
		quoteForm.setJobId(jobId);
		quoteForm.setAcctQuoteMeta(metaList);
		
		AcctQuote acctQuoteDb = this.acctQuoteDao.save(quoteForm);
		Integer quoteId = acctQuoteDb.getQuoteId();
		this.acctQuoteMetaDao.updateByQuoteId(quoteId, metaList);
		
		AcctJobquotecurrent acctJobquotecurrent = this.acctJobquotecurrentDao.getAcctJobquotecurrentByJobId(jobId);
		acctJobquotecurrent.setQuoteId(quoteId);
		// if jobid is null, create a new record in database table 
		if(acctJobquotecurrent.getJobId() == null) {
			acctJobquotecurrent.setJobId(jobId);
			acctJobquotecurrentDao.persist(acctJobquotecurrent);
		}
		
		Map qMap = new HashMap();
		qMap.put("jobId", jobId);
		qMap.put("state.task.iName", "Quote Job");
		List<Statejob> sjList = statejobDao.findByMap(qMap);
		for (Statejob sj : sjList) {
			State st = stateDao.getStateByStateId(sj.getStateId());
			st.setStatus("COMPLETED");
		}
		
		
		try {
			response.getWriter().println(this.messageService.getMessage("acctQuote.created_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message", e);
		}
	
	    
	}
}