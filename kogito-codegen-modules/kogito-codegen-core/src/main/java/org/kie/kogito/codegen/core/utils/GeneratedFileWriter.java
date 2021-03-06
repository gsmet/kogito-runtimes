/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.codegen.core.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import org.kie.kogito.codegen.api.GeneratedFile;
import org.kie.kogito.codegen.api.GeneratedFileType;

/**
 * Writes {@link GeneratedFile} to the right directory, depending on its
 * {@link GeneratedFileType.Category}
 */
public class GeneratedFileWriter {

    public static class Builder {

        private final String classesDir;
        private final String sourcesDir;
        private final String resourcePath;
        private final String scaffoldedSourcesDir;

        /**
         *
         * @param classesDir usually target/classes/
         * @param sourcesDir usually target/generated-sources/kogito/
         * @param resourcesDir usually target/generated-resources/kogito/
         * @param scaffoldedSourcesDir usually src/main/java/
         */
        public Builder(String classesDir, String sourcesDir, String resourcesDir, String scaffoldedSourcesDir) {
            this.classesDir = classesDir;
            this.sourcesDir = sourcesDir;
            this.resourcePath = resourcesDir;
            this.scaffoldedSourcesDir = scaffoldedSourcesDir;
        }

        /**
         * @param basePath the path to which the given subdirectories will be written
         *                 e.g. ${basePath}/${classesDir}/myfile.ext
         *
         */
        public GeneratedFileWriter build(Path basePath) {
            return new GeneratedFileWriter(
                    basePath.resolve(classesDir),
                    basePath.resolve(sourcesDir),
                    basePath.resolve(resourcePath),
                    basePath.resolve(scaffoldedSourcesDir));
        }
    }

    private final Path classesDir;
    private final Path sourcesDir;
    private final Path resourcePath;
    private final Path scaffoldedSourcesDir;

    /**
     *
     * @param classesDir usually target/classes/
     * @param sourcesDir usually target/generated-sources/kogito/
     * @param resourcePath usually target/generated-resources/kogito/
     * @param scaffoldedSourcesDir usually src/main/java/
     */
    public GeneratedFileWriter(Path classesDir, Path sourcesDir, Path resourcePath, Path scaffoldedSourcesDir) {
        this.classesDir = classesDir;
        this.sourcesDir = sourcesDir;
        this.resourcePath = resourcePath;
        this.scaffoldedSourcesDir = scaffoldedSourcesDir;
    }

    public void writeAll(Collection<GeneratedFile> generatedFiles) {
        generatedFiles.forEach(this::write);
    }

    public void write(GeneratedFile f) throws UncheckedIOException {
        try {
            GeneratedFileType.Category category = f.category();
            switch (category) {
                case RESOURCE: // since codegen happens after maven-resource-plugin (both in Quarkus and SB), need to manually place in the correct (CP) location
                case COMPILED_CLASS:
                    writeGeneratedFile(f, classesDir);
                    break;
                case SOURCE:
                    if (f.type().isCustomizable()) {
                        writeGeneratedFile(f, scaffoldedSourcesDir);
                    } else {
                        writeGeneratedFile(f, sourcesDir);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Category " + category.name());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void writeGeneratedFile(GeneratedFile f, Path location) throws IOException {
        if (location == null) {
            return;
        }
        // verify if this is still needed https://issues.redhat.com/browse/KOGITO-3085
        String generatedClassFile = f.relativePath().replace("src/main/java", "");
        Files.write(
                pathOf(location, generatedClassFile),
                f.contents());
    }

    private Path pathOf(Path location, String end) throws IOException {
        Path path = location.resolve(end);
        Files.createDirectories(path.getParent());
        return path;
    }
}
