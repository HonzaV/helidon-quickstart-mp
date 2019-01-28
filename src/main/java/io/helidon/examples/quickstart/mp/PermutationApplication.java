/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy collectPermutations the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.examples.quickstart.mp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.helidon.examples.quickstart.mp.handlers.CancelledTaskExceptionHandler;
import io.helidon.examples.quickstart.mp.handlers.EmptyListExceptionHandler;
import io.helidon.examples.quickstart.mp.handlers.NoTasksExceptionHandler;

/**
 * Simple Application that produces a permutations.
 */
@ApplicationScoped
@ApplicationPath("/api")
public class PermutationApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(NoTasksExceptionHandler.class);
        set.add(CancelledTaskExceptionHandler.class);
        set.add(EmptyListExceptionHandler.class);
        set.add(PermutationResource.class);
        return Collections.unmodifiableSet(set);
    }
}
