/*
 * Copyright 2001-2008 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scalatest.finders;

import java.util.*;

public class WordSpecFinder implements Finder {
    
  private final Set<String> scopeSet;
    
  public WordSpecFinder() {
    scopeSet = new HashSet<>();
    scopeSet.add("should");
    scopeSet.add("must");
    scopeSet.add("can");
    scopeSet.add("which");
    scopeSet.add("when");
    scopeSet.add("that"); // 'that' is deprecated
  }
  
  private String getTestNameBottomUp(MethodInvocation invocation) {
    StringBuilder result = new StringBuilder();
    while (invocation != null) {
      MethodInvocation nameInvocation = invocation;
      if (invocation.target().name().equals("taggedAs") && invocation.target() instanceof MethodInvocation) {
        nameInvocation = (MethodInvocation) invocation.target();
      }
      if (!nameInvocation.target().canBePartOfTestName() || (!invocation.name().equals("in") && invocation.canBePartOfTestName()))
        return null;
      String targetText = (nameInvocation.name().equals("in") || nameInvocation.name().equals("taggedAs"))
          ? nameInvocation.target().toString()
          : (nameInvocation.target().toString() + " " + nameInvocation.name());
      result.insert(0, targetText + " ");
      if (invocation.parent() != null && invocation.parent() instanceof MethodInvocation)
        invocation = (MethodInvocation) invocation.parent();
      else
        invocation = null;
    }
    return result.toString().trim();
  }
  
  private String getDisplayNameBottomUp(MethodInvocation invocation) {
    if (invocation.parent() == null || !(invocation.parent() instanceof MethodInvocation))
      return invocation.target().toString();
    else
      return getTestNameBottomUp((MethodInvocation) invocation.parent()) + " " + invocation.target().toString(); 
  }
  
  private List<String> getTestNamesTopDown(MethodInvocation invocation) {
    List<String> results = new ArrayList<>();
    List<AstNode> nodes = new ArrayList<>();
    nodes.add(invocation);
        
    while (nodes.size() > 0) {
      AstNode head = nodes.remove(0);
      if (head instanceof MethodInvocation) {
        MethodInvocation headInvocation = (MethodInvocation) head;
        if (headInvocation.name().equals("in")) {
          String testName = getTestNameBottomUp(headInvocation);
          if (testName != null) {
            results.add(testName);
          }
        }
        else
          nodes.addAll(0, Arrays.asList(headInvocation.children()));
      }
    }
        
    return results;
  }
  
  public Selection find(AstNode node) {
    Selection result = null;
    while (result == null) {
      if (node instanceof MethodInvocation) {
        MethodInvocation invocation = (MethodInvocation) node;
        String name = invocation.name();
        AstNode parent = invocation.parent();
        if (name.equals("in") && scopeSet.contains(parent.name())) {
          String testName = getTestNameBottomUp(invocation);
          result = testName != null ? new Selection(invocation.className(), testName, new String[] { testName }) : null;
          if (testName == null) {
            if (node.parent() != null) {
              node = node.parent();
            } else break;
          }
        }
        else if (scopeSet.contains(name)) {
          String displayName = getDisplayNameBottomUp(invocation);
          List<String> testNames = getTestNamesTopDown(invocation);
          result = new Selection(invocation.className(), displayName, testNames.toArray(new String[0]));
        }
      }
        
      if (result == null) {
        if (node.parent() != null) 
          node = node.parent();
        else
          break;
      }
    }
    return result;
  }
}