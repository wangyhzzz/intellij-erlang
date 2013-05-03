/*
 * Copyright 2012-2013 Sergey Ignatov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intellij.erlang.go;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ArrayUtil;
import org.intellij.erlang.ErlangStructureViewFactory;
import org.intellij.erlang.ErlangSymbolIndex;
import org.intellij.erlang.psi.ErlangNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ignatov
 */
public class ErlangSymbolContributor implements ChooseByNameContributor {
  @NotNull
  @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    Collection<String> result = ErlangSymbolIndex.getNames(project);
    return ArrayUtil.toStringArray(result);
  }

  @NotNull
  @Override
  public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    GlobalSearchScope scope = includeNonProjectItems ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
    Collection<ErlangNamedElement> result = ErlangSymbolIndex.getItemsByName(name, project, scope);
    ArrayList<NavigationItem> items = new ArrayList<NavigationItem>(result.size());
    for (final ErlangNamedElement element : result) {
      items.add(new ErlangStructureViewFactory.Element(element) {
        @Override
        public String getLocationString() {
          return "(in " + element.getContainingFile().getName() + ")";
        }
      });
    }
    return items.toArray(new NavigationItem[items.size()]);
  }
}
