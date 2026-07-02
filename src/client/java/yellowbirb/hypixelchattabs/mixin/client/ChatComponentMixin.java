package yellowbirb.hypixelchattabs.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yellowbirb.hypixelchattabs.HypixelChatTabsClient;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

	@WrapMethod(method = "addMessageToDisplayQueue")
	private void onAddVisibleMessage(GuiMessage message, Operation<Void> original) {
		if (HypixelChatTabsClient.filter(message.content())) {
			original.call(message);
		}
	}

	@ModifyExpressionValue(method =
			{"addMessageToDisplayQueue", "addMessageToQueue", "addRecentChat"},
			at = @At(value = "CONSTANT", args = "intValue=100"))
	public int modifyMaxHistorySize(int originalMaxSize) {
		return 512;
	}
}