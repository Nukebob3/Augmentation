package net.nukebob.augment;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Augmentation implements ClientModInitializer {
	public static final String MOD_ID = "augment";

	public static KeyBinding keyBindingCheck;
	public static KeyBinding keyBindingAll;
	public static int opacity;
	public static final int maxOpacity = 2;

	@Override
	public void onInitializeClient() {
		keyBindingCheck = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.augment.check_missing_enchants",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_ALT,
				"category.augment.augmentation"
		));
		keyBindingAll = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.augment.check_all_missing_enchants",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_ALT,
				"category.augment.augmentation"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), KeyBindingHelper.getBoundKeyOf(Augmentation.keyBindingAll).getCode())) {
				if (opacity<maxOpacity) opacity++;
			} else if (opacity>0) opacity--;
		});
	}
}