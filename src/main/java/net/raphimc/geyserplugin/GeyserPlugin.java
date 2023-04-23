/*
 * This file is part of ViaProxyGeyserPlugin - https://github.com/RaphiMC/ViaProxyGeyserPlugin
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.geyserplugin;

import net.lenni0451.lambdaevents.EventHandler;
import net.raphimc.geyserplugin.impl.VPGeyserBootstrap;
import net.raphimc.viaproxy.cli.options.Options;
import net.raphimc.viaproxy.plugins.PluginManager;
import net.raphimc.viaproxy.plugins.events.ConsoleCommandEvent;
import net.raphimc.viaproxy.plugins.events.ProxyStartEvent;
import net.raphimc.viaproxy.plugins.events.ProxyStopEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.network.AuthType;
import org.geysermc.geyser.text.GeyserLocale;

import java.io.File;

public class GeyserPlugin {

    public static final Logger LOGGER = LogManager.getLogger("Geyser");
    public static final File ROOT_FOLDER = new File(PluginManager.PLUGINS_DIR, "Geyser");

    private VPGeyserBootstrap bootstrap;

    public void onEnable() {
        ROOT_FOLDER.mkdirs();

        this.bootstrap = new VPGeyserBootstrap();
        GeyserLocale.init(this.bootstrap);
        this.bootstrap.onEnable();
        GeyserImpl.getInstance().shutdown();

        PluginManager.EVENT_MANAGER.register(this);
    }

    @EventHandler
    public void onConsoleCommand(final ConsoleCommandEvent event) {
        if (event.getCommand().equals("geyser") || event.getCommand().equals("/geyser")) {
            event.setCancelled(true);

            this.bootstrap.getGeyserCommandManager().runCommand(this.bootstrap.getGeyserLogger(), "geyser " + String.join(" ", event.getArgs()));
        }
    }

    @EventHandler
    public void onProxyStart(final ProxyStartEvent event) {
        this.bootstrap.onEnable(config -> {
            config.setAutoconfiguredRemote(true);
            config.getRemote().setAddress("127.0.0.1");
            config.getRemote().setPort(Options.BIND_PORT);
            config.getRemote().setAuthType(AuthType.OFFLINE);
        });
    }

    @EventHandler
    public void onProxyStop(final ProxyStopEvent event) {
        GeyserImpl.getInstance().shutdown();
    }

}
