package org.openhab.binding.nest.handler;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * base handler for the nest code, all the nest handlers are extended from this one..
 *
 * @author David Bennett - Initial contribution
 */
abstract class BaseNestHandler extends BaseThingHandler {
    private Logger logger = LoggerFactory.getLogger(BaseNestHandler.class);

    BaseNestHandler(Thing thing) {
        super(thing);
    }
}
