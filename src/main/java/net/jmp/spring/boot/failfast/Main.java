package net.jmp.spring.boot.failfast;

/*
 * (#)Main.java 0.1.0   01/04/2025
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

import net.jmp.spring.boot.failfast.services.*;

import static net.jmp.util.logging.LoggerUtils.entry;
import static net.jmp.util.logging.LoggerUtils.exit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.env.Environment;

import org.springframework.stereotype.Component;

/// The main application class.
///
/// @version    0.1.0
/// @since      0.1.0
@Component
public class Main implements Runnable {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The environment.
    private final Environment environment;

    /// The list service.
    private final ListService listService;

    /// The map service.
    private final MapService mapService;

    /// The set service.
    private final SetService setService;

    /// The constructor.
    ///
    /// @param  environment org.springframework.core.env.Environment
    /// @param  listService net.jmp.spring.boot.failfast.services.ListService
    /// @param  mapService  net.jmp.spring.boot.failfast.services.MapService
    /// @param  setService  net.jmp.spring.boot.failfast.services.SetService
    public Main(final Environment environment,
                final ListService listService,
                final MapService mapService,
                final SetService setService) {
        super();

        this.environment = environment;
        this.listService = listService;
        this.mapService = mapService;
        this.setService = setService;
    }

    ///
    /// The run method.
    @Override
    public void run() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        this.listService.runService();
        this.mapService.runService();
        this.setService.runService();

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }
}
