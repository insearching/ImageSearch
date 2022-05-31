package com.payback.imagesearch.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Observer for [LiveData]. Provides some convenience methods to assert the received events.
 */
class LiveDataTestObserver<T> : Observer<T> {
	
	/**
	 * The observed event values.
	 */
	private val observedValues = mutableListOf<T>()
	private var valueLatch = CountDownLatch(1)
	private var assertedValueIndex = 0
	
	override fun onChanged(value: T?) {
		valueLatch.countDown()
		value?.let {
			observedValues.add(it)
		}
	}
	
	/**
	 * Asserts that at least one event was received.
	 *
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun assertHasValue(): LiveDataTestObserver<T> {
		return also {
			assertTrue(observedValues.size > 0)
		}
	}
	
	/**
	 * Asserts that no events were received.
	 *
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun assertNoValue(): LiveDataTestObserver<T> {
		return also {
			assertTrue(observedValues.size == 0)
		}
	}
	
	/**
	 * Asserts that the expected amount of events were received.
	 *
	 * @param expectedSize the expected amount of events
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun assertValueCount(expectedSize: Int): LiveDataTestObserver<T> {
		return also {
			assertEquals(expectedSize, observedValues.size)
		}
	}
	
	/**
	 * Asserts that the last non-asserted event value is equal to the given value.
	 *
	 * @param expected the expected value
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun assertValue(expected: T): LiveDataTestObserver<T> {
		return also {
			if (observedValues.size == assertedValueIndex) {
				fail("Not more events")
			}
			assertEquals(expected, observedValues[assertedValueIndex++])
		}
	}
	
	/**
	 * Asserts that the last non-asserted event value matches the given method.
	 *
	 * @param valuePredicate the lambda which is used to evaluate the last event value
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun assertValue(valuePredicate: (T) -> Boolean): LiveDataTestObserver<T> {
		return also {
			assertTrue(valuePredicate(it.observedValues[assertedValueIndex++]))
		}
	}
	
	/**
	 * Asserts that there is no more value to check.
	 *
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun assertNoMoreValues(): LiveDataTestObserver<T> {
		return also {
			assertTrue(it.observedValues.size <= assertedValueIndex)
		}
	}
	
	/**
	 * Asserts that the last event value matches the given method.
	 *
	 * @param valuePredicate the lambda which is used to evaluate the last event value
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun lastValue(valuePredicate: (T) -> Unit): LiveDataTestObserver<T> {
		return also {
			valuePredicate(it.observedValues.last())
		}
	}
	
	/**
	 * Asserts that the list of all events is equal to the given list.
	 *
	 * @param expected the expected list
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun assertValues(expected: List<T>): LiveDataTestObserver<T> {
		return also {
			assertEquals(observedValues, expected)
		}
	}
	
	/**
	 * Asserts that no event value matches the given method.
	 *
	 * @param valuePredicate the lambda which is used to evaluate the event values
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun assertNever(valuePredicate: (T) -> Boolean): LiveDataTestObserver<T> {
		return also {
			observedValues.forEach { assertFalse(valuePredicate(it)) }
		}
	}
	
	/**
	 * Waits until one event value was received.
	 *
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun awaitValue(): LiveDataTestObserver<T> {
		return also {
			valueLatch.await()
		}
	}
	
	/**
	 * Waits until one event value was received or the given timeout was reached.
	 *
	 * @param timeout the maximum time to wait
	 * @param timeUnit the unit of the given [timeout]
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun awaitValue(timeout: Long, timeUnit: TimeUnit): LiveDataTestObserver<T> {
		return also {
			valueLatch.await(timeout, timeUnit)
		}
	}
	
	/**
	 * Waits until the next event value was received.
	 *
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun awaitNextValue(): LiveDataTestObserver<T> {
		return withNewLatch().awaitValue()
	}
	
	/**
	 * Waits until the next event value was received or the given timeout was reached.
	 *
	 * @param timeout the maximum time to wait
	 * @param timeUnit the unit of the given [timeout]
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun awaitNextValue(timeout: Long, timeUnit: TimeUnit): LiveDataTestObserver<T> {
		return withNewLatch().awaitValue(timeout, timeUnit)
	}
	
	/**
	 * Resets current observer values.
	 *
	 * @return the [LiveDataTestObserver] to allow chaining
	 */
	fun reset(): LiveDataTestObserver<T> {
		return also {
			observedValues.clear()
		}
	}
	
	private fun withNewLatch(): LiveDataTestObserver<T> {
		return also {
			valueLatch = CountDownLatch(1)
		}
	}
}