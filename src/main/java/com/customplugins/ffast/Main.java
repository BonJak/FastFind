package com.customplugins.ffast;

import com.intellij.openapi.actionSystem.AnAction;

public class Main {
    public static void main(String[] args) {
        MyActions myActions = new MyActions();
        myActions.init();
    }
}
