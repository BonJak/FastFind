package com.customplugins.ffast;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchForwardsAction extends AnAction {
    private final JTextField searchField;

    public SearchForwardsAction() {
        super("Search Forwards");
        searchField = new JTextField();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            Messages.showErrorDialog(project, "Please open a file first.", "Error");
            return;
        }

        JBPopupFactory factory = JBPopupFactory.getInstance();
        JBPopup popup = factory.createComponentPopupBuilder(searchField, searchField)
                .setFocusable(true)
                .setRequestFocus(true)
                .setCancelOnClickOutside(true)
                .createPopup();

        popup.showInBestPositionFor(editor);
        searchField.requestFocus();
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                if (searchTerm.isEmpty()) {
                    return;
                }

                int caretOffset = editor.getCaretModel().getOffset();
                String editorText = editor.getDocument().getText();
                int searchTermStart = -1;
                // case insensitive search
                Pattern pattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(editorText);
                matcher.region(caretOffset, editorText.length());
                if (matcher.find()) {
                    searchTermStart = matcher.start();
                } else {
                    // wrap around
                    matcher.region(0, caretOffset);
                    if (matcher.find()) {
                        searchTermStart = matcher.start();
                    } else {
                        // no match
                        return;
                    }
                }
                editor.getCaretModel().moveToOffset(searchTermStart);
                editor.getSelectionModel().setSelection(searchTermStart, searchTermStart + searchTerm.length());
                popup.cancel();
                searchField.setText("");
            }
        });
    }
}
