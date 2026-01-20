package yellowbirb.hypixelchattabs.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yellowbirb.hypixelchattabs.HypixelChatTabsClient;
import yellowbirb.hypixelchattabs.HypixelChatTabsClient.Tab;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo ci) {
        ChatHud hud = MinecraftClient.getInstance().inGameHud.getChatHud();
        for (Tab chatTab : Tab.values()) {
            String message = "X";
            switch (chatTab) {
                case ALL -> message = "A";
                case PARTY -> message = "P";
                case GUILD -> message = "G";
                case PRIVATE -> message = "PM";
                case COOP -> message = "CC";
            }
            ButtonWidget tabButton = ButtonWidget.builder(Text.literal(message), (btn) -> {
                HypixelChatTabsClient.tab = chatTab;
                hud.reset();
            }).dimensions(5 + chatTab.ordinal() * 22, this.height - hud.getHeight() - 40 - 20 - 5, 20, 20).build();

            addDrawableChild(tabButton);
        }
    }
}
