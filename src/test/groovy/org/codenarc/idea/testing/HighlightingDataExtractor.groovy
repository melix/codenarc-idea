// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.codenarc.idea.testing

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiFile
import com.intellij.testFramework.ExpectedHighlightingData
import com.intellij.util.ThreeState
import gnu.trove.THashSet
import gnu.trove.TObjectHashingStrategy
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class HighlightingDataExtractor extends ExpectedHighlightingData {


    HighlightingDataExtractor(@NotNull Document document,
                              boolean checkWarnings,
                              boolean checkWeakWarnings,
                              boolean checkInfos,
                              boolean ignoreExtraHighlighting,
                              ResourceBundle... messageBundles) {
        super(document, checkWarnings, checkWeakWarnings, checkInfos, ignoreExtraHighlighting, messageBundles)
    }

    String extractResult(@Nullable PsiFile psiFile, Collection<? extends HighlightInfo> infos, String text) {
        StringBuilder failMessage = new StringBuilder()

        Set<HighlightInfo> expectedFound = new THashSet<>(new TObjectHashingStrategy<HighlightInfo>() {
            @Override
            int computeHashCode(HighlightInfo object) {
                return object.hashCode()
            }

            @Override
            boolean equals(HighlightInfo o1, HighlightInfo o2) {
                return haveSamePresentation(o1, o2, true)
            }
        })

        if (!myIgnoreExtraHighlighting) {
            Map<ExpectedHighlightingSet, Set<HighlightInfo>> indexed = new HashMap<>()
            for (ExpectedHighlightingSet set : myHighlightingTypes.values()) {
                indexed.put(set, indexInfos(set.infos))
            }

            for (HighlightInfo info : infos) {
                ThreeState state = expectedInfosContainsInfo(info, indexed)
                if (state == ThreeState.NO) {
                    reportProblem(psiFile, failMessage, text, info, "extra ")
                    failMessage.append(" [").append(info.type).append(']')
                } else if (state == ThreeState.YES) {
                    if (expectedFound.contains(info)) {
                        if (isDuplicatedCheckDisabled) {
                            //noinspection AssignmentToStaticFieldFromInstanceMethod
                            failedDuplicationChecks++
                        } else {
                            reportProblem(psiFile, failMessage, text, info, "duplicated ")
                        }
                    }
                    expectedFound.add(info)
                }
            }
        }

        Set<HighlightInfo> indexedInfos = indexInfos(infos)
        Collection<ExpectedHighlightingSet> expectedHighlights = myHighlightingTypes.values()
        for (ExpectedHighlightingSet highlightingSet : expectedHighlights) {
            Set<HighlightInfo> expInfos = highlightingSet.infos
            for (HighlightInfo expectedInfo : expInfos) {
                if (!indexedInfos.contains(expectedInfo) && highlightingSet.enabled) {
                    reportProblem(psiFile, failMessage, text, expectedInfo, "missing ")
                }
            }
        }

        if (failMessage.length() > 0) {
            failMessage.append('\n')
            return composeText(infos, text)
        }
        return text
    }
}
