package com.customplugins.ffast;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.AnAction;

public class MyActions extends DefaultActionGroup {
    public void init() {
        AnAction searchForwardsAction = new SearchForwardsAction();
        AnAction searchBackwardsAction = new SearchBackwardsAction();
        // Add the actions to the toolbar or menu
        add(searchForwardsAction);
        add(searchBackwardsAction);

    }

}

