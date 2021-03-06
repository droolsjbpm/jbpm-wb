/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.workbench.pr.backend.server.util;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.jbpm.workbench.pr.backend.server.ProcessInstanceVariableMapper;
import org.jbpm.workbench.pr.model.ProcessVariableSummary;
import org.kie.server.api.model.instance.VariableInstance;

import static org.jbpm.workbench.pr.backend.server.util.VariableHelper.JBPM_DOCUMENT;

public class DocumentsVariableProcessor implements VariableHelper.VariableProcessor {

    private static final String PATTERN_SUFFIX = "+\\s\\(\\d+/\\d+\\)";

    private String type;

    public DocumentsVariableProcessor(String type) {
        this.type = type;
    }

    @Override
    public void process(long processInstanceId, String varName, String varType, List<VariableInstance> variables,
                        String deploymentId, String serverTemplateId, Consumer<ProcessVariableSummary> consumer) {
        String patternString = varName + PATTERN_SUFFIX;

        for (Iterator<VariableInstance> it = variables.iterator(); it.hasNext(); ) {
            VariableInstance var = it.next();

            if (var.getVariableName().matches(patternString)) {
                consumer.accept(new ProcessInstanceVariableMapper(deploymentId, serverTemplateId, JBPM_DOCUMENT).apply(var));
                it.remove();
            }
        }
    }

    @Override
    public String getSupportedType() {
        return type;
    }
}
