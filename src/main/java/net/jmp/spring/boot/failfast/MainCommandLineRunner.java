package net.jmp.spring.boot.failfast;

/*
 * (#)MainCommandLineRunner.java    0.1.0   01/04/2025
 *
 * @author    Jonathan Parker
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

import static net.jmp.util.logging.LoggerUtils.entry;
import static net.jmp.util.logging.LoggerUtils.exit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.ApplicationContext;

import org.springframework.context.annotation.Profile;

import org.springframework.stereotype.Component;

/// The command line runner class that runs the main class.
///
/// @version 0.1.0
/// @since   0.1.0
@Component
@Profile("!test")
public class MainCommandLineRunner implements CommandLineRunner {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The application context.
    private final ApplicationContext context;

    /// A constructor.
    ///
    /// @param  context org.springframework.context.ApplicationContext
    MainCommandLineRunner(final ApplicationContext context) {
        super();

        this.context = context;
    }

    /// The run method.
    ///
    /// @param  args    java.lang.String[]
    @Override
    public void run(final String... args) throws Exception {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        this.context.getBean(Main.class).run();

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }
}
