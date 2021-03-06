/*
 * Copyright (c) 2016, 2017, 2018 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.mixin.misc;

import com.google.common.collect.Lists;
import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.ModContainer;
import net.fabricmc.loader.ModInfo;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(CrashReport.class)
public abstract class MixinCrashReport {
	@Shadow
	public abstract CrashReportElement getElement();

	@Inject(at = @At("HEAD"), method = "generateWittyComment", cancellable = true)
	private static void generateWittyComment(CallbackInfoReturnable<String> info) {
		if (SystemUtil.getMeasuringTimeNano() % 14723 == 0) {
			info.setReturnValue("OOPSIE WOOPSIE!! Uwu We made a fucky wucky!! A wittle fucko boingo! The code monkeys at our headquarters are working VEWY HAWD to fix this!");
			info.cancel();
		}
	}

	@Inject(at = @At("RETURN"), method = "method_559")
	private void method_559(CallbackInfo info) {
		getElement().add("Fabric Mods", () -> {
			Map<String, String> mods = new TreeMap<>();
			for (ModContainer container : FabricLoader.INSTANCE.getModContainers()) {
				mods.put(container.getInfo().getName(), container.getInfo().getVersionString() + " (" + container.getOriginFile().getName() + ")");
			}

			StringBuilder modString = new StringBuilder();

			for (String id : mods.keySet()) {
				modString.append("\n\t\t");
				modString.append(id);
				modString.append(": ");
				modString.append(mods.get(id));
			}

			return modString.toString();
		});
	}
}