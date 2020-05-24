/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trello.rxlifecycle3;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class RxLifecycleTest {

    private Observable<Object> observable;

    @Before
    public void setup() {
        // Simulate an actual lifecycle (hot Observable that does not end)
        observable = PublishSubject.create().hide();
    }

    @Test
    public void testBindLifecycle() {
        BehaviorSubject<Object> lifecycle = BehaviorSubject.create();
        TestObserver<Object> testObserver = observable.compose(RxLifecycle.bind(lifecycle)).test();
        testObserver.assertNotComplete();
        lifecycle.onNext(new Object());
        testObserver.assertComplete();
    }

    @Test
    public void testBindLifecycleOtherObject() {
        // Ensures it works with other types as well, and not just "Object"
        BehaviorSubject<String> lifecycle = BehaviorSubject.create();
        TestObserver<Object> testObserver = observable.compose(RxLifecycle.bind(lifecycle)).test();
        testObserver.assertNotComplete();
        lifecycle.onNext("");
        testObserver.assertComplete();
    }

    // Null checks

    @Test(expected=NullPointerException.class)
    public void testBindThrowsOnNullLifecycle() {
        //noinspection ResourceType
        RxLifecycle.bind((Observable) null);
    }

    @Test(expected = NullPointerException.class)
    public void testBindUntilThrowsOnNullLifecycle() {
        //noinspection ResourceType
        RxLifecycle.bindUntilEvent(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testBindUntilThrowsOnNullEvent() {
        BehaviorSubject<Object> lifecycle = BehaviorSubject.create();
        //noinspection ResourceType
        RxLifecycle.bindUntilEvent(lifecycle, null);
    }
}
