package com.teamtea.eclipticseasons.compat.modmenu;

import com.teamtea.eclipticseasons.client.gui.screen.ESModConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuCompact implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ESModConfigScreen::new;
    }
}