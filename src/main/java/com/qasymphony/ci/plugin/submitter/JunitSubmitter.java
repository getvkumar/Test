package com.qasymphony.ci.plugin.submitter;

import com.qasymphony.ci.plugin.exception.StoreResultException;
import com.qasymphony.ci.plugin.exception.SubmittedException;
import com.qasymphony.ci.plugin.model.SubmittedResult;
import hudson.model.AbstractBuild;

import java.io.IOException;

/**
 * @author trongle
 * @version 10/21/2015 2:37 PM trongle $
 * @since 1.0
 */
public interface JunitSubmitter {
  /**
   * Submit test result to qTest
   *
   * @param junitSubmitterRequest
   * @return
   * @throws Exception
   */
  JunitSubmitterResult submit(JunitSubmitterRequest junitSubmitterRequest) throws SubmittedException;

  /**
   * @param build
   * @param result
   * @return
   * @throws IOException
   * @throws InterruptedException
   * @throws StoreResultException
   */
  SubmittedResult storeSubmittedResult(AbstractBuild build, JunitSubmitterResult result)
    throws StoreResultException;
}
