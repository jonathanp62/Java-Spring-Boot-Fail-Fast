package net.jmp.spring.boot.failfast.services;

/*
 * (#)MapService.java   0.1.0   01/04/2025
 *
 * @author   Jonathan Parker
 *
 * MIT License
 *
 * Copyright (c) 2024 Jonathan M. Parker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.function.Consumer;

import net.jmp.spring.boot.failfast.functions.Functions;

import static net.jmp.util.logging.LoggerUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/// The map service class.
///
/// @version    0.1.0
/// @since      0.1.0
@Service
public class MapService implements ServiceRunner {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The error message when an entry is removed from the map.
    private static final String REMOVING_FROM_MAP = "A ConcurrentModificationException occurred removing an element from the map";

    /// The error message when an entry is added to the map.
    private static final String ADDING_TO_MAP = "A ConcurrentModificationException occurred adding an element from the map";

    /// The consumer function for logging a key.
    private final Consumer<String> logKey = Functions.logString(this.logger, "key: {}");

    /// The consumer function for logging an error.
    private final Consumer<String> logError = Functions.logError(this.logger);

    /// The default constructor.
    public MapService() {
        super();
    }

    /// Runs the service.
    @Override
    public void runService() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        final Map<String, String> map = new HashMap<>();

        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        map.put("key5", "value5");

        this.failFastForEach(map);  // Removes 3 and adds 6
        this.failFastIterator(map); // Removes 2 and adds 3

        // Log the final contents of the map; 1, 3, 4, 5, 6

        for (final String key : map.keySet()) {
            if (this.logger.isInfoEnabled()) {
                this.logKey.accept(key);
            }
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Runs the fail fast for iteration
    /// using the enhanced for or for-each loop.
    /// Note that this map contains elements 1 through 5.
    ///
    /// @param  map java.util.Map<java.lang.String,java.lang.String>
    private void failFastForEach(final Map<String, String> map) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(map));
        }

        for (final String key : map.keySet()) {
            if (this.logger.isInfoEnabled()) {
                this.logKey.accept(key);
            }
        }

        try {
            for (final String _ : map.keySet()) {
                map.remove("key3"); // This succeeds
            }
        } catch (final ConcurrentModificationException _) {
            this.logError.accept(REMOVING_FROM_MAP);
        }

        try {
            for (final String _ : map.keySet()) {
                map.put("key6", "value6");  // This succeeds
            }
        } catch (final ConcurrentModificationException _) {
            this.logError.accept(ADDING_TO_MAP);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Runs the fail fast for iteration
    /// using the iterator object.
    /// Note that this map contains elements 1, 2, 4, 5, 6.
    /// Element 3 was removed by the for-each loop.
    ///
    /// @param  map java.util.Map<java.lang.String,java.lang.String>
    private void failFastIterator(final Map<String, String> map) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(map));
        }

        for (final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
            final Map.Entry<String, String> entry = iterator.next();

            if (this.logger.isInfoEnabled()) {
                this.logKey.accept(entry.getKey());
            }
        }

        try {
            for (final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
                final Map.Entry<String, String> _ = iterator.next();

                map.remove("key2"); // This succeeds
            }
        } catch (final ConcurrentModificationException _) {
            this.logError.accept(REMOVING_FROM_MAP);
        }

        try {
            for (final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
                final Map.Entry<String, String> _ = iterator.next();

                map.put("key3", "value3");  // This succeeds
            }
        } catch (final ConcurrentModificationException _) {
            this.logError.accept(ADDING_TO_MAP);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }
}
