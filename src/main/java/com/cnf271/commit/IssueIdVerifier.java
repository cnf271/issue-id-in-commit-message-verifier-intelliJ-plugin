package com.cnf271.commit;

import java.awt.BorderLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.Nullable;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.util.ui.UIUtil;

/**
 * @author Naween Fonseka
 * @since 11/12/2021
 */
public class IssueIdVerifier extends CheckinHandler {
  private static final String CHECKER_STATE_KEY = "COMMIT_MESSAGE_ISSUE_CHECKER_STATE_KEY";
  private final CheckinProjectPanel panel;

  public IssueIdVerifier(final CheckinProjectPanel panel) {
    this.panel = panel;
  }

  public static boolean isCheckMessageEnabled() {
    return PropertiesComponent.getInstance().getBoolean(CHECKER_STATE_KEY, true);
  }

  @Override
  public @Nullable RefreshableOnComponent getBeforeCheckinConfigurationPanel() {
    final JCheckBox checkBox = new JCheckBox("Check reference to issue in message");

    return new RefreshableOnComponent() {
      @Override
      public JComponent getComponent() {
        final JPanel root = new JPanel(new BorderLayout());
        root.add(checkBox, "West");
        return root;
      }

      @Override
      public void refresh() {}

      @Override
      public void saveState() {
        PropertiesComponent.getInstance().setValue(CHECKER_STATE_KEY, checkBox.isSelected());
      }

      @Override
      public void restoreState() {
        checkBox.setSelected(isCheckMessageEnabled());
      }
    };
  }

  @Override
  public ReturnResult beforeCheckin() {
    if (!isCheckMessageEnabled()) return super.beforeCheckin();

    final Pattern pattern = Pattern.compile("[A-Z]+\\-\\d+");
    final Matcher matcher = pattern.matcher(panel.getCommitMessage());

    if (matcher.find()) {
      return ReturnResult.COMMIT;
    }

    final String html =
        "<html><body>"
            + "Issue id is missing in the commit message."
            + "<br>"
            + "<br>"
            + "Do you wish to continue ?"
            + "</body></html>";

    int forceCommit = Messages.showYesNoDialog(html, "Missing Issue Id", UIUtil.getWarningIcon());

    if (forceCommit == Messages.YES) {
      return ReturnResult.COMMIT;
    }

    return ReturnResult.CANCEL;
  }
}
