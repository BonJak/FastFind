package com.customplugins.ffast;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.diagnostic.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchBackwardsAction extends AnAction {
    private final JTextField searchField;

    public SearchBackwardsAction() {
        super("Search Backwards");
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
//                    Messages.showErrorDialog(project, "Search term cannot be empty.", "Error");
                    return;
                }
                int caretOffset = editor.getCaretModel().getOffset();
                String editorText = editor.getDocument().getText();
                // use regex for case insensitive search
                Pattern pattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(editorText);
                int searchTermStart = -1;
                while (matcher.find()) {
                    if (matcher.start() < caretOffset) {
                        searchTermStart = matcher.start();
                    }
                }
                if (searchTermStart == -1) {
                    return;
                }

                editor.getCaretModel().moveToOffset(searchTermStart);
                editor.getSelectionModel().setSelection(searchTermStart, searchTermStart + searchTerm.length());
                popup.cancel();
                searchField.setText("");
            }
        });
    }
}
