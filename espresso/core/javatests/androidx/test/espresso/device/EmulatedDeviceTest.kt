/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.test.espresso.device

import com.google.protobuf.Empty
import java.util.concurrent.TimeUnit
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.lessThan
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EmulatedDeviceTest {

  @Test
  fun emulatorConnection_hasChannel() {
    assertNotNull(EmulatedDevice.emulatorConnection().channel())
  }

  @Test
  fun emulatorConnection_hasCallCredentials() {
    assertNotNull(EmulatedDevice.emulatorConnection().credentials())
  }

  @Test
  fun emulatorConnection_hasEmulatorController() {
    assertNotNull(EmulatedDevice.emulatorConnection().emulatorController())
  }

  @Test
  fun emulatorConnection_emulatorUptimeIncreases() {
    val controller = EmulatedDevice.emulatorConnection().emulatorController()
      .withDeadlineAfter(2, TimeUnit.SECONDS)
    val fst = controller.getStatus(Empty.getDefaultInstance())
    assertThat("Uptime", fst.uptime, greaterThan(0))

    val snd = controller.getStatus(Empty.getDefaultInstance())
    assertThat("Uptime", fst.uptime, lessThan(snd.uptime))
  }
}
