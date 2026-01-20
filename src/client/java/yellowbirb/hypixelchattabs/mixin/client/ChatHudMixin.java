package yellowbirb.hypixelchattabs.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yellowbirb.hypixelchattabs.HypixelChatTabsClient;

@Mixin(ChatHud.class)
public class ChatHudMixin {

	@WrapMethod(method = "addVisibleMessage")
	private void onAddVisibleMessage(ChatHudLine message, Operation<Void> original) {
		if (HypixelChatTabsClient.filter(message.content())) {
			original.call(message);
		}
	}

	@ModifyExpressionValue(method =
			{"addVisibleMessage", "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", "addToMessageHistory"},
			at = @At(value = "CONSTANT", args = "intValue=100"))
	public int modifyMaxHistorySize(int originalMaxSize) {
		return 512;
	}
}