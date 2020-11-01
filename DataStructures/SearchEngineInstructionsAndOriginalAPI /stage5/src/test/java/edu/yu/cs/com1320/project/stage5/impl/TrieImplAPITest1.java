package edu.yu.cs.com1320.project.stage5.impl;

import org.junit.Test;

import edu.yu.cs.com1320.project.impl.TrieImpl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TrieImplAPITest1 {


    @Test
    public void methodCount() {//need only test for non constructors
        Method[] methods = TrieImpl.class.getDeclaredMethods();
        int publicMethodCount = 0;
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                if(!method.getName().equals("equals") && !method.getName().equals("hashCode")) {
                    publicMethodCount++;
                }
            }
        }
        assertTrue(publicMethodCount == 7);
    }
}