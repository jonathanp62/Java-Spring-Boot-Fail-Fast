package net.jmp.spring.boot.failfast;

/*
 * (#)TestListFailFastIteration.java    0.1.0   01/06/2025
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
import java.util.Iterator;
import java.util.List;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;

/// The test class for the list service.
///
/// @version    0.1.0
/// @since      0.1.0
@DisplayName("List Fail Fast Iteration")
@Tag("Service")
final class TestListFailFastIteration {
    private static final int SIZE = 10_000;

    private final List<Integer> list = new ArrayList<>(SIZE);

    @BeforeEach
    void beforeEach() {
        IntStream.rangeClosed(1, SIZE).forEach(this.list::add);
    }

    @AfterEach
    void afterEach() {
        this.list.clear();
    }

    @Test
    @DisplayName("Test Fail Fast For Each")
    void testFailFastForEach() {
        final Runnable runner = () -> {
            final List<Integer> copy = this.copy(this.list);

            for (final Integer value : copy) {
                System.out.println(Thread.currentThread().getName() + ": " + value);
                Thread.yield();
            }
        };

        final Runnable modifier = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                this.list.add((i + 1) * SIZE);
                Thread.yield();
            }
        };

        final Thread thread1 = new Thread(runner);
        final Thread thread2 = new Thread(runner);
        final Thread thread3 = new Thread(modifier);

        thread1.setName("Runner 1");
        thread2.setName("Runner 2");
        thread3.setName("Modifier");

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (final InterruptedException _) {
            Thread.currentThread().interrupt();
        }

        assertThat(this.list).hasSize(SIZE + 100);
    }

    @Test
    @DisplayName("Test Fail Fast Iterator")
    void testFailFastIterator() {
        final Runnable runner = () -> {
            final List<Integer> copy = this.copy(this.list);

            for (final Iterator<Integer> iterator = copy.iterator(); iterator.hasNext();) {
                final int value = iterator.next();

                System.out.println(Thread.currentThread().getName() + ": " + value);
                Thread.yield();
            }
        };

        final Runnable modifier = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                this.list.add((i + 1) * SIZE);
                Thread.yield();
            }
        };

        final Thread thread1 = new Thread(runner);
        final Thread thread2 = new Thread(runner);
        final Thread thread3 = new Thread(modifier);

        thread1.setName("Runner 1");
        thread2.setName("Runner 2");
        thread3.setName("Modifier");

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (final InterruptedException _) {
            Thread.currentThread().interrupt();
        }

        assertThat(this.list).hasSize(SIZE + 100);
    }

    private List<Integer> copy(final List<Integer> list) {
        final List<Integer> copy = new ArrayList<>(SIZE);

        copy.addAll(list);

        return copy;
    }
}
