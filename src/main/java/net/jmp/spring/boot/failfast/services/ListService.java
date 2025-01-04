package net.jmp.spring.boot.failfast.services;

/*
 * (#)ListService.java  0.1.0   01/04/2025
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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import java.util.function.Consumer;

import net.jmp.spring.boot.failfast.functions.Functions;

import static net.jmp.util.logging.LoggerUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/// The list service class.
///
/// @version    0.1.0
/// @since      0.1.0
@Service
public class ListService implements ServiceRunner {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The error message when an item is removed from the list.
    private static final String REMOVING_FROM_LIST = "A ConcurrentModificationException occurred removing an item from the list";

    /// The error message when an item is added to the list.
    private static final String ADDING_TO_LIST = "A ConcurrentModificationException occurred adding an item to the list";

    /// The consumer function for logging a value.
    private final Consumer<String> logValue = Functions.logString(this.logger, "value: {}");

    /// The consumer function for logging an error.
    private final Consumer<String> logError = Functions.logError(this.logger);

    /// The default constructor.
    public ListService() {
        super();
    }

    /// Runs the service.
    @Override
    public void runService() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        final List<String> list = new ArrayList<>();

        list.add("value1");
        list.add("value2");
        list.add("value3");
        list.add("value4");
        list.add("value5");

        this.failFastForEach(list);     // Removes 3 and adds 6
        this.failFastIterator(list);    // Removes 2 and adds 3

        // Log the final contents of the list; 1, 3, 4, 5, 6

        for (final String value : list) {
            if (this.logger.isInfoEnabled()) {
                this.logValue.accept(value);
            }
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Runs the fail fast for iteration
    /// using the enhanced for or for-each loop.
    /// Note that this list contains items 1 through 5.
    ///
    /// @param  list    java.util.List<java.lang.String>
    private void failFastForEach(final List<String> list){
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(list));
        }

        for (final String value : list) {
            if (this.logger.isInfoEnabled()) {
                this.logValue.accept(value);
            }
        }

        try {
            for (final String _ : list) {
                list.remove("value3"); // This succeeds
            }
        } catch (final ConcurrentModificationException _) {
            this.logError.accept(REMOVING_FROM_LIST);
        }

        try {
            for (final String _ : list) {
                list.add("value6"); // This succeeds
            }
        } catch (final ConcurrentModificationException _) {
            this.logError.accept(ADDING_TO_LIST);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Runs the fail fast for iteration
    /// using the iterator object.
    /// Note that this list contains items 1, 2, 4, 5, 6.
    /// Item 3 was removed by the for-each loop.
    ///
    /// @param  list    java.util.List<java.lang.String>
    private void failFastIterator(final List<String> list){
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(list));
        }

        for (final Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
            final String value = iterator.next();

            if (this.logger.isInfoEnabled()) {
                this.logValue.accept(value);
            }
        }

        try {
            for (final Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
                final String _ = iterator.next();

                list.remove("value2"); // This succeeds
            }
        } catch (final ConcurrentModificationException _) {
            this.logError.accept(REMOVING_FROM_LIST);
        }

        try {
            for (final Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
                final String _ = iterator.next();

                list.add("value3"); // This succeeds
            }
        } catch (final ConcurrentModificationException _) {
            this.logError.accept(ADDING_TO_LIST);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }
}
