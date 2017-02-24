package org.openhab.binding.nest.handler;

import static org.openhab.binding.nest.NestBindingConstants.*;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.nest.internal.data.SmokeDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NestSmokeDetectorHandler extends BaseNestHandler {
    private Logger logger = LoggerFactory.getLogger(NestCameraHandler.class);
    private SmokeDetector lastData;

    public NestSmokeDetectorHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // TODO Auto-generated method stub

    }

    public void updateSmokeDetector(SmokeDetector smokeDetector) {
        logger.debug("Updating camera " + smokeDetector.getDeviceId());
        if (lastData == null || !lastData.equals(smokeDetector)) {
            Channel chan = getThing().getChannel(CHANNEL_UI_COLOR_STATE);
            updateState(chan.getUID(), new StringType(smokeDetector.getUiColorState().toString()));
            chan = getThing().getChannel(CHANNEL_BATTERY);
            updateState(chan.getUID(), new StringType(smokeDetector.getBatteryHealth().toString()));
            chan = getThing().getChannel(CHANNEL_CO_ALARM_STATE);
            updateState(chan.getUID(), new StringType(smokeDetector.getCoAlarmState().toString()));
            chan = getThing().getChannel(CHANNEL_SMOKE_ALARM_STATE);
            updateState(chan.getUID(), new StringType(smokeDetector.getSmokeAlarmState().toString()));
            chan = getThing().getChannel(CHANNEL_MANUAL_TEST_ACTIVE);
            updateState(chan.getUID(), smokeDetector.isManualTestActive() ? OnOffType.ON : OnOffType.OFF);

            if (smokeDetector.isOnline()) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE);
            }

            // Setup the properties for this device.
            updateProperty(PROPERTY_ID, smokeDetector.getDeviceId());
            updateProperty(PROPERTY_FIRMWARE_VERSION, smokeDetector.getSoftwareVersion());
        } else {
            logger.debug("Nothing to update, same as before.");
        }
    }

}
