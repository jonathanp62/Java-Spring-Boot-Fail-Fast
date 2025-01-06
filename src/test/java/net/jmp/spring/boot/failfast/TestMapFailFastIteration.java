package net.jmp.spring.boot.failfast;

/*
 * (#)TestMapFailFastIteration.java 0.1.0   01/06/2025
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

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;

/// The test class for maps.
///
/// @version    0.1.0
/// @since      0.1.0
@DisplayName("Map Fail Fast Iteration")
final class TestMapFailFastIteration {
    private static final int SIZE = 10_000;

    private final Map<Integer, Integer> map = new HashMap<>(SIZE);
    private final Map<Integer, Integer> threadSafeMap = new ConcurrentHashMap<>(SIZE);

    @BeforeEach
    void beforeEach() {
        IntStream.rangeClosed(1, SIZE).forEach(key -> this.map.put(key, key));
        IntStream.rangeClosed(1, SIZE).forEach(key -> this.threadSafeMap.put(key, key));
    }

    @AfterEach
    void afterEach() {
        this.map.clear();
        this.threadSafeMap.clear();
    }

    @Test
    @DisplayName("Test Fail Fast For Each Using HashMap")
    void testFailFastForEachUsingHashMap() {
        final Map<Integer, Integer> copy = this.copy();

        final Runnable runner = () -> {
            for (final Map.Entry<Integer, Integer> entry : copy.entrySet()) {
                System.out.println(Thread.currentThread().getName() + ": " + entry.getKey());
                Thread.yield();
            }
        };

        final Runnable modifier = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                this.map.put((i + 11) * SIZE, (i + 11) * SIZE);
                Thread.yield();
            }
        };

        final Thread thread1 = new Thread(runner);
        final Thread thread2 = new Thread(runner);
        final Thread thread3 = new Thread(modifier);

        thread1.setName("HashMap Runner 1");
        thread2.setName("HashMap Runner 2");
        thread3.setName("HashMap Modifier");

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

        assertThat(this.map).hasSize(SIZE + 100);
    }

    @Test
    @DisplayName("Test Fail Fast For Each Using ConcurrentHashMap")
    void testFailFastForEachUsingConcurrentHashMap() {
        final Runnable runner = () -> {
            for (final Map.Entry<Integer, Integer> entry : this.threadSafeMap.entrySet()) {
                System.out.println(Thread.currentThread().getName() + ": " + entry.getKey());
                Thread.yield();
            }
        };

        final Runnable modifier = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                this.threadSafeMap.put((i + 11) * SIZE, (i + 11) * SIZE);
                Thread.yield();
            }
        };

        final Thread thread1 = new Thread(runner);
        final Thread thread2 = new Thread(runner);
        final Thread thread3 = new Thread(modifier);

        thread1.setName("ConcurrentHashMap Runner 1");
        thread2.setName("ConcurrentHashMap Runner 2");
        thread3.setName("ConcurrentHashMap Modifier");

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

        assertThat(this.threadSafeMap).hasSize(SIZE + 100);
    }

    @Test
    @DisplayName("Test Fail Fast Iterator Using HashMap")
    void testFailFastIteratorUsingHashMap() {
        final Map<Integer, Integer> copy = this.copy();

        final Runnable runner = () -> {
            for (final Iterator<Map.Entry<Integer, Integer>> iterator = copy.entrySet().iterator(); iterator.hasNext();) {
                final Map.Entry<Integer, Integer> entry = iterator.next();

                System.out.println(Thread.currentThread().getName() + ": " + entry.getKey());
                Thread.yield();
            }
        };

        final Runnable modifier = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                this.map.put((i + 11) * SIZE, (i + 11) * SIZE);
                Thread.yield();
            }
        };

        final Thread thread1 = new Thread(runner);
        final Thread thread2 = new Thread(runner);
        final Thread thread3 = new Thread(modifier);

        thread1.setName("HashMap Runner 1");
        thread2.setName("HashMap Runner 2");
        thread3.setName("HashMap Modifier");

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

        assertThat(this.map).hasSize(SIZE + 100);
    }

    @Test
    @DisplayName("Test Fail Fast Iterator Using ConcurrentHashMap")
    void testFailFastIteratorUsingConcurrentHashMap() {
        final Runnable runner = () -> {
            for (final Iterator<Map.Entry<Integer, Integer>> iterator = this.threadSafeMap.entrySet().iterator(); iterator.hasNext();) {
                final Map.Entry<Integer, Integer> entry = iterator.next();

                System.out.println(Thread.currentThread().getName() + ": " + entry.getKey());
                Thread.yield();
            }
        };

        final Runnable modifier = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                this.threadSafeMap.put((i + 11) * SIZE, (i + 11) * SIZE);
                Thread.yield();
            }
        };

        final Thread thread1 = new Thread(runner);
        final Thread thread2 = new Thread(runner);
        final Thread thread3 = new Thread(modifier);

        thread1.setName("HashMap Runner 1");
        thread2.setName("HashMap Runner 2");
        thread3.setName("HashMap Modifier");

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

        assertThat(this.threadSafeMap).hasSize(SIZE + 100);
    }

    private Map<Integer, Integer> copy() {
        final Map<Integer, Integer>copy = new HashMap<>(SIZE);

        copy.putAll(this.map);

        return copy;
    }
}
