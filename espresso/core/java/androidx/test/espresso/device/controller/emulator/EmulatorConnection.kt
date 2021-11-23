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
package androidx.test.espresso.device.controller.emulator

import com.android.emulator.control.EmulatorControllerGrpc
import io.grpc.CallCredentials
import io.grpc.ManagedChannel

/**
 * Provides a connection to the Android Emulator in which this test is currently running.
 *
 * This provides all the proper channels and credentials to create RPC stubs that interact with the
 * emulator.
 */
interface EmulatorConnection {
  /**
   * Gets a virtual connection to the Android Emulator in which this test is running.
   *
   * A gRPC channel should be reused when possible as this allows calls to be multiplexed through an
   * existing HTTP/2 connection.
   */
  fun channel(): ManagedChannel

  /**
   * Carries credential data that will be propagated to the emulator via request metadata for each
   * RPC. The credentials are provided by the gradle test runner and can be used as follows:
   *
   * <pre><code> EmulatorConnection connection val stub =
   * EmulatorControllerGrpc.EmulatorControllerBlockingStub.newStub(connection.channel()) response =
   * stub.withCallCredentials(connection.credentials()).bar(request); </code></pre>
   */
  fun credentials(): CallCredentials

  /**
   * Gets a fully configured default emulator controller stub that is connected to the emulator in
   * which this test is running.
   */
  fun emulatorController(): EmulatorControllerGrpc.EmulatorControllerBlockingStub
}
