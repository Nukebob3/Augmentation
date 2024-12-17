package net.nukebob.augment;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Augmentation implements ClientModInitializer {
	public static final String MOD_ID = "augment";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static KeyBinding keyBinding;
	public static int opacity;
	public static final int maxOpacity = 3;

	@Override
	public void onInitializeClient() {
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.augment.check_missing_enchants",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_ALT,
				KeyBinding.MISC_CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), KeyBindingHelper.getBoundKeyOf(Augmentation.keyBinding).getCode())) {
				if (opacity<maxOpacity) opacity++;
			} else if (opacity>0) opacity--;
		});
	}
}