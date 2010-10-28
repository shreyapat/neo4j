/**
 * Copyright (c) 2002-2010 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neo4j.server.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.junit.Ignore;
import org.junit.Test;

public class ConfiguratorTest {
    
    @Test
    public void shouldProvideAConfiguration() {
        Configuration config = new Configurator().configuration();
        assertNotNull(config);
    }

    @Test
    @Ignore("This is a functional test, so move it accordingly.")
    public void shouldUseSpecifiedConfigDir() {
        Configuration testConf = new Configurator(new File("src/test/resources/etc/neo-server")).configuration();

        final String EXPECTED_VALUE = "bar";
        assertEquals(EXPECTED_VALUE, testConf.getString("org.neo4j.foo"));
    }
    
    @Test
    public void shouldAcceptDuplicateKeysWithSameValueAndLogDuplication() throws IOException {
        File configA = createTempPropertyFile();
        File configB = createTempPropertyFile();
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(configA));
        writer.write("foo=bar");
        writer.close();
        
        writer = new BufferedWriter(new FileWriter(configB));
        writer.write("foo=bar");
        writer.close();
        
        Configurator configurator = new Configurator(configA, configB);
        Configuration testConf = configurator.configuration();
        
    }

    private File createTempPropertyFile() throws IOException {
        File f = File.createTempFile("test-", ".properties");
        f.deleteOnExit();
        return f;
    }
    
    @Test(expected = InvalidServerConfigurationException.class)
    public void shouldFailOnDuplicateKeysWithDifferentValues() throws IOException {
        File configA = createTempPropertyFile();
        File configB = createTempPropertyFile();
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(configA));
        writer.write("foo=bar");
        writer.close();
        
        writer = new BufferedWriter(new FileWriter(configB));
        writer.write("foo=bar");
        writer.close();
        
        Configurator configurator = new Configurator(configA, configB);
        Configuration testConf = configurator.configuration();

    }
}