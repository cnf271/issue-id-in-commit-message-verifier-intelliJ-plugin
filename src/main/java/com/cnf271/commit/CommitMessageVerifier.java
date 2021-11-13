package com.cnf271.commit;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;

/**
 * @author Naween Fonseka
 * @since 11/12/2021
 */
public class CommitMessageVerifier extends CheckinHandlerFactory{

  @Override
  public @NotNull CheckinHandler createHandler(@NotNull final CheckinProjectPanel panel,
      @NotNull final CommitContext commitContext) {
    return new IssueIdVerifier(panel);
  }
}
