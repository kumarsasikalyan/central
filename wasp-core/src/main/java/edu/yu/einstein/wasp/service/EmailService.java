
package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;

/**
 * EmailService interface.
 * 
 * Specifies interface for sending WASP emails
 * 
 */
@Service
public interface EmailService  {
	
	/**
	 * Sends an email message to a user requesting that they confirm their email address
	 * 
	 * @param user
	 * @param authcode
	 */
	public void sendUserEmailConfirm(final User user, final String authcode);

	/**
	 * Sends an email message to a pending user requesting that they confirm their email address
	 * 
	 * @param userPending
	 * @param authcode
	 */
	public void sendPendingUserEmailConfirm(final UserPending userPending, final String authcode);
	
	/**
	 * Sends an email message to a pending PI requesting that they confirm their email address
	 * 
	 * @param userPending
	 * @param authcode
	 */
	public void sendPendingPIEmailConfirm(final UserPending userPending, final String authcode);
	
	
	/**
	 * Sends an email message to a pending user notifying an accepted application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingUserNotifyAccepted(final User user, final Lab lab);
	
	/**
	 * Sends an email message to a pending lab user notifying an accepted application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingLabUserNotifyAccepted(final User user, final Lab lab);
	
	/**
	 * Sends an email message to a pending lab user notifying a rejected application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingLabUserNotifyRejected(final User user, final Lab lab);
	
	/**
	 * Sends an email message to a pending user notifying a rejected application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingUserNotifyRejected(final UserPending userPending, final Lab lab);
	
	/**
	 * Sends an email message to PI notifying an accepted application to create a lab 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingLabNotifyAccepted(final Lab lab);
	
	/**
	 * Sends an email message to a PI notifying a rejected application to create a lab
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 * @param comment reason for rejection
	 */
	public void sendPendingLabNotifyRejected(final LabPending labPending, final String comment);
	
	/**
	 * Sends an email message to the list of lab managers and principal investigator of the lab 
	 * which the pending user would like to join. 
	 *
	 * @param userPending the pending user
	 */
	public void sendPendingUserConfirmRequest(final UserPending userPending) ;
	
	/**
	 * Sends an email message to the list of lab managers and principal investigator of the lab 
	 * which the pending labUser would like to join.  
	 *
	 * @param labUser the pending lab user
	 *
	 */
	public void sendPendingLabUserConfirmRequest(final LabUser labUser);
	
	/**
	 * Sends an email message to the department administrator to confirm or deny a principal investigator
	 * application.
	 * @param userPending
	 */
	public void sendPendingPrincipalConfirmRequest(final LabPending labPending);

	/**
	 * Sends an email message to anonymous (not logged in) user containing an auth code to permit
	 * entering of a new password.
	 * 
	 * @param user
	 * @param authcode
	 */
	public void sendRequestNewPassword(User user, String authcode);

	/**
	 * Inform User that login name has been changed. Necessary if Admin changes user profile and login is updated.
	 * @param user
	 */
	public void informUserLoginChanged(User user);

	/**
	 * Inform user than admin has created an account for them
	 * @param user
	 * @param authcode
	 */
	void informUserAccountCreatedByAdmin(User user, String authcode);
	
    /**
    * setConfirmEmailAuthDao(ConfirmEmailAuthDao confirmEmailAuthDao)
    *
    * @param confirmEmailAuthDao
    *
    */

	
	public String getNewAuthcodeForUser(final User user);

	public String getNewAuthcodeForUserPending(UserPending userpending);

	/**
	 * Sends an email message to the department administrator to confirm or deny a principal investigator
	 * application from an already existing user that is now trying to create a lab of his/her own.
	 * @param userPending
	 */
	public void sendExistingUserPendingPrincipalConfirmRequest(final LabPending labPending);

	/**
	 * Sends an email to a job submitter stating that their newly submitted job has been recorded
	 * and is awaiting approval from PI/Lab Manager.
	 * @param Job job
	 */
	public void sendSubmitterJobStarted(final Job job);
	
	/**
	 * Sends an email to a lab's PI, stating that a member of their lab has just submitted a new job
	 * that is awaiting approval from either the PI or a Lab Manager.
	 * @param Job job
	 */
	public void sendPIJobStartedConfirmRequest(final Job job);
	
	/**
	 * Sends an email to a lab's Lab Manager(s), stating that a member of the lab has just submitted a new job
	 * that is awaiting approval from either the PI or a Lab Manager.
	 * @param Job job
	 */
	public void sendLabManagerJobStartedConfirmRequest(final Job job);
	
	/**
	 * Sends an email to the Department Administrator(s) that cover this lab, stating that a member of a specific lab has just submitted a new job
	 * that the administrator must generate a quote and either approve or reject the job submission, based on funds availability. 
	 * @param Job job
	 */
	public void sendDAJobStartedConfirmRequest(final Job job);
}

