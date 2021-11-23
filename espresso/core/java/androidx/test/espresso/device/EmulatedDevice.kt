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

import androidx.test.annotation.ExperimentalTestApi
import androidx.test.espresso.device.controller.DeviceControllerOperationException
import androidx.test.espresso.device.controller.emulator.EmulatorConnection
import androidx.test.espresso.device.dagger.DeviceHolder
import androidx.test.espresso.device.dagger.DeviceLayerComponent
import androidx.test.espresso.device.util.isTestDeviceAnEmulator
import androidx.test.internal.util.Checks.checkNotMainThread

/** Entry point for device centric operations */
class EmulatedDevice private constructor() {
  companion object {
    private val BASE: DeviceLayerComponent = DeviceHolder.deviceLayer()

    /**
     * Returns a properly configured connection to the Android Emulator in which this
     * test is running.
     *
     * @throws IllegalStateException when being invoked on the main thread
     * @throws DeviceControllerOperationException when not running on an emulator that is
     *  properly configured for gRPC access.
     *
     * <p>This API is experimental and subject to change or removal.
     */
    @ExperimentalTestApi
    @JvmStatic
    fun emulatorConnection(): EmulatorConnection {
     if (!isTestDeviceAnEmulator()) {
      throw DeviceControllerOperationException("Not running on an emulator.")
     }
      checkNotMainThread()
      return BASE.emulatorConnection()
    }
  }
}
