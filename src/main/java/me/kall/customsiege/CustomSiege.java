package me.kall.customsiege;

import me.kall.customsiege.config.SiegeConfig;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CustomSiege.MOD_ID)
public final class CustomSiege {
    public static final String MOD_ID = "customsiege";
    public static final String MOD_NAME = "CustomSiege";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    static {
        SiegeConfig.init();
    }
}
