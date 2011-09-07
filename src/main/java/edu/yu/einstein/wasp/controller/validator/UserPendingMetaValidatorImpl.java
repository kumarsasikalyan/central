package edu.yu.einstein.wasp.controller.validator;

import java.util.List;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.UserService;

public class UserPendingMetaValidatorImpl extends MetaValidatorImpl{

	protected UserService userService;
	protected LabService labService;
	
	public UserPendingMetaValidatorImpl(UserService userService, LabService labService){
		this.allowableConstraints.add(Constraint.isValidPiEmail);
		this.userService = userService;
		this.labService = labService;
	}

 	@Override
	public void validate(List<? extends MetaBase> list, BindingResult result, String area, String parentarea) {
 		super.validate(list, result, area, parentarea); // call the overridden method in the superclass to validate
 		
		Errors errors=new BindException(result.getTarget(), parentarea); 
		for(int i=0;i<list.size();i++) {
			MetaBase meta=list.get(i);
			String constraint=map.get(meta.getK());
			if (constraint==null) continue;
			String errorFieldName = parentarea+"Meta["+i+"].k";
			String errorMessageKey = meta.getK() + ".error";
		    String defaultMessage = errorMessageKey+" (no message has been defined for this property)";
		
			if (constraint.equals( Constraint.isValidPiEmail.name() ) ){
		        if (meta.getV()==null || meta.getV().isEmpty()) {
		        	errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
		        } else {
					User primaryInvestigator = userService.getUserByEmail(meta.getV());
					if (primaryInvestigator.getUserId() == 0 || primaryInvestigator.getIsActive() == 0){
					  errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
					} else {
						Lab lab = labService.getLabByPrimaryUserId(primaryInvestigator.getUserId());
						if (lab.getLabId() == 0){
							errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
						}
					}
		        }
			}
		}
		result.addAllErrors(errors);
	}
}
